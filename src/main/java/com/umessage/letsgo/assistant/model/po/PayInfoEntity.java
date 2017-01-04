package com.umessage.letsgo.assistant.model.po;

import java.io.Serializable;
import java.util.Date;

/**
 * 支付记录实体类
 * Created by gaofei on 2016/12/29.
 */

public class PayInfoEntity implements Serializable {


   /*   ID                   bigint not null comment '系统id',
   openid               varchar(256) comment '用户的openid',
   order_id             varchar(256) comment '订单号',
   transaction_id       varchar(256),
   create_time          timestamp comment '支付或退款时间',
   fee                  double comment '支付或退款金额',,*/

    //主键
    private Long id;
    // 用户的openid
    private String openId;
    //订单号（和订单信息表中的orderId 相关）
    private String orderId;
    //微信支付订单号
    private String transactionId;
    //支付或退款金额
    private Double fee;
    //支付或退款时间
    private Date createTime;
    //支付状态 0 已支付 1 未支付
    private  Integer status;


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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PayInfoEntity{" +
                "id=" + id +
                ", openId='" + openId + '\'' +
                ", order_id='" + orderId + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", fee=" + fee +
                ", create_time=" + createTime +
                '}';
    }
}
