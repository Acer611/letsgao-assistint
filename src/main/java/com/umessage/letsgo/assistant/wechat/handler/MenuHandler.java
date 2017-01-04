package com.umessage.letsgo.assistant.wechat.handler;

import com.umessage.letsgo.assistant.common.helper.UserLoginHelper;
import com.umessage.letsgo.assistant.model.po.WeChatInfoEntity;
import com.umessage.letsgo.assistant.service.IWeChatInfoService;
import com.umessage.letsgo.assistant.wechat.security.WxUser;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.Map;

/**
 * @author Binary Wang
 */
@Component
public class MenuHandler extends AbstractHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IWeChatInfoService weChatInfoService;
   /* @Autowired
    private IWeChatSendMessageService weChatSendMessageService;*/
    @Autowired
    private UserLoginHelper loginHelper;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
            Map<String, Object> context, WxMpService weixinService,
            WxSessionManager sessionManager)  throws WxErrorException {
        this.logger.info("二级菜单点击事件: " + wxMessage.getFromUser());
        String msg = String.format("type:%s, event:%s, key:%s",
            wxMessage.getMsgType(), wxMessage.getEvent(),
            wxMessage.getEventKey());

        String openId = wxMessage.getFromUser();
        String eventKey = wxMessage.getEventKey();
        WxMpUser userWxInfo = weixinService.getUserService()
                .userInfo(wxMessage.getFromUser(), null);
        //二维码
        if("assistint".equals(eventKey)){
            return WxMpXmlOutMessage.TEXT()
                    .content("在的亲，您有什么需求？ 直接点击键盘留言，助理24小时在线为您服务☺。" )
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser())
                    .build();


            //通送二维码信息
           /*/weChatSendMessageService.sendQrCodeTemplateMessage(openId);*/
        }

        if("ticket".equals(eventKey)){
            //获取当前用户openID
            this.logger.info("获取openID： "+openId);
            WeChatInfoEntity weChatInfoEntity = weChatInfoService.findWecahtUserByOpenID(openId);
            //获取微信二维码ticket
            WxMpQrCodeTicket wxMpQrCodeTicket = this.getWxMpQrCodeTicket(weChatInfoEntity, openId, weixinService);
            if (wxMpQrCodeTicket != null){
                //获取二维码
                File file = weixinService.getQrcodeService().qrCodePicture(wxMpQrCodeTicket);
                //新增永久素材
                WxMpMaterial wxMaterial = new WxMpMaterial();
                wxMaterial.setFile(file);
                wxMaterial.setName("二维码");
                WxMpMaterialUploadResult res = weixinService.getMaterialService().materialFileUpload("thumb", wxMaterial);
                String mediaId = res.getMediaId();
                //被动回复用户消息，回复图片消息
                this.logger.info("发送图片消息" );
                if (!StringUtils.isEmpty(mediaId)){
                    return WxMpXmlOutMessage.IMAGE()
                            .mediaId(mediaId)
                            .fromUser(wxMessage.getToUser())
                            .toUser(wxMessage.getFromUser())
                            .build();
                }
            }
        }


        return WxMpXmlOutMessage.TEXT().content(msg)
                .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                .build();
    }



    public WxMpQrCodeTicket getWxMpQrCodeTicket(WeChatInfoEntity weChatInfoEntity, String openId, WxMpService wxMpService) throws WxErrorException {
        if (weChatInfoEntity == null){
            this.logger.info("微信信息weChatInfoEntity为空，无法生成永久的二维码！");
            return  new WxMpQrCodeTicket();
        }
        String ticket = weChatInfoEntity.getTicket();
        String ticketUrl = weChatInfoEntity.getTicketUrl();
        this.logger.info("生成永久的二维码");
        WxMpQrCodeTicket wxMpQrCodeTicket = new WxMpQrCodeTicket();
        if (StringUtils.isEmpty(ticket)) {
            //根据用户的id 生成永久的二维码
            wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateLastTicket(String.valueOf(weChatInfoEntity.getID()));
            if (null != wxMpQrCodeTicket) {
                ticketUrl = wxMpQrCodeTicket.getUrl();
                ticket = wxMpQrCodeTicket.getTicket();
                this.logger.info("更新微信信息表中ticket: "+ticket+"和ticket_url:"+ticketUrl);
                weChatInfoService.updateTickURLByOpenID(ticketUrl, ticket, openId);
            }
        } else {
            wxMpQrCodeTicket.setTicket(ticket);
            wxMpQrCodeTicket.setUrl(ticketUrl);
        }
        return  wxMpQrCodeTicket;
    }

}
