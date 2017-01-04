package com.umessage.letsgo.assistant.service.impl;

import com.umessage.letsgo.assistant.model.po.WeChatTemplateMessageEntity;
import com.umessage.letsgo.assistant.service.ITemplateMessageService;
import com.umessage.letsgo.assistant.service.IWeChatTemplateMessageService;
import com.umessage.letsgo.assistant.model.vo.request.TemplateMessageRequest;
import com.umessage.letsgo.assistant.common.utils.JsonUtils;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * Created by Lizhen on 2016/11/21 0030.
 */
@Service
public class TemplateMessageServiceImpl implements ITemplateMessageService {
    private static final Logger logger = Logger.getLogger(TemplateMessageServiceImpl.class);
    // 模板消息字体颜色
    private static final String TEMPLATE_BLACK_COLOR = "#000000";
    @Autowired
    protected WxMpService wxMpService;
    @Autowired
    protected IWeChatTemplateMessageService weChatTemplateMessageService;

    @Value(value = "${wechat.ts.payurl}")
    private String payurl;

    @Value(value = "${wechat.ts.paysuccess}")
    private String paysuccess;

    @Value(value = "${wechat.ts.orderstates}")
    private String orderstates;


    /**
     * 推送支付链接
     * @param request
     */
    @Override
    public void sendPayUrlTemplate(TemplateMessageRequest request) {
        WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
        templateMessage.setToUser(request.getOpenid());//openid
        templateMessage.setTemplateId(payurl);
        templateMessage.setUrl(request.getUrl());
        templateMessage = this.getTemplateMessageRequest(templateMessage, request);
        try {
            wxMpService.getTemplateMsgService()
                    .sendTemplateMsg(templateMessage);
            this.addWxTemplateMessage(request,payurl);
        } catch (WxErrorException e) {
            logger.error(e.getMessage().toString());
        }
    }


    /**
     * 推送支付完成推送支付成功消息
     * @param request
     */
    @Override
    public void sendPaySuccessTemplate(TemplateMessageRequest request) {
        WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
        templateMessage.setToUser(request.getOpenid());//openid
        templateMessage.setTemplateId(paysuccess);
        templateMessage.setUrl(request.getUrl());
        templateMessage = this.getTemplateMessageRequest(templateMessage, request);
        try {
            wxMpService.getTemplateMsgService()
                    .sendTemplateMsg(templateMessage);
            this.addWxTemplateMessage(request,paysuccess);
        } catch (WxErrorException e) {
            logger.error(e.getMessage().toString());
        }
    }


    /**
     * 推送订单完成消息
     * @param request
     */
    @Override
    public void sendOrderStatesEndTemplate(TemplateMessageRequest request) {
        WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
        templateMessage.setToUser(request.getOpenid());//openid
        templateMessage.setTemplateId(orderstates);
        templateMessage.setUrl(request.getUrl());
        templateMessage = this.getOrderStatesTemplateMessageRequest(templateMessage, request);
        try {
            wxMpService.getTemplateMsgService()
                    .sendTemplateMsg(templateMessage);
            this.addWxTemplateMessage(request,orderstates);
        } catch (WxErrorException e) {
            logger.error(e.getMessage().toString());
        }
    }


    /**
     * 根据参数获取模板消息
     * @param request
     * @return
     */
    public WxMpTemplateMessage getTemplateMessageOneRequest(WxMpTemplateMessage templateMessage, TemplateMessageRequest request){
        WxMpTemplateData firstData = new WxMpTemplateData("first", request.getData().get("first").getValue(), TEMPLATE_BLACK_COLOR);
        WxMpTemplateData remarkData = new WxMpTemplateData("remark", request.getData().get("remark").getValue(), TEMPLATE_BLACK_COLOR);
        templateMessage.addWxMpTemplateData(firstData);
        templateMessage.addWxMpTemplateData(remarkData);
        return templateMessage;
    }


    /**
     * 根据参数获取模板消息
     * @param request
     * @return
     */
    public WxMpTemplateMessage getTemplateMessageRequest(WxMpTemplateMessage templateMessage, TemplateMessageRequest request){
        WxMpTemplateData firstData = new WxMpTemplateData("first", request.getData().get("first").getValue(), TEMPLATE_BLACK_COLOR);
        WxMpTemplateData keyword1Data = new WxMpTemplateData("keyword1", request.getData().get("keyword1").getValue(), TEMPLATE_BLACK_COLOR);
        WxMpTemplateData keyword2Data = new WxMpTemplateData("keyword2", request.getData().get("keyword2").getValue(), TEMPLATE_BLACK_COLOR);
        WxMpTemplateData remarkData = new WxMpTemplateData("remark", request.getData().get("remark").getValue(), TEMPLATE_BLACK_COLOR);
        templateMessage.addWxMpTemplateData(firstData);
        templateMessage.addWxMpTemplateData(keyword1Data);
        templateMessage.addWxMpTemplateData(keyword2Data);
        templateMessage.addWxMpTemplateData(remarkData);
        return templateMessage;
    }


    /**
     * 根据参数获取模板消息
     * @param request
     * @return
     */
    public WxMpTemplateMessage getTemplateMessageMoreRequest(WxMpTemplateMessage templateMessage, TemplateMessageRequest request){
        WxMpTemplateData firstData = new WxMpTemplateData("first", request.getData().get("first").getValue(), TEMPLATE_BLACK_COLOR);
        WxMpTemplateData keyword1Data = new WxMpTemplateData("keyword1", request.getData().get("keyword1").getValue(), TEMPLATE_BLACK_COLOR);
        WxMpTemplateData keyword2Data = new WxMpTemplateData("keyword2", request.getData().get("keyword2").getValue(), TEMPLATE_BLACK_COLOR);
        WxMpTemplateData keyword3Data = new WxMpTemplateData("keyword3", request.getData().get("keyword3").getValue(), TEMPLATE_BLACK_COLOR);
        WxMpTemplateData keyword4Data = new WxMpTemplateData("keyword4", request.getData().get("keyword4").getValue(), TEMPLATE_BLACK_COLOR);
        WxMpTemplateData remarkData = new WxMpTemplateData("remark", request.getData().get("remark").getValue(), TEMPLATE_BLACK_COLOR);
        templateMessage.addWxMpTemplateData(firstData);
        templateMessage.addWxMpTemplateData(keyword1Data);
        templateMessage.addWxMpTemplateData(keyword2Data);
        templateMessage.addWxMpTemplateData(keyword3Data);
        templateMessage.addWxMpTemplateData(keyword4Data);
        templateMessage.addWxMpTemplateData(remarkData);
        return templateMessage;
    }



    /**
     * 根据参数获取模板消息
     * @param request
     * @return
     */
    public WxMpTemplateMessage getOrderStatesTemplateMessageRequest(WxMpTemplateMessage templateMessage, TemplateMessageRequest request){
        WxMpTemplateData firstData = new WxMpTemplateData("first", request.getData().get("first").getValue(), TEMPLATE_BLACK_COLOR);
        WxMpTemplateData keyword1Data = new WxMpTemplateData("OrderSn", request.getData().get("OrderSn").getValue(), TEMPLATE_BLACK_COLOR);
        WxMpTemplateData keyword2Data = new WxMpTemplateData("OrderStatus", request.getData().get("OrderStatus").getValue(), TEMPLATE_BLACK_COLOR);
        WxMpTemplateData remarkData = new WxMpTemplateData("remark", request.getData().get("remark").getValue(), TEMPLATE_BLACK_COLOR);
        templateMessage.addWxMpTemplateData(firstData);
        templateMessage.addWxMpTemplateData(keyword1Data);
        templateMessage.addWxMpTemplateData(keyword2Data);
        templateMessage.addWxMpTemplateData(remarkData);
        return templateMessage;
    }


    /**
     * 添加模板消息记录
     * @param request
     */
    public void addWxTemplateMessage(TemplateMessageRequest request, String templateId){
        String jsonString = JsonUtils.toJson(request.getData());
        WeChatTemplateMessageEntity templateMessageEntity = new WeChatTemplateMessageEntity();
        templateMessageEntity.setOpenid(request.getOpenid());
        templateMessageEntity.setTemplateId(request.getTemplate_id());
        templateMessageEntity.setTemplateData(jsonString);
        templateMessageEntity.setTemplateId(templateId);
        weChatTemplateMessageService.insert(templateMessageEntity);
    }
}
