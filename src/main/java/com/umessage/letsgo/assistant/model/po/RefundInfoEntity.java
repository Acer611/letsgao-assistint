package com.umessage.letsgo.assistant.model.po;

import java.io.Serializable;
import java.util.Date;

/**
 * 退款记录实体类
 * Created by gaofei on 2016/12/30.
 */
public class RefundInfoEntity implements Serializable {
  /*  ID                   bigint not null comment '主键id',
    openid               varchar(256) comment '用户的openid',
    order_id             varchar(256) comment '订单号',
    refund_no            varchar(256),
    refund_id            varchar(256),
    create_time          timestamp comment '退款时间',
    fee                  double comment '退款金额',
    too_fee              double,
    cash_fee             double,*/

    //主键
    private Long id;
    // 用户的openid
    private String openid;
    //订单号（和订单信息表中的orderId 相关）
    private String orderId;
    //退款订单号
    private String refundNo;
    //微信退款单号
    private String refundId;
    //退款时间
    private Date createTime;
    //退款金额
    private Double fee;
    //标价金额
    private Double tooFee;
    //现金支付金额
    private Double cashFee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Double getTooFee() {
        return tooFee;
    }

    public void setTooFee(Double tooFee) {
        this.tooFee = tooFee;
    }

    public Double getCashFee() {
        return cashFee;
    }

    public void setCashFee(Double cashFee) {
        this.cashFee = cashFee;
    }

    @Override
    public String toString() {
        return "RefundInfoEntity{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", orderId='" + orderId + '\'' +
                ", refundNo='" + refundNo + '\'' +
                ", refundId='" + refundId + '\'' +
                ", createTime=" + createTime +
                ", fee=" + fee +
                ", tooFee=" + tooFee +
                ", cashFee=" + cashFee +
                '}';
    }
}
