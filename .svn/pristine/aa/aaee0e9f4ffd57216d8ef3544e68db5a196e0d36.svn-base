package com.umessage.letsgo.assistant.wechat.handler;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 
 * @author Binary Wang
 *
 */
@Component
public abstract class ScanHandler extends AbstractHandler {

    @Resource
   private  SubscribeHandler subscribeHandler;


    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) throws WxErrorException {
        String openId = wxMessage.getFromUser();
        this.logger.info("扫码用户 OPENID: " + wxMessage.getFromUser());

        try {
            subscribeHandler.handleSpecial(wxMessage,wxMpService,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
