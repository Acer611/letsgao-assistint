package com.umessage.letsgo.assistant.dao;

import com.umessage.letsgo.assistant.model.po.WeChatTemplateMessageEntity;
import com.umessage.letsgo.assistant.dao.provider.WeChatTemplateMessageSqlProvider;
import org.apache.ibatis.annotations.*;

/**
 * Created by ZhaoYidong on 2016/12/20.
 */
@Mapper
public interface WeChatTemplateMessageDao {

    @SelectProvider(type=WeChatTemplateMessageSqlProvider.class, method="insertSql")
    @Options(useGeneratedKeys = true, keyProperty = "templateMessageEntity.id")
    public void insert(@Param("templateMessageEntity") WeChatTemplateMessageEntity templateMessageEntity);



    @SelectProvider(type=WeChatTemplateMessageSqlProvider.class, method="selectSql")
    @Results(value ={
            @Result(id=true, property="id",column="ID"),
            @Result(property="openid",column="openid"),
            @Result(property="templateId",column="template_id"),
            @Result(property="templateData",column="template_data"),
            @Result(property="createTime",column="create_time")
    })
    public WeChatTemplateMessageEntity select(@Param("id") Long id);

}
