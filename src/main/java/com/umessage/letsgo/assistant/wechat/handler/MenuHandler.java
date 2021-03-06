package com.umessage.letsgo.assistant.wechat.handler;

import com.umessage.letsgo.assistant.common.helper.UserLoginHelper;
import com.umessage.letsgo.assistant.common.utils.FileDownloadUtil;
import com.umessage.letsgo.assistant.common.utils.NewImageUtils;
import com.umessage.letsgo.assistant.model.po.WeChatInfoEntity;
import com.umessage.letsgo.assistant.service.IWeChatInfoService;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * @author gaofei
 */
@Component
public class MenuHandler extends AbstractHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IWeChatInfoService weChatInfoService;
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
        //旅游助手
        if("assistint".equals(eventKey)){
            //组装客服消息
            WxMpKefuMessage wxkefuMessage = WxMpKefuMessage.TEXT()
                    .content("在的亲，您有什么需求？ 直接点击键盘留言，助理24小时在线为您服务☺。" )
                    .toUser(wxMessage.getFromUser())
                    .build();
            //发送客服消息
            weixinService.getKefuService().sendKefuMessage(wxkefuMessage);

            return WxMpXmlOutMessage.TEXT()
                    .content("在的亲，您有什么需求？ 直接点击键盘留言，助理24小时在线为您服务☺。" )
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser())
                    .build();

        }

        //二维码
        if("ticket".equals(eventKey)){
            //获取当前用户openID
            this.logger.info("获取openID： "+openId);
            WeChatInfoEntity weChatInfoEntity = weChatInfoService.findWecahtUserByOpenID(openId);
            //获取微信二维码ticket
            WxMpQrCodeTicket wxMpQrCodeTicket = this.getWxMpQrCodeTicket(weChatInfoEntity, openId, weixinService);
            if (wxMpQrCodeTicket != null){
                //获取二维码
                File file = weixinService.getQrcodeService().qrCodePicture(wxMpQrCodeTicket);
                file = this.changePic(openId, weixinService, file);
                //新增永久素材
                WxMpMaterial wxMaterial = new WxMpMaterial();
                wxMaterial.setFile(file);
                wxMaterial.setName("二维码");
                WxMpMaterialUploadResult res = weixinService.getMaterialService().materialFileUpload("thumb", wxMaterial);
                String mediaId = res.getMediaId();
                //被动回复用户消息，回复图片消息
                this.logger.info("发送图片消息" );
                if (!StringUtils.isEmpty(mediaId)){
                    //组装客服消息
                    WxMpKefuMessage wxkefuMessage = WxMpKefuMessage.IMAGE()
                            .toUser(wxMessage.getFromUser())
                            .mediaId(mediaId)
                            .build();
                    //发送客服消息
                    weixinService.getKefuService().sendKefuMessage(wxkefuMessage);

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


    public File changePic(String openId, WxMpService weixinService, File fileZipQrCode){
        File file = null;
        try {
            file = File.createTempFile("weixinbg", ".jpg");
            //系统路径
            String systemPath = System.getProperty("java.io.tmpdir");
            //读取背景图
            ClassPathResource classPathResource = new ClassPathResource("/static/images/myticketbg.jpg");
            InputStream inputStream = classPathResource.getInputStream();
            //生成图片临时文件
            FileUtils.copyInputStreamToFile(inputStream, file);

            //添加二维码
            //压缩图片大小
            NewImageUtils.ImgCompress(fileZipQrCode, 250, 250, 0.7f);
            NewImageUtils.watermark(file, fileZipQrCode, 250, 780, 1.0f);

            // 添加微信头像
            WxMpUser wxMpUser = weixinService.getUserService().userInfo(openId, null);

            //添加文字
            String text = "我是"+wxMpUser.getNickname();
            //NewImageUtils.pressText(text,saveFilePath,"黑体", Font.TRUETYPE_FONT,0,25,395,1170);
            NewImageUtils.waterMarkByText(text,file,330,125,30,0.8f);

            String headImgUrl = wxMpUser.getHeadImgUrl();
            String saveWxPath = "F://wxhead.jpg";
            File headImgFile = FileDownloadUtil.downloadNet(headImgUrl);

            //压缩图片大小
            //String zipWxhead = NewImageUtils.zipImageFile(saveWxPath, 65, 65, 1f, "x2");
            NewImageUtils.ImgCompress(headImgFile, 65, 65, 0.7f);
            NewImageUtils.watermark(file, headImgFile, 570, 130, 1.0f);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return file;
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
