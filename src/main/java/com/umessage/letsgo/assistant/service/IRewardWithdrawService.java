package com.umessage.letsgo.assistant.service;

import com.google.gson.JsonObject;
import com.umessage.letsgo.assistant.model.po.RewardWithdrawEntity;

import java.util.HashMap;
import java.util.List;

/**
 * 支付记录接口
 * Created by gaofei on 2016/12/30.
 */
public interface IRewardWithdrawService {

    /**
     * 生成
     * @param rewardWithdrawEntity
     * @return
     */
    public void insert(RewardWithdrawEntity rewardWithdrawEntity);

    /**
     * 修改
     * @param rewardWithdrawEntity
     * @return
     */
    public void update(RewardWithdrawEntity rewardWithdrawEntity);

    /**
     * 查询
     * @param id
     * @return
     */
    public RewardWithdrawEntity select(Long id);

    /**
     * 根据OopenID 获取
     * @param openId
     * @return
     */
    public List<RewardWithdrawEntity> selectByOpenId(String openId);

    /**
     * 提现操作
     * @param amount 退款金额
     * @param openId
     */
    boolean  withDraw(Double amount,String openId);

    /**
     * 生成提现记录
     * @param openId 提现人 openID
     * @param partenerNo 提现商户订单号
     * @param amount 提现金额
     */
    void generateRewardInfo(String openId, String partenerNo, Double amount,int status,HashMap resultMap);
}
