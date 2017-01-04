package com.umessage.letsgo.assistant.controller;

import com.umessage.letsgo.assistant.common.helper.UserLoginHelper;
import com.umessage.letsgo.assistant.common.utils.HttpServletUtil;
import com.umessage.letsgo.assistant.model.po.WeChatInfoEntity;
import com.umessage.letsgo.assistant.service.IWeChatInfoService;
import com.umessage.letsgo.assistant.wechat.security.WxUser;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by gaofei on 2016/12/23.
 */
@Controller
@RequestMapping("/wechat")
public class WeChatUserInfoController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WxMpService wxService;
    @Autowired
    private IWeChatInfoService weChatInfoService;
    @Autowired
    private UserLoginHelper loginHelper;


    /**
     * 导游点击我的二维码操作
     * @param request
     * @param model
     * @return
     * @throws WxErrorException
     */
    @RequestMapping("/findWeChatInfo")
    public String findWeChatInfo(HttpServletRequest request,  Model model) throws WxErrorException {
        logger.info("进入获取二维码信息的接口");
        String openId = request.getParameter("openId");
        if(null!=openId && !"".equals(openId)){
            openId = openId;
        }else{
            WxUser wxUser = loginHelper.getWxUser();
            openId = wxUser.getUsername();
        }
        this.logger.info("当前事件为点击我的二维码操作事件 " );
        WeChatInfoEntity weChatInfo =  weChatInfoService.findWecahtUserByOpenID(openId);
        this.logger.info("生成永久的二维码 " );
        WxMpQrCodeTicket wxMpQrCodeTicket = null;
        if(null == weChatInfo.getTicket()){
            //根据用户的id 生成永久的二维码
            wxMpQrCodeTicket = wxService.getQrcodeService().qrCodeCreateLastTicket(String.valueOf(weChatInfo.getID()));
            if(null != wxMpQrCodeTicket){
                this.logger.info("更新微信信息表中ticket 和ticket_url字段 " );
                String ticketUrl = wxMpQrCodeTicket.getUrl();
                weChatInfoService.updateTickURLByOpenID(ticketUrl,wxMpQrCodeTicket.getTicket(),openId);
            }
            weChatInfo.setTicket("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + wxMpQrCodeTicket.getTicket());
        }else{
            weChatInfo.setTicket("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + weChatInfo.getTicket());
        }

        WxJsapiSignature jsapiSignature = null;
        String url = HttpServletUtil.getCompleteUrl(request);
        logger.info("url:"+url);
        try {
            jsapiSignature = wxService.createJsapiSignature(url);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        logger.info("appid:"+jsapiSignature.getAppid()+",noncestr:"+jsapiSignature.getNoncestr()
                +",timestamp:"+jsapiSignature.getTimestamp()+",signature:"+jsapiSignature.getSignature());

        model.addAttribute("jsapiSignature", jsapiSignature);
        //
        WxMpUser userWxInfo = new WxMpUser();
        try {
            userWxInfo = wxService.getUserService().userInfo(openId);
        } catch (WxErrorException e) {
            logger.info("获取微信信息失败：" + e.getMessage());
        }
        weChatInfo.setNickname(userWxInfo.getNickname());
        weChatInfo.setHeadImgUrl(weChatInfo.getHeadImgUrl());
        model.addAttribute("wechatInfo",weChatInfo);
        return "assistant";
    }


    /**
     * 邀请同行的操作
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/askUser")
    public String askUser(HttpServletRequest request, Model model){

        String openId = request.getParameter("openId");
        //openId = "oy6Qtw6pICdD86JKObwBdoV3cZjA";
        if(null!=openId && !"".equals(openId)){
            openId = openId;
        }else{
            WxUser wxUser = loginHelper.getWxUser();
            openId = wxUser.getUsername();
        }

        logger.info("进入邀请同行的接口");
        WxJsapiSignature jsapiSignature = null;
        String url = HttpServletUtil.getCompleteUrl(request);
        logger.info("url:"+url);
        try {
            jsapiSignature = wxService.createJsapiSignature(url);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        logger.info("appid:"+jsapiSignature.getAppid()+",noncestr:"+jsapiSignature.getNoncestr()
                +",timestamp:"+jsapiSignature.getTimestamp()+",signature:"+jsapiSignature.getSignature());

        WxMpUser userWxInfo = new WxMpUser();
        try {
            userWxInfo = wxService.getUserService().userInfo(openId);
        } catch (WxErrorException e) {
            logger.info("获取微信信息失败：" + e.getMessage());
        }
        //获取当前用户的邀请二维码
        String ticket = weChatInfoService.findAskTicket(openId);
        model.addAttribute("ticket", "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket);
        model.addAttribute("openId", openId);
        model.addAttribute("HeadImgUrl", userWxInfo.getHeadImgUrl());
        model.addAttribute("jsapiSignature", jsapiSignature);
        return "guide/invitation";
    }


}
