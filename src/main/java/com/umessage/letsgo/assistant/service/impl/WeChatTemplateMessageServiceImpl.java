package com.umessage.letsgo.assistant.service.impl;

import com.umessage.letsgo.assistant.dao.WeChatTemplateMessageDao;
import com.umessage.letsgo.assistant.model.po.WeChatTemplateMessageEntity;
import com.umessage.letsgo.assistant.service.IWeChatTemplateMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by ZhaoYidong on 2016/12/20.
 */
@Service
public class WeChatTemplateMessageServiceImpl implements IWeChatTemplateMessageService {

    @Autowired
    private WeChatTemplateMessageDao weChatTemplateMessageDao;

    @Override
    public void insert(WeChatTemplateMessageEntity templateMessageEntity) {
        templateMessageEntity.setCreateTime(new Date());
        weChatTemplateMessageDao.insert(templateMessageEntity);
    }

    @Override
    public WeChatTemplateMessageEntity select(Long id) {
        return weChatTemplateMessageDao.select(id);
    }
}
