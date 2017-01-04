package com.umessage.letsgo.assistant.wechat.handler;

import com.umessage.letsgo.assistant.service.IWeChatInfoService;
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
public class NullHandler extends AbstractHandler {

    @Resource
    private IWeChatInfoService weChatInfoService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
            Map<String, Object> context, WxMpService wxMpService,
            WxSessionManager sessionManager) throws WxErrorException {
        String openId = wxMessage.getFromUser();
        String eventKey = wxMessage.getEventKey();
      /*  //二维码
        if("ticket".equals(eventKey)){
            WeChatInfoEntity weChatInfo =  weChatInfoService.findWecahtUserByOpenID(openId);
            //根据用户的id 生成永久的二维码
            WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateLastTicket(String.valueOf(weChatInfo.getID()));
            String ticketUrl = wxMpQrCodeTicket.getUrl();
            weChatInfoService.updateTickURLByOpenID(ticketUrl,openId);
        }

        //导游查看收益
        if("order".equals(eventKey)){

        }

        //邀请同行
        if("ask".equals(eventKey)){

        }
        //用户查看订单列表
        if("c_order".equals(eventKey)){

        }

*/
        return null;
    }

}
