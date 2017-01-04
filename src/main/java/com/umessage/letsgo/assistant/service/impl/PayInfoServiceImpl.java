package com.umessage.letsgo.assistant.service.impl;

import com.umessage.letsgo.assistant.common.utils.WxPayOrderNotifyResult;
import com.umessage.letsgo.assistant.dao.PayInfoDao;
import com.umessage.letsgo.assistant.model.po.PayInfoEntity;
import com.umessage.letsgo.assistant.model.po.WeChatOrderInfoEntity;
import com.umessage.letsgo.assistant.service.IPayInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by gaofei on 2016/12/30.
 */

@Service
public class PayInfoServiceImpl  implements IPayInfoService {

    @Autowired
    private PayInfoDao payInfoDao;

    /**
     * 生成支付记录
     * @param payInfoEntity
     * @return
     */
    @Override
    public PayInfoEntity createPaInfo(PayInfoEntity payInfoEntity) {
         payInfoDao.insert(payInfoEntity);
        return payInfoEntity;
    }

    /**
     * 修改支付记录根据id
     *
     * @param payInfoEntity
     * @return
     */
    @Override
    public PayInfoEntity mofifyPayInfo(PayInfoEntity payInfoEntity) {
        payInfoDao.update(payInfoEntity);
        return payInfoEntity;
    }

    /**
     * 根据openId 获取所有的支付记录
     * @param openId
     * @return
     */
    @Override
    public List<PayInfoEntity> getAllPayInfoEntityByOpenID(String openId) {

        //TODO add method
        return null;
    }

    /**
     * 根据orderId 获取支付信息
     * @param orderId
     * @return
     */
    @Override
    public PayInfoEntity getPayInfoByOrderId(String orderId) {
        return payInfoDao.selectByOrderId(orderId);
    }

    /**
     * 拼接修改支付信息的方法
     * @param wxPayOrderNotifyResult
     */
    @Override
    public void generateUpdateDate(WxPayOrderNotifyResult wxPayOrderNotifyResult) {
        PayInfoEntity payInfoEntity =  new PayInfoEntity();
        payInfoEntity.setOpenId(wxPayOrderNotifyResult.getOpenid());
        payInfoEntity.setOrderId(wxPayOrderNotifyResult.getOut_trade_no());
        payInfoEntity.setTransactionId(wxPayOrderNotifyResult.getOut_trade_no());
        payInfoEntity.setStatus(0);

        /*payInfoEntity.setOpenId("1234");
        payInfoEntity.setTransactionId("1234");
        payInfoEntity.setOrderId("121");*/
        this.modifyByOrderId(payInfoEntity);
    }

    @Override
    public void modifyByOrderId(PayInfoEntity payInfoEntity) {
        payInfoDao.updateByOrderId(payInfoEntity);
    }

    /**
     * 生成支付记录的参数的拼接方法
     * @param wechatOrderInfoEntities
     */
    @Override
    public void generatePaInfo(WeChatOrderInfoEntity wechatOrderInfoEntities) {
        PayInfoEntity exsitPayInfo = getPayInfoByOrderId(wechatOrderInfoEntities.getOrderId());
        if(null!= exsitPayInfo){
            PayInfoEntity payInfoEntity =  new PayInfoEntity();
            payInfoEntity.setOpenId(wechatOrderInfoEntities.getCustomerOpenid());
            payInfoEntity.setFee(wechatOrderInfoEntities.getTotalFee());
            payInfoEntity.setOrderId(wechatOrderInfoEntities.getOrderId());
            payInfoEntity.setStatus(1);
            payInfoEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));

           /* payInfoEntity.setOpenId("111");
            payInfoEntity.setOrderId("121");
            payInfoEntity.setFee(1.0);*/
            this.createPaInfo(payInfoEntity);
        }
    }




}
