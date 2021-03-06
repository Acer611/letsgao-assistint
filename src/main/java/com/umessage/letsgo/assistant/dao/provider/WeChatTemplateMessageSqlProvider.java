package com.umessage.letsgo.assistant.dao.provider;

import com.umessage.letsgo.assistant.model.po.WeChatTemplateMessageEntity;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by ZhaoYidong on 2016/12/20.
 */
public class WeChatTemplateMessageSqlProvider {
    public String insertSql(WeChatTemplateMessageEntity templateMessageEntity) {
        return new SQL(){
            {
                INSERT_INTO("wechat_template_message");
                VALUES("openid","#{templateMessageEntity.openid}");
                VALUES("template_id","#{templateMessageEntity.templateId}");
                VALUES("template_data","#{templateMessageEntity.templateData}");
                VALUES("create_time","#{templateMessageEntity.createTime}");
            }
        }.toString();
    }



    public String selectSql(Long id) {
        return new SQL() {{
            SELECT("ID, openid, template_id, template_data, create_time");
            FROM("wechat_template_message");
            if (id != null) {
                WHERE("ID = "+id);
            }
        }}.toString();
    }
}
