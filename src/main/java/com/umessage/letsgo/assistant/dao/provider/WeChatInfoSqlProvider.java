package com.umessage.letsgo.assistant.dao.provider;

import com.umessage.letsgo.assistant.model.po.WeChatInfoEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by lizhen on 2016/12/16.
 */
public class WeChatInfoSqlProvider {

    public String updateByOpenIdSql(WeChatInfoEntity wechatInfo) {
        return new SQL(){
            {
                UPDATE("wechat_info");
                if(wechatInfo.getID() != null){
                    SET("ID=#{wechatInfo.ID}");
                }
                if(!StringUtils.isEmpty(wechatInfo.getParentid())){
                    SET("parentid=#{wechatInfo.parentid}");
                }
                if(!StringUtils.isEmpty(wechatInfo.getUnionid())){
                    SET("unionid=#{wechatInfo.unionid}");
                }
                if(!StringUtils.isEmpty(wechatInfo.getNickname())){
                    SET("nickname=#{wechatInfo.nickname}");
                }
                if(!StringUtils.isEmpty(wechatInfo.getSex())){
                    SET("sex=#{wechatInfo.sex}");
                }
                if(!StringUtils.isEmpty(wechatInfo.getProvince())){
                    SET("province=#{wechatInfo.province}");
                }
                if(!StringUtils.isEmpty(wechatInfo.getCity())){
                    SET("city=#{wechatInfo.city}");
                }
                if(!StringUtils.isEmpty(wechatInfo.getCountry())){
                    SET("country=#{wechatInfo.country}");
                }
                if(!StringUtils.isEmpty(wechatInfo.getHeadImgUrl())){
                    SET("headImgUrl=#{wechatInfo.headImgUrl}");
                }
                if(wechatInfo.getStatus() != null){
                    SET("status=#{wechatInfo.status}");
                }
                if(!StringUtils.isEmpty(wechatInfo.getTicket())){
                    SET("ticket=#{wechatInfo.ticket}");
                }
                if(!StringUtils.isEmpty(wechatInfo.getTicketUrl())){
                    SET("ticket_url=#{wechatInfo.ticketUrl}");
                }
                if(wechatInfo.getIsGuide() != null){
                    SET("is_guide=#{wechatInfo.isGuide}");
                }
                if(wechatInfo.getReward() != null){
                    SET("reward=#{wechatInfo.reward}");
                }
                WHERE("openid=#{wechatInfo.openid}");
            }
        }.toString();
    }

}
