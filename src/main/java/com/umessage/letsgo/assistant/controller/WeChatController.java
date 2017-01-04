package com.umessage.letsgo.assistant.controller;

import com.thoughtworks.xstream.XStream;
import com.umessage.letsgo.assistant.common.utils.Signature;
import com.umessage.letsgo.assistant.common.utils.WxPayOrderNotifyResult;
import com.umessage.letsgo.assistant.model.po.WeChatOrderInfoEntity;
import com.umessage.letsgo.assistant.model.vo.response.WxPayOrderNotifyResponse;
import com.umessage.letsgo.assistant.service.IPayInfoService;
import com.umessage.letsgo.assistant.service.IWeChatOrderInfoService;
import com.umessage.letsgo.assistant.service.IWeChatSendMessageService;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.xml.XStreamInitializer;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpPayService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.util.xml.XStreamTransformer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author gaofei
 */
@RestController
@RequestMapping("/portal")
public class WeChatController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IWeChatOrderInfoService wechatOrderInfoService;
    @Autowired
    private IWeChatSendMessageService weChatSendMessageService;
    @Autowired
    private WeChatOrderInfoController weChatOrderInfoController;
    @Autowired
    private WxMpService wxService;
    @Autowired
    private IPayInfoService payService;

    @Autowired
    private WxMpMessageRouter router;



    @GetMapping(produces = "text/plain;charset=utf-8")
    public @ResponseBody String authGet(
            @RequestParam(name = "signature",
                    required = false) String signature,
            @RequestParam(name = "timestamp",
                    required = false) String timestamp,
            @RequestParam(name = "nonce", required = false) String nonce,
            @RequestParam(name = "echostr", required = false) String echostr) {

        this.logger.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature,
            timestamp, nonce, echostr);

        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }

        if (this.wxService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }

        return "非法请求";
    }

    @PostMapping(produces = "application/xml; charset=UTF-8")
    public @ResponseBody String post(@RequestBody String requestBody,
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam(name = "encrypt_type",
                    required = false) String encType,
            @RequestParam(name = "msg_signature",
                    required = false) String msgSignature) {
        this.logger.info(
            "\n接收微信请求：[signature=[{}], encType=[{}], msgSignature=[{}],"
                + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
            signature, encType, msgSignature, timestamp, nonce, requestBody);

        if (!this.wxService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }
        
        String out = null;
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toXml();
        } else if ("aes".equals(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(
                requestBody, this.wxService.getWxMpConfigStorage(), timestamp,
                nonce, msgSignature);
            this.logger.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage
                .toEncryptedXml(this.wxService.getWxMpConfigStorage());
        }

        this.logger.debug("\n组装回复信息：{}", out);

        return out;
    }

    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return this.router.route(message);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 订单支付完成之后异步回调
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/payNotify")
    public String payNotify(HttpServletRequest request, HttpServletResponse response, Model model) throws WxErrorException, IOException, ParserConfigurationException, SAXException {

        long start = System.currentTimeMillis();
       logger.info("进入获取支付成功后的回调方法");
        String result = null;
        WxMpPayService wxMpPayService = wxService.getPayService();
        String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        logger.info("xmlResult 信息为： " + xmlResult);
        logger.info("回调信息获取完成");
        XStreamTransformer.registerClass(WxPayOrderNotifyResult.class);
        WxPayOrderNotifyResult wxPayOderNotifyResult =  XStreamTransformer.fromXml(WxPayOrderNotifyResult.class,xmlResult);
        logger.info("回调信息转换为map 完成");
        String signature = wxPayOderNotifyResult.getSign();
        logger.info("获取sign 为 :"+ signature);

        logger.info("进行签名的验证");
        if(!Signature.checkIsSignValidFromResponseString(xmlResult,wxService.getWxMpConfigStorage().getPartnerKey())) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
         }

        //返回微信支付成功的信息
        logger.info("返回微信支付成功的信息");
        StringBuilder xmlReturnResult = new StringBuilder();
        WxPayOrderNotifyResponse wxPayOrderNotifyResponse = new WxPayOrderNotifyResponse();
        try{
            wxPayOrderNotifyResponse.setReturn_code("SUCCESS");
            wxPayOrderNotifyResponse.setReturn_msg("OK");
            XStream xstream = XStreamInitializer.getInstance();
            xmlReturnResult.append("<xml>");
            xmlReturnResult.append("<return_code>");
            xmlReturnResult.append("SUCCESS");
            xmlReturnResult.append("</return_code>");

            xmlReturnResult.append("<return_msg>");
            xmlReturnResult.append("OK");
            xmlReturnResult.append("</return_msg>");
            xmlReturnResult.append("</xml>");


            String orderId = wxPayOderNotifyResult.getOut_trade_no();
            //检查orderinfo表中的支付状态是否改为1 ，防止后台系统因为、网络、服务器等出现异常，最终未接收到支付通知为改变状态
            logger.info("修改订单状态");
            logger.info("商户订单号为："+ orderId);

            WeChatOrderInfoEntity wxOrderInfo = wechatOrderInfoService.findOrderInfoByOrderId(orderId);
            if(wxOrderInfo.getStatus()==0){
                WeChatOrderInfoEntity weChatOrderInfoEntity =  wechatOrderInfoService.updateOrderStatusToPay(orderId);
                // 推送支付成功的模板消息
                logger.info("推送支付成功的模板消息");
                weChatSendMessageService.sendPaySuccessTemplateMessage(wxPayOderNotifyResult.getOpenid(),weChatOrderInfoEntity);

                //修改支付记录表中的数据 添加微信支付订单号
                logger.info("修改支付记录表中的数据 添加微信支付订单号");
                payService.generateUpdateDate(wxPayOderNotifyResult);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        logger.info("回调方法耗时：" + (end-start)/1000);
        return xmlReturnResult.toString();
    }

}
