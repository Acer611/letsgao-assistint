package com.umessage.letsgo.assistant.wechat.handler;

import com.umessage.letsgo.assistant.model.po.WeChatInfoEntity;
import com.umessage.letsgo.assistant.service.IWeChatInfoService;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @author Binary Wang
 */
@Component
public class SubscribeHandler extends AbstractHandler {

    @Resource
    private IWeChatInfoService wechatInfoService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {

        this.logger.info("新关注用户 OPENID: " + wxMessage.getFromUser());
        WxMpUser userWxInfo = weixinService.getUserService()
                .userInfo(wxMessage.getFromUser(), null);
        WxMpXmlOutMessage responseResult = null;
        try {
            responseResult = handleSpecial(wxMessage,weixinService,responseResult);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        if (responseResult != null) {
            return responseResult;
        }

        return responseResult;
    }

    /**
     *
     */
    public WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage,WxMpService weixinService,WxMpXmlOutMessage responseResult)
            throws Exception {

        logger.info("获取用户基本信息");
        // 获取微信用户基本信息
        WxMpUser userWxInfo = weixinService.getUserService()
                .userInfo(wxMessage.getFromUser(), null);

        String pid = "0";
        if(!"" .equals(wxMessage.getEventKey()) || null != wxMessage.getTicket()){
            pid = wxMessage.getEventKey();
            if(pid.contains("_")){
                pid = pid.substring(pid.indexOf("_") + 1,pid.length());
            }
        }

        logger.info("获取当前公众号的所有标签");
        List<WxUserTag> userTagList = weixinService.getUserTagService().tagGet();
        long guidTag = 0L;
        long customerTag = 0L;
        for (WxUserTag userTag: userTagList) {
            if(userTag.getName().equals("导游")){
                guidTag = userTag.getId();
            }
            if(userTag.getName().equals("用户")){
                customerTag = userTag.getId();
            }
        }
        logger.info("当前用户的openId 为 " + userWxInfo.getOpenId() );
        if (userWxInfo != null) {
            //检查用户是否存在
            WeChatInfoEntity wechatInfo = wechatInfoService.findWecahtUserByOpenID(userWxInfo.getOpenId());
            if(null == wechatInfo){
                logger.info("当前用户pid是：" + pid);
                logger.info("新关注用户信息录入");
                //新关注用户
                wechatInfoService.createWechatuser(userWxInfo,pid);

            }else{
                logger.info("重新关注用户状态修改");
                wechatInfo.setOpenid(userWxInfo.getOpenId());
                wechatInfo.setCity(userWxInfo.getCity());
                wechatInfo.setSex(userWxInfo.getSex());
                wechatInfo.setHeadImgUrl(userWxInfo.getHeadImgUrl());
                wechatInfo.setCountry(userWxInfo.getCountry());
                wechatInfo.setUnionid(userWxInfo.getUnionId());
                String nickName = null;
                try{
                    nickName = URLEncoder.encode(userWxInfo.getNickname(), "utf-8");
                }catch (Exception e){
                    e.printStackTrace();
                }
                wechatInfo.setNickname(nickName);
                wechatInfo.setProvince(userWxInfo.getProvince());
                wechatInfo.setStatus(0);
                logger.info("当前用户pid是：" + pid);
                if(!"0".equals(pid) ){
                    int iPid = Integer.parseInt(pid);
                    if(iPid<=100){
                        wechatInfo.setIsGuide(0);
                    }else{
                        wechatInfo.setIsGuide(1);
                    }
                    wechatInfo.setParentid(pid);
                }else{
                    wechatInfo.setParentid("0");
                    wechatInfo.setIsGuide(1);
                }
                //重新关注用户
                wechatInfoService.updateStatus(wechatInfo,0,userWxInfo.getOpenId());
            }
            // 给用户打标签
            if(!"0".equals(pid)){
                int iPid = Integer.parseInt(pid);
                if(iPid<=100){
                    logger.info("给导游角色的用户打标签");
                    //打导游标签
                    String [] openIdArray = new  String[1];
                    if(0L != guidTag){
                        openIdArray [0] = userWxInfo.getOpenId();
                        weixinService.getUserTagService().batchTagging(guidTag,openIdArray);
                    }
               /*     responseResult = WxMpXmlOutMessage.TEXT()
                            .content(userWxInfo.getNickname() + "，欢迎进入“跟上旅游助理  代理人”端后台。\n\n" +
                                    " 想知道“旅游助理”如何帮你赚收益吗？\n" +
                                    "想知道如何成为“旅游助理”代理人吗？\n\n"+
                                    "花2分钟时间，认真阅读下文▼▽▼\n\n"+
                                    "<a href=\"http://mp.weixin.qq.com/s?__biz=MzIzMjcwNDgzNg==&mid=2247483656&idx=1&sn=178e4740dac4b596ac2214713f8f6cd3&chksm=e8919f23dfe61635b958bdaf8ea881c76e5d302fa8aaf3bae4897a72b6547d7613cbcfe646b3&mpshare=1&scene=23&srcid=1229zFrzsqlZLYDYeZD0GrMK#rd\">《一个帮旅游行业从业者赚外快的公众号》</a>\n" +
                                    "   \n" +
                                    "（如有任何疑问，均可点击键盘留言咨询）\n\n" +
                                    "首次关注，“个人中心”栏目中内容缓存迟缓，请耐心等待5分钟……" )
                            .fromUser(wxMessage.getToUser())
                            .toUser(wxMessage.getFromUser())
                            .build();*/

                    responseResult = WxMpXmlOutMessage.TEXT()
                            .content( "\n\n" +
                                    " 呀呀呀！厉害呀！这么低调一地儿都被你发现了。\n\n\n" +
                                    "瞅瞅有啥宝贝：\n"+
                                    "<a href=\"http://mp.weixin.qq.com/s?__biz=MzIzMjcwNDgzNg==&mid=2247483656&idx=1&sn=178e4740dac4b596ac2214713f8f6cd3&chksm=e8919f23dfe61635b958bdaf8ea881c76e5d302fa8aaf3bae4897a72b6547d7613cbcfe646b3&mpshare=1&scene=23&srcid=1229zFrzsqlZLYDYeZD0GrMK#rd\">《一个帮旅游行业从业者赚外快的公众号》</a>\n\n\n" +
                                    "\uD83D\uDC47     \n" +
                                    "<a href=\"https://zhuli.igenshang.com/wechat/findWeChatInfo\">您的专属二维码</a>" +
                                    "\n\n" +
                                    "  " )
                            .fromUser(wxMessage.getToUser())
                            .toUser(wxMessage.getFromUser())
                            .build();

                }else{
                    responseResult = WxMpXmlOutMessage.TEXT()
                            .content("\n\n" +
                                    " 阿尼阿塞哟~\n" +
                                    "我是出境旅游小助.\n\n\n" +
                                    "\uD83D\uDC47     \n"  +
                                    "√出境机票\n" +
                                    "√出境签证\n" +
                                    "√境外酒店\n" +
                                    "√境外保险\n" +
                                    "…………\n\n\n" +
                                    "我都可以搞定的~\n" +
                                    "点开键盘留言吧~"+
                                    "\n\n" +
                                    "  " )
                            .fromUser(wxMessage.getToUser())
                            .toUser(wxMessage.getFromUser())
                            .build();
                }

            }else{
                responseResult = WxMpXmlOutMessage.TEXT()
                        .content("\n\n" +
                                " 阿尼阿塞哟~\n" +
                                "我是出境旅游小助.\n\n\n" +
                                "\uD83D\uDC47     \n"  +
                                "√出境机票\n" +
                                "√出境签证\n" +
                                "√境外酒店\n" +
                                "√境外保险\n" +
                                "…………\n\n\n" +
                                "我都可以搞定的~\n" +
                                "点开键盘留言吧~"+
                                "\n\n" +
                                "  " )
                        .fromUser(wxMessage.getToUser())
                        .toUser(wxMessage.getFromUser())
                        .build();
            }


        }

        return responseResult;
    }

}
