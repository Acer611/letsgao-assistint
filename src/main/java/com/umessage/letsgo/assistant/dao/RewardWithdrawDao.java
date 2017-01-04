package com.umessage.letsgo.assistant.dao;

import com.umessage.letsgo.assistant.dao.provider.RewardWithdrawSqlProvider;
import com.umessage.letsgo.assistant.model.po.RewardWithdrawEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * 收益提现记录Dao
 * Created by gaofei on 2016/12/30.
 */
@Mapper
public interface RewardWithdrawDao {
    /**
     * 创建
     * @param rewardWithdrawEntity
     */
    @Insert("insert into reward_withdraw(openid,partnet_trade_no,fee,wx_order_id,withdraw_time,`status`) " +
            "values(#{rewardWithdrawEntity.openId},#{rewardWithdrawEntity.partentTradeNo},#{rewardWithdrawEntity.fee},#{rewardWithdrawEntity.wXOrderId},#{rewardWithdrawEntity.withdrawTime},#{rewardWithdrawEntity.status})")
    @SelectKey(before=false,keyProperty="rewardWithdrawEntity.id",resultType=Long.class,statementType= StatementType.STATEMENT,statement="SELECT LAST_INSERT_ID() AS id")
    public void insert(@Param("rewardWithdrawEntity") RewardWithdrawEntity rewardWithdrawEntity);


    /**
     * 根据Id 查询
     * @param id
     * @return
     */
    @SelectProvider(type=RewardWithdrawSqlProvider.class, method="findByIdSql")
    @Results(value ={
            @Result(id=true, property="id",column="ID"),
            @Result(property="openId",column="openid"),
            @Result(property="partentTradeNo",column="partnet_trade_no"),
            @Result(property="fee",column="fee"),
            @Result(property="wXOrderId",column="wx_order_id"),
            @Result(property="withdrawTime",column="withdraw_time"),
            @Result(property="status",column="status")
    })
    public RewardWithdrawEntity select(@Param("id") Long id);


    /**
     * 根据orderId 查询
     * @param openId
     * @return
     */
    @SelectProvider(type=RewardWithdrawSqlProvider.class, method="findByOpenIdSql")
    @Results(value ={
            @Result(id=true, property="id",column="ID"),
            @Result(property="openId",column="openid"),
            @Result(property="partentTradeNo",column="partnet_trade_no"),
            @Result(property="fee",column="fee"),
            @Result(property="wXOrderId",column="wx_order_id"),
            @Result(property="withdrawTime",column="withdraw_time"),
            @Result(property="status",column="status")
    })
    public List<RewardWithdrawEntity> selectByOpenId(@Param("openId") String openId);

    /**
     * 根据Id 修改
     * @param rewardWithdrawEntity
     */
    @SelectProvider(type=RewardWithdrawSqlProvider.class, method="updateSql")
    public void update(@Param("rewardWithdrawEntity") RewardWithdrawEntity rewardWithdrawEntity);


    @Delete("delete from reward_withdraw where id = #{id}")
    public void delete(@Param("id") Long id);
}
