package com.umessage.letsgo.assistant.dao;

import com.umessage.letsgo.assistant.dao.provider.PayInfoSqlProvider;
import com.umessage.letsgo.assistant.model.po.PayInfoEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

/**
 * 支付记录Dao
 * Created by gaofei on 2016/12/30.
 */
@Mapper
public interface PayInfoDao {
    /**
     * 创建支付记录
     * @param payInfoEntity
     */
    @Insert("insert into pay_info(openid,order_id,transaction_id,fee,status,create_time) " +
            "values(#{payInfoEntity.openId},#{payInfoEntity.orderId},#{payInfoEntity.transactionId},#{payInfoEntity.fee},#{payInfoEntity.status},#{payInfoEntity.createTime})")
    @SelectKey(before=false,keyProperty="payInfoEntity.id",resultType=Long.class,statementType= StatementType.STATEMENT,statement="SELECT LAST_INSERT_ID() AS id")
    public void insert(@Param("payInfoEntity") PayInfoEntity payInfoEntity);


    /**
     * 根据Id 查询支付记录信息
     * @param id
     * @return
     */
    @SelectProvider(type=PayInfoSqlProvider.class, method="findByIdSql")
    @Results(value ={
            @Result(id=true, property="id",column="ID"),
            @Result(property="openId",column="openid"),
            @Result(property="orderId",column="order_id"),
            @Result(property="transactionId",column="transaction_id"),
            @Result(property="fee",column="fee"),
            @Result(property="createTime",column="create_time")
    })
    public PayInfoEntity select(@Param("id") Long id);


    /**
     * 根据orderId 查询支付信息
     * @param orderId
     * @return
     */
    @SelectProvider(type=PayInfoSqlProvider.class, method="findByOrderIdSql")
    @Results(value ={
            @Result(id=true, property="id",column="ID"),
            @Result(property="openId",column="openid"),
            @Result(property="orderId",column="order_id"),
            @Result(property="transactionId",column="transaction_id"),
            @Result(property="fee",column="fee"),
            @Result(property="createTime",column="create_time")
    })
    public PayInfoEntity selectByOrderId(@Param("orderId") String orderId);

    /**
     * 根据Id 修改支付记录信息
     * @param payInfoEntity
     */
    @SelectProvider(type=PayInfoSqlProvider.class, method="updateSql")
    public void update(@Param("payInfoEntity") PayInfoEntity payInfoEntity);


    /**
     * 根据OrderId 修改支付记录信息
     * @param payInfoEntity
     */
    @SelectProvider(type=PayInfoSqlProvider.class, method="updateByOrderIdSql")
    public void updateByOrderId(@Param("payInfoEntity") PayInfoEntity payInfoEntity);

    @Delete("delete from pay_info where id = #{id}")
    public void delete(@Param("id") Long id);
}
