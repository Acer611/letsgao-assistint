package com.umessage.letsgo.assistant.model.po;

/**
 * Created by gaofei on 2016/12/29.
 */

import java.io.Serializable;
import java.util.Date;

/**
 * 提现记录表
 */
public class RewardWithdrawEntity implements Serializable {
    //主键
    private Long id;
   // 用户的openid
    private String openId;
    //商户订单号
    private String partentTradeNo;
    //提现金额
    private Double fee;
    //提现时间
    private Date withdrawTime;
    //微信订单号
    private String wXOrderId;
    //是否提现成功 0 成功 1 失败
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPartentTradeNo() {
        return partentTradeNo;
    }

    public void setPartentTradeNo(String partentTradeNo) {
        this.partentTradeNo = partentTradeNo;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Date getWithdrawTime() {
        return withdrawTime;
    }

    public void setWithdrawTime(Date withdrawTime) {
        this.withdrawTime = withdrawTime;
    }

    public String getwXOrderId() {
        return wXOrderId;
    }

    public void setwXOrderId(String wXOrderId) {
        this.wXOrderId = wXOrderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RewardWithdrawEntity{" +
                "id=" + id +
                ", openId='" + openId + '\'' +
                ", partentTradeNo='" + partentTradeNo + '\'' +
                ", fee=" + fee +
                ", withdrawTime=" + withdrawTime +
                ", wXOrderId='" + wXOrderId + '\'' +
                ", status=" + status +
                '}';
    }
}
