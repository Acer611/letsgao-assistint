package com.umessage.letsgo.assistant.wechat.handler;

import com.umessage.letsgo.assistant.service.IWeChatInfoService;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by gaofei on 2016/12/16.
 */

@Component
public class ViewHandler  extends AbstractHandler{
    @Resource
    private IWeChatInfoService wechatInfoService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        WxMpXmlOutTextMessage m = new WxMpXmlOutTextMessage();

        this.logger.info("VIEW事件: " + wxMpXmlMessage.getFromUser());
        String openId = wxMpXmlMessage.getFromUser();

        return m;
    }
}
