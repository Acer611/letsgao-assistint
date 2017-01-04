package com.umessage.letsgo.assistant.controller;

import com.github.pagehelper.Page;
import com.umessage.letsgo.assistant.common.exception.BusinessException;
import com.umessage.letsgo.assistant.common.utils.HttpClientUtil;
import com.umessage.letsgo.assistant.common.utils.MD5;
import com.umessage.letsgo.assistant.model.po.WeChatInfoEntity;
import com.umessage.letsgo.assistant.model.po.WeChatOrderInfoEntity;
import com.umessage.letsgo.assistant.service.IRewardWithdrawService;
import com.umessage.letsgo.assistant.service.IWeChatInfoService;
import com.umessage.letsgo.assistant.service.IWeChatOrderInfoService;
import com.umessage.letsgo.assistant.service.IWeChatSendMessageService;
import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpPayService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.pay.result.WxPayOrderCloseResult;
import me.chanjar.weixin.mp.bean.pay.result.WxPayOrderQueryResult;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单操作的基本方法
 * Created by gaofei on 2016/12/22.
 */
@Controller
@RequestMapping("/order/operation")
public class OrderOperationController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IWeChatOrderInfoService wechatOrderInfoService;
    @Autowired
    private IWeChatSendMessageService weChatSendMessageService;
    @Autowired
    private IWeChatInfoService weChatInfoService;
    @Autowired
    private WxMpService wxService;
    @Autowired
    protected WxMpService wxMpService;
    @Autowired
    protected  WxMpConfigStorage wxMpConfigStorage;
    @Autowired
    protected IRewardWithdrawService rewardWithdrawService;



    /**
     * 去保存订单页面
     * @return
     */
    @RequestMapping("/toInsertPage")
    public String toInsertPage(HttpServletRequest request, Model model){
        logger.info("进入跳转下单页面的方法");
        String openid = request.getParameter("qimoClientId");
        String exten = request.getParameter("exten");
        //String token = request.getParameter("token");
        //String tokenId = request.getParameter("tokenId");
        //校验token
        //this.httpClientTools(token, tokenId);
        //String exten = "1556";
        logger.info("用户的openId 是" + openid);
        model.addAttribute("opdeId",openid);
        model.addAttribute("exten",exten);
        //model.addAttribute("token",token);
        //model.addAttribute("tokenId",tokenId);
        return "customer/ordersavenew";
    }


    /**
     * 生成订单
     * @param
     * @return
     */
    @RequestMapping("/insert")
    @ResponseBody
    public Map<String, Object> insert(HttpServletRequest request,WeChatOrderInfoEntity orderInfoEntity) throws BusinessException{
        logger.info("进入下单的方法");
        Map<String,Object> map = new HashMap<>();
        String openId = request.getParameter("opdeId");
        //String openId = "oy6Qtw6pICdD86JKObwBdoV3cZjA";
        logger.info("当前用户的openID 是：" + openId );
        WeChatInfoEntity weChatInfoEntity =  weChatInfoService.findWecahtUserByOpenID(openId);
        if (Integer.parseInt(weChatInfoEntity.getParentid())>100){
            logger.info("获取到当前用户的信息：" + weChatInfoEntity );
            WeChatInfoEntity parendInfoEntity =  weChatInfoService.findWecahtUserByID(weChatInfoEntity.getParentid());
            orderInfoEntity.setSellerOpenid(parendInfoEntity.getOpenid());
        }
        orderInfoEntity.setCustomerOpenid(openId);
        try {
            // 获取微信用户基本信息
            WxMpUser userWxInfo = wxMpService.getUserService().userInfo(openId, null);
            String nickname = "";
            if (userWxInfo!=null){
                nickname = userWxInfo.getNickname();
                nickname = URLEncoder.encode(nickname, "utf-8");
            }
            wechatOrderInfoService.insert(orderInfoEntity,nickname);
            Long id = orderInfoEntity.getId();
            //TODO 推送支付链接模板消息
            weChatSendMessageService.sendPayUrlTemplateMessage(openId,orderInfoEntity);
            map.put("result",0);
            map.put("message","添加成功");
        }catch (Exception e){
            System.out.println(e.getMessage());
            map.put("result",-1);
            map.put("message","添加失败");
        }
        return map;
    }


    /**
     * 去修改订单页面
     * @return
     */
    @RequestMapping("/toUpdatePage")
    public String toUpdatePage(HttpServletRequest request, Long id, Model model){
        WeChatOrderInfoEntity orderInfoEntity = wechatOrderInfoService.select(id);
        model.addAttribute("orderInfoEntity",orderInfoEntity);
        return "customer/ordersave";
    }


    /**
     * 更新订单
     * @param
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public String update(WeChatOrderInfoEntity orderInfoEntity, HttpServletRequest request) throws BusinessException{
        Map<String,Object> map = new HashMap<>();
        /*
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
        String checkInTimes = request.getParameter("checkInTimes");
        */
        String openId = request.getParameter("opdeId");
        try {
            wechatOrderInfoService.update(orderInfoEntity);
            map.put("result",0);
            map.put("message","更新成功");
        }catch (Exception e){
            System.out.println(e.getMessage());
            map.put("result",-1);
            map.put("message","更新失败");
        }
        return "redirect:/order/operation/getAllOrderList?pageNumber=1&openId="+openId;
    }

    /**
     * 取消订单
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String,Object> delete(Long id) throws BusinessException{
        Map<String,Object> map = new HashMap<>();
        try {
            wechatOrderInfoService.delete(id);
            map.put("result",0);
            map.put("message","删除成功");
        }catch (BusinessException e){
            System.out.println(e.getCode());
            System.out.println(e.getTargetUrl());
            map.put("result",-1);
            map.put("message","删除失败");
            map.put("url",e.getTargetUrl());
            map.put("code",e.getCode());
        }
        return map;
    }
    /**
     * 获取全部订单列表（助手端）
     * 当前搜索为完全匹配不支持模糊查询功能
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/getAllOrderList")
    public String  getAllOrderList(HttpServletRequest request, Model model) throws BusinessException{
        logger.info("进入助手端获取全部订单列表方法");
        String status = request.getParameter("status");
        String keyWords = request.getParameter("keyWords");
        String pageNumber = request.getParameter("pageNumber");
        WeChatOrderInfoEntity wechatOrderInfoEntity = new WeChatOrderInfoEntity();
        if(null != status && !"".equals(status)){
            wechatOrderInfoEntity.setStatus(Integer.parseInt(status));
        }
        if(null != keyWords && !"".equals(keyWords)){
            wechatOrderInfoEntity.setKeyWords(keyWords);
        }else{
            wechatOrderInfoEntity.setKeyWords("");
        }

        logger.info("要查询的订单状态和关键字分别是:" + status + " , " + keyWords);
        Page<WeChatOrderInfoEntity> orderList = wechatOrderInfoService.selectOrderList(wechatOrderInfoEntity, pageNumber);
        Map resultMap = new HashMap<>();
        resultMap.put("orderList",orderList);
        resultMap.put("total",orderList.getTotal());
        resultMap.put("page",orderList.getPages());
        model.addAttribute("status",status);
        model.addAttribute("keyWords",keyWords);
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("resultMap",resultMap);
        return  "customer/orderlist";
    }

    /**
     * 获取当前用户订单列表（助手端）
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/getCurrentUserOrderList")
    public String  getCurrentUserOrderList(HttpServletRequest request, Model model) throws BusinessException{
        logger.info("进入助手端获取订当前用户单列表方法");
        //String status = request.getParameter("status");
        //String keyWords = request.getParameter("keyWords");
        String openId = request.getParameter("qimoClientId");
        if(null==openId || "".equals(openId)){
            openId = request.getParameter("openId");
        }
        logger.info("当前用户的openid是：" + openId);
        String pageNumber = request.getParameter("pageNumber");
        WeChatOrderInfoEntity wechatOrderInfoEntity = new WeChatOrderInfoEntity();
        if(null != openId && !"".equals(openId)){
            wechatOrderInfoEntity.setCustomerOpenid(openId);
        }
        Page<WeChatOrderInfoEntity> orderList = wechatOrderInfoService.selectOrderList(wechatOrderInfoEntity, pageNumber);
        Map resultMap = new HashMap<>();
        resultMap.put("orderList",orderList);
        resultMap.put("openId",openId);
        resultMap.put("total",orderList.getTotal());
        resultMap.put("page",orderList.getPages());
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("resultMap",resultMap);
        return  "customer/curOrderlist";
    }

    /**
     * 获取待处理订单列表（助手端）
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/getTODOOrderList")
    public String  getTODOOrderList(HttpServletRequest request, Model model) throws BusinessException{
        logger.info("进入助手端获取待处理订单列表方法");
        String pageNumber = request.getParameter("pageNumber");
        WeChatOrderInfoEntity wechatOrderInfoEntity = new WeChatOrderInfoEntity();
        //Page<WeChatOrderInfoEntity> orderList = wechatOrderInfoService.selectOrderList(wechatOrderInfoEntity, pageNumber);
        Page<WeChatOrderInfoEntity> orderList = wechatOrderInfoService.selectTODOOrderList(wechatOrderInfoEntity, pageNumber);
        Map resultMap = new HashMap<>();
        resultMap.put("orderList",orderList);
        resultMap.put("total",orderList.getTotal());
        resultMap.put("page",orderList.getPages());
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("resultMap",resultMap);
        return  "customer/curOrderlist";
    }

    /**
     * 查看订单详情（助手端）
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/selectOrderInfoDetail")
    public String selectOrderInfoDetail(HttpServletRequest request,Long id, Model model){
        Object key = request.getSession(false).getAttribute("key");
        WeChatOrderInfoEntity orderInfoEntity = wechatOrderInfoService.select(id);
        model.addAttribute("orderInfoEntity",orderInfoEntity);
        return "";
    }

    /**
     * 更改支付状态
     * @param id
     * @return
     */
    @RequestMapping("/updateStatus")
    @ResponseBody
    public Map<String,Object> updateStatus(Long id) throws BusinessException {
        Map<String,Object> map = new HashMap<>();
        try {
            WeChatOrderInfoEntity wechatOrderInfoEntity = wechatOrderInfoService.select(id);
            if (wechatOrderInfoEntity == null){
                map.put("result",-2);
                map.put("message","未查询到对应的订单");
                return map;
            }
            wechatOrderInfoEntity = wechatOrderInfoService.updateStatus(wechatOrderInfoEntity);
            //为推荐人增加收益
            WeChatInfoEntity weChatInfoEntity = weChatInfoService.findWecahtUserByOpenID(wechatOrderInfoEntity.getSellerOpenid());
            if (weChatInfoEntity != null){
                weChatInfoEntity.setReward(weChatInfoEntity.getReward()+wechatOrderInfoEntity.getSellerReward());
                weChatInfoService.updateStatus(weChatInfoEntity,0,weChatInfoEntity.getOpenid());
            }
            //TODO 推送订单完成模板消息
            weChatSendMessageService.sendOrderStatesEndTemplateMessage(wechatOrderInfoEntity.getCustomerOpenid(),wechatOrderInfoEntity);
            map.put("result",0);
            map.put("message","更新状态成功");
        }catch (Exception e){
            System.out.println(e.getMessage());
            map.put("result",-1);
            map.put("message","更新状态失败");
        }
        return map;
    }


    /**
     * 查询支付结果
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/queryOrderInfo")
    public String queryOrderInfo(HttpServletRequest request,Model model) throws WxErrorException {
        logger.info("进入获取查看支付结果方法");
        String orderId = request.getParameter("orderID");
        WxMpPayService wxMpPayService = wxService.getPayService();
        try{
            WxPayOrderQueryResult wxPayOrderQueryResult = wxMpPayService.queryOrder(null,orderId);
            logger.info("获取订单支付信息完成");
            //判断支付状态是否成功
            if("SUCCESS".equalsIgnoreCase(wxPayOrderQueryResult.getTradeState())) {
                //关闭订单
                WxPayOrderCloseResult wxPayOrderCloseResult = wxMpPayService.closeOrder(orderId);
                logger.info("关闭订单完成");
                //检查orderinfo表中的支付状态是否改为1 ，防止后台系统因为、网络、服务器等出现异常，最终未接收到支付通知为改变众泰
                logger.info("更新订单信息表中的支付状态");
                wechatOrderInfoService.updateOrderStatusToPay(orderId);
            }else{
                if(wxPayOrderQueryResult.getErrCode().equals("ORDERNOTEXIST")){
                    //查询系统中不存在此交易订单号
                    //throw WxErrorException()
                    return "查询系统中不存在此交易订单号";
                }else if(wxPayOrderQueryResult.getErrCode().equals("SYSTEMERROR")){
                    logger.info("第一次获取订单信息失败，尝试二次获取订单信息");
                    //后台系统返回错误,系统异常，请再调用发起查询
                    wxPayOrderQueryResult = wxMpPayService.queryOrder(null,orderId);
                    if("SUCCESS".equalsIgnoreCase(wxPayOrderQueryResult.getTradeState())) {
                        //关闭订单
                        WxPayOrderCloseResult wxPayOrderCloseResult = wxMpPayService.closeOrder(orderId);
                        logger.info("关闭订单完成");
                        wechatOrderInfoService.updateOrderStatusToPay(orderId);
                        logger.info("更新订单信息表中的支付状态完成");
                    }
                }else{
                    return "查询微信订单错误";
                }
            }
        }catch (Exception e){
            WxError wxError = new WxError();
            wxError.setErrorMsg(e.getMessage().toString());
            throw new WxErrorException(wxError);

        }
        return null;
    }


    /**
     * 根据订单id 查看订单内容
     * @param id
     * @return
     * @throws WxErrorException
     */
    @RequestMapping("/selectOrderDetailById")
    @ResponseBody
    public Map selectOrderDetailById(Long id) throws BusinessException {
        WeChatOrderInfoEntity orderInfoEntity = wechatOrderInfoService.select(id);
        Map resultMap = new HashMap<>();
        resultMap.put("orderDetail",orderInfoEntity.getOrderDetail());
        return resultMap;
    }


    public String httpClientTools(String token, String tokenId) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String datetime = sdf.format(date);
        String sigs = "N00000011465ef523840-d1a9-11e6-ad3c-9d5cc7a49d3d"+datetime;
        String authorizations = "N00000011465:"+datetime;
        String sig = MD5.encodeByMD5(sigs);
        String authorization = Base64.encodeBase64String(authorizations.getBytes());
        //String url = "http://api.7moor.com/sso/token/checkTokenLegal?token=" + token + "&tokenId=" + tokenId +"&sig="+sig;
        String url = "http://api.7moor.com/sso/token/checkTokenLegal/" + token + "/" + tokenId;

     /*
        Map<String, Object> _params = new HashMap<String, Object>();
        String str = HttpClientUtil.doPost(url, _params, "UTF-8", true, authorization);
        if (str != null) {
            logger.info("http Post request result:" + str);
        } else {
            logger.info("http Post request process fail");
        }
*/

        JSONObject jsonObject = HttpClientUtil.httpGet(url,authorization);
        String code = "";
        String message = "";
        if(jsonObject != null) {
            code = jsonObject.getString("code");
            message = jsonObject.getString("message");
            logger.info("http Get request process sucess");
            logger.info("code:" + code);
            logger.info("message:" + message);
        }else {
            logger.info("http Get request process fail");
        }
        if ("200".equals(code)){
            message = "合法请求,校验成功";
        }else if ("500".equals(code)){
            message = "服务器错误";
        }else if ("501".equals(code)){
            message = "未查询到对应token数据";
        }else if ("502".equals(code)){
            message = "很抱歉,token已经超时";
        }else if ("503".equals(code)){
            message = "token不匹配,非法请求";
        }
        jsonObject.put("message",message);
        return "";
    }

    /**
     * 跳转到退款金额页面
     * @param id  订单的id
     * @param model
     * @return
     */
    @RequestMapping("/toRefundPage")
    public String toRefundPage(Long id,Model model){
        WeChatOrderInfoEntity orderInfoEntity = wechatOrderInfoService.select(id);
        model.addAttribute("orderInfoEntity",orderInfoEntity.getOrderDetail());
        //TODO 添加输入退款金额的页面
        return "customer/refund";
    }

    /**
     *
     * @param id 订单的id
     * @param refundFee 退款金额
     * @return
     * @throws WxErrorException
     * @throws IOException
     */
    @RequestMapping("/refund")
    public String refund(Long id, Double refundFee) throws WxErrorException, IOException {
        logger.info("开始调用退款的方法");
        //根据id 获取订单信息
        WeChatOrderInfoEntity orderInfoEntity = wechatOrderInfoService.select(id);
        if(null==orderInfoEntity){return "订单输入有误请核查!";}
        wechatOrderInfoService.refund(orderInfoEntity,refundFee);

        return "";
    }


}
