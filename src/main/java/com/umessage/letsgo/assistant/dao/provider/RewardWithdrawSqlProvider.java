package com.umessage.letsgo.assistant.dao.provider;

import com.umessage.letsgo.assistant.model.po.RewardWithdrawEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by lizhen on 2016/12/16.
 */
public class RewardWithdrawSqlProvider {

    /**
     * 根据id 查询
     * @param id
     * @return
     */
    public String findByIdSql(Long id) {
        return new SQL() {{
            SELECT("ID,openid,partnet_trade_no,fee,wx_order_id,withdraw_time,`status`");
            FROM("reward_withdraw");
            if (id != null) {
                WHERE("id = "+id);
            }
        }}.toString();
    }

    /**
     * 根据openID 获取
     * @param openId
     * @return
     */
    public String findByOpenIdSql(String openId) {
        return new SQL() {{
            SELECT("ID,openid,partnet_trade_no,fee,wx_order_id,withdraw_time,`status`");
            FROM("reward_withdraw");
            if (openId != null) {
                WHERE("openid = " + openId);
            }
        }}.toString();
    }


    /**
     * 修改
     * @param rewardWithdrawEntity
     * @return
     */
    public String updateSql(RewardWithdrawEntity rewardWithdrawEntity) {
        return new SQL(){
            {
                UPDATE("reward_withdraw");
                if(!StringUtils.isEmpty(rewardWithdrawEntity.getOpenId())){
                    SET("openid=#{rewardWithdrawEntity.openId}");
                }
                if(!StringUtils.isEmpty(rewardWithdrawEntity.getPartentTradeNo())){
                    SET("partnet_trade_no=#{rewardWithdrawEntity.partentTradeNo}");
                }
                if(rewardWithdrawEntity.getFee() != null){
                    SET("fee=#{rewardWithdrawEntity.fee}");
                }
                if(!StringUtils.isEmpty(rewardWithdrawEntity.getwXOrderId())){
                    SET("wx_order_id=#{rewardWithdrawEntity.wXOrderId}");
                }
                if(rewardWithdrawEntity.getWithdrawTime() != null){
                    SET("withdraw_time=#{rewardWithdrawEntity.withdrawTime}");
                }
                if(rewardWithdrawEntity.getStatus() != null){
                    SET("`status`=#{rewardWithdrawEntity.status}");
                }
                WHERE("id=#{rewardWithdrawEntity.id}");
            }
        }.toString();
    }

}
