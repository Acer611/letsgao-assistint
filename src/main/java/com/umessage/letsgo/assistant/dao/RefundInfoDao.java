package com.umessage.letsgo.assistant.dao;

import com.umessage.letsgo.assistant.dao.provider.RefundInfoSqlProvider;
import com.umessage.letsgo.assistant.model.po.RefundInfoEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

/**
 * 退款记录Dao
 * Created by gaofei on 2016/12/30.
 */
@Mapper
public interface RefundInfoDao {
    @Insert("insert into refund_info(openid,order_id,refund_no,refund_id,create_time,fee,too_fee,cash_fee) " +
            "values(#{refundInfoEntity.openid},#{refundInfoEntity.orderId},#{refundInfoEntity.refundNo},#{refundInfoEntity.refundId},#{refundInfoEntity.createTime},#{refundInfoEntity.fee},#{refundInfoEntity.tooFee},#{refundInfoEntity.cashFee})")
    @SelectKey(before=false,keyProperty="refundInfoEntity.id",resultType=Long.class,statementType= StatementType.STATEMENT,statement="SELECT LAST_INSERT_ID() AS id")
    public void insert(@Param("refundInfoEntity") RefundInfoEntity refundInfoEntity);


    @SelectProvider(type=RefundInfoSqlProvider.class, method="findByIdSql")
    @Results(value ={
            @Result(id=true, property="id",column="ID"),
            @Result(property="openid",column="openid"),
            @Result(property="orderId",column="order_id"),
            @Result(property="refundNo",column="refund_no"),
            @Result(property="refundId",column="refund_id"),
            @Result(property="createTime",column="create_time"),
            @Result(property="fee",column="fee"),
            @Result(property="tooFee",column="too_fee"),
            @Result(property="cashFee",column="cash_fee")
    })
    public RefundInfoEntity select(@Param("id") Long id);


    @SelectProvider(type=RefundInfoSqlProvider.class, method="updateSql")
    public void update(@Param("refundInfoEntity") RefundInfoEntity refundInfoEntity);


    @Delete("delete from refund_info where id = #{id}")
    public void delete(@Param("id") Long id);


    @SelectProvider(type=RefundInfoSqlProvider.class, method="selectByOrderIdSql")
    @Results(value ={
            @Result(id=true, property="id",column="ID"),
            @Result(property="openid",column="openid"),
            @Result(property="orderId",column="order_id"),
            @Result(property="refundNo",column="refund_no"),
            @Result(property="refundId",column="refund_id"),
            @Result(property="createTime",column="create_time"),
            @Result(property="fee",column="fee"),
            @Result(property="tooFee",column="too_fee"),
            @Result(property="cashFee",column="cash_fee")
    })
    RefundInfoEntity selectByOrderId(@Param("orderId") String orderId);
}
