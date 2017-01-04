package com.umessage.letsgo.assistant.dao.provider;

import com.umessage.letsgo.assistant.model.po.RefundInfoEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by lizhen on 2016/12/16.
 */
public class RefundInfoSqlProvider {

    public String findByIdSql(Long id) {
        return new SQL() {{
            SELECT("ID,openid,order_id,refund_no,refund_id,create_time,fee,too_fee,cash_fee");
            FROM("refund_info");
            if (id != null) {
                WHERE("id = "+id);
            }
        }}.toString();
    }


    public String findByOrderIdSql(String orderId) {
        return new SQL() {{
            SELECT("ID,openid,order_id,refund_no,refund_id,create_time,fee,too_fee,cash_fee");
            FROM("refund_info");
            if (orderId != null) {
                WHERE("order_id = " + orderId);
            }
        }}.toString();
    }


    public String updateSql(RefundInfoEntity refundInfoEntity) {
        return new SQL(){
            {
                UPDATE("refund_info");
                if(!StringUtils.isEmpty(refundInfoEntity.getOpenid())){
                    SET("openid=#{refundInfoEntity.openid}");
                }
                if(!StringUtils.isEmpty(refundInfoEntity.getOrderId())){
                    SET("order_id=#{refundInfoEntity.orderId}");
                }
                if(!StringUtils.isEmpty(refundInfoEntity.getRefundNo())){
                    SET("refund_no=#{refundInfoEntity.refundNo}");
                }
                if(!StringUtils.isEmpty(refundInfoEntity.getRefundId())){
                    SET("refund_id=#{refundInfoEntity.refundId}");
                }
                if(refundInfoEntity.getCreateTime() != null){
                    SET("create_time=#{refundInfoEntity.createTime}");
                }
                if(refundInfoEntity.getFee() != null){
                    SET("fee=#{refundInfoEntity.fee}");
                }
                if(refundInfoEntity.getTooFee() != null){
                    SET("too_fee=#{refundInfoEntity.tooFee}");
                }
                if(refundInfoEntity.getCashFee() != null){
                    SET("cash_fee=#{refundInfoEntity.cashFee}");
                }
                WHERE("id=#{refundInfoEntity.id}");
            }
        }.toString();
    }

}
