package com.umessage.letsgo.assistant.service;

import com.umessage.letsgo.assistant.model.po.WeChatTemplateMessageEntity;
import org.springframework.stereotype.Service;

/**
 * Created by ZhaoYidong on 2016/12/20.
 */
@Service
public interface IWeChatTemplateMessageService {

    void insert(WeChatTemplateMessageEntity templateMessageEntity);

    WeChatTemplateMessageEntity select(Long id);
}
