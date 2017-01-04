package com.umessage.letsgo.assistant.dao.provider;

import com.umessage.letsgo.assistant.model.po.PayInfoEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by lizhen on 2016/12/16.
 */
public class PayInfoSqlProvider {

    /**
     * 根据id 查询支付记录
     * @param id
     * @return
     */
    public String findByIdSql(Long id) {
        return new SQL() {{
            SELECT("ID, openid,order_id,transaction_id,fee,create_time");
            FROM("pay_info");
            if (id != null) {
                WHERE("id = "+id);
            }
        }}.toString();
    }

    /**
     * 根据openID 获取支付记录
     * @param orderId
     * @return
     */
    public String findByOrderIdSql(String orderId) {
        return new SQL() {{
            SELECT("ID, openid,order_id,transaction_id,fee,create_time");
            FROM("pay_info");
            if (orderId != null) {
                WHERE("order_id = " + orderId);
            }
        }}.toString();
    }


    /**
     * 修改改支付记录根据ID
     * @param payInfoEntity
     * @return
     */
    public String updateSql(PayInfoEntity payInfoEntity) {
        return new SQL(){
            {
                UPDATE("pay_info");
                if(!StringUtils.isEmpty(payInfoEntity.getOpenId())){
                    SET("openid=#{payInfoEntity.openId}");
                }
                if(!StringUtils.isEmpty(payInfoEntity.getOrderId())){
                    SET("order_id=#{payInfoEntity.orderId}");
                }
                if(!StringUtils.isEmpty(payInfoEntity.getTransactionId())){
                    SET("transaction_id=#{payInfoEntity.transactionId}");
                }
                if(payInfoEntity.getFee() != null){
                    SET("fee=#{payInfoEntity.fee}");
                }
                if(payInfoEntity.getCreateTime() != null){
                    SET("create_time=#{payInfoEntity.createTime}");
                }
                WHERE("id=#{payInfoEntity.id}");
            }
        }.toString();
    }


    /**
     * 修改改支付记录根据OrderId
     * @param payInfoEntity
     * @return
     */
    public String updateByOrderIdSql(PayInfoEntity payInfoEntity) {
        return new SQL(){
            {
                UPDATE("pay_info");
                if(!StringUtils.isEmpty(payInfoEntity.getOpenId())){
                    SET("openid=#{payInfoEntity.openId}");
                }
                if(!StringUtils.isEmpty(payInfoEntity.getTransactionId())){
                    SET("transaction_id=#{payInfoEntity.transactionId}");
                }
                if(payInfoEntity.getFee() != null){
                    SET("fee=#{payInfoEntity.fee}");
                }
                if(payInfoEntity.getCreateTime() != null){
                    SET("create_time=#{payInfoEntity.createTime}");
                }
                if(payInfoEntity.getStatus() != null){
                    SET("status=#{payInfoEntity.status}");
                }

                WHERE("order_id=#{payInfoEntity.orderId}");
            }
        }.toString();
    }

}
