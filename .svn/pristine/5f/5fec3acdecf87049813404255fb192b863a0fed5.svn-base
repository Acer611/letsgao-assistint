package com.umessage.letsgo.assistant.service;

import com.umessage.letsgo.assistant.model.po.WeChatOrderInfoEntity;
import org.springframework.stereotype.Service;

/**
 * Created by ZhaoYidong on 2016/12/20.
 */
@Service
public interface IWeChatSendMessageService {

    void sendPayUrlTemplateMessage(String openid, WeChatOrderInfoEntity orderInfoEntity);

    void sendPaySuccessTemplateMessage(String openid, WeChatOrderInfoEntity orderInfoEntity);

    void sendOrderStatesEndTemplateMessage(String openid, WeChatOrderInfoEntity orderInfoEntity);
}
