package com.umessage.letsgo.assistant.service.impl;

import com.umessage.letsgo.assistant.dao.RefundInfoDao;
import com.umessage.letsgo.assistant.model.po.RefundInfoEntity;
import com.umessage.letsgo.assistant.model.po.WeChatOrderInfoEntity;
import com.umessage.letsgo.assistant.service.IRefundInfoService;
import me.chanjar.weixin.mp.bean.pay.result.WxPayRefundResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by gaofei on 2016/12/30.
 */

@Service
public class RefundInfoServiceImpl implements IRefundInfoService {

    @Autowired
    private RefundInfoDao refundInfoDao;


    @Override
    public void insertPayInfo(RefundInfoEntity refundInfoEntity) {
        refundInfoDao.insert(refundInfoEntity);
    }

    @Override
    public RefundInfoEntity selectById(Long id) {
        return refundInfoDao.select(id);
    }

    @Override
    public void updateByID(RefundInfoEntity refundInfoEntity) {
        refundInfoDao.update(refundInfoEntity);
    }

    @Override
    public void delete(Long id) {
        refundInfoDao.delete(id);
    }


    /**
     * 生成退款记录
     * @param orderInfoEntity
     * @param wxPayRefundResult
     * @param refundFee
     */
    public void createRefundInfo(WeChatOrderInfoEntity orderInfoEntity, WxPayRefundResult wxPayRefundResult,Double refundFee ) {
        RefundInfoEntity refundInfoEntity =  new RefundInfoEntity();
        refundInfoEntity.setOrderId(orderInfoEntity.getOrderId());
        refundInfoEntity.setCreateTime(new Date());
        refundInfoEntity.setFee(refundFee);
        refundInfoEntity.setOpenid(orderInfoEntity.getCustomerOpenid());
        refundInfoEntity.setTooFee(orderInfoEntity.getTotalFee());
        refundInfoEntity.setRefundId(wxPayRefundResult.getRefundId());
        refundInfoEntity.setRefundNo(wxPayRefundResult.getOutRefundNo());


        refundInfoDao.insert(refundInfoEntity);

    }

    @Override
    public RefundInfoEntity findRefundByOrderId(String orderId) {
        return refundInfoDao.selectByOrderId(orderId);
    }
}
