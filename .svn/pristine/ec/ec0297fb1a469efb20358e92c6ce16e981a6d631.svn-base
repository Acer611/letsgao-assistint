package com.umessage.letsgo.assistant.service.impl;

import com.umessage.letsgo.assistant.dao.WeChatInfoDao;
import com.umessage.letsgo.assistant.model.po.WeChatInfoEntity;
import com.umessage.letsgo.assistant.service.IWeChatInfoService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;

/**
 * Created by gaofei on 2016/12/15.
 */

@Service
public class WeChatInfoServiceImpl implements IWeChatInfoService {

    @Resource
    private WeChatInfoDao wechatInfoDao;

    @Override
    public WeChatInfoEntity findWecahtUserByOpenID(String openId) {
        return wechatInfoDao.findByOpenID(openId);
    }

    @Override
    public void createWechatuser(WxMpUser userWxInfo,String pid) {
        WeChatInfoEntity wechatInfo =  new WeChatInfoEntity();
        wechatInfo.setOpenid(userWxInfo.getOpenId());
        wechatInfo.setCity(userWxInfo.getCity());
        wechatInfo.setSex(userWxInfo.getSex());
        wechatInfo.setHeadImgUrl(userWxInfo.getHeadImgUrl());
        wechatInfo.setCountry(userWxInfo.getCountry());
        wechatInfo.setUnionid(userWxInfo.getUnionId());
        wechatInfo.setReward(0.0);
        String nickName = null;
        try{
            nickName = URLEncoder.encode(userWxInfo.getNickname(), "utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }

        wechatInfo.setNickname(nickName);
        wechatInfo.setNickname(userWxInfo.getNickname());
        wechatInfo.setProvince(userWxInfo.getProvince());
        wechatInfo.setStatus(0);
        if(!"0".equals(pid) ){
            int iPid = Integer.parseInt(pid);
            if(iPid<=100){
                wechatInfo.setIsGuide(0);
            }else{
                wechatInfo.setIsGuide(1);
            }
            wechatInfo.setParentid(pid);
        }else{
            wechatInfo.setParentid("0");
            wechatInfo.setIsGuide(1);
        }
        wechatInfoDao.insert(wechatInfo);

    }

    @Override
    public void updateTickURLByOpenID(String ticketUrl,String ticket, String openId) {
        WeChatInfoEntity wechatInfo =  new WeChatInfoEntity();
        wechatInfo.setTicketUrl(ticketUrl);
        wechatInfo.setTicket(ticket);
        wechatInfo.setOpenid(openId);
        wechatInfoDao.updateTicketUrlByOpenID(wechatInfo);
    }

    @Override
    public void updateStatus(WeChatInfoEntity wechatInfo,int i,String openId) {
        wechatInfo.setStatus(i);
        wechatInfo.setOpenid(openId);
        wechatInfoDao.updateStatus(wechatInfo);
    }

    @Override
    public void updateByStatus(int i,String openId) {
        WeChatInfoEntity wechatInfo = new WeChatInfoEntity();
        wechatInfo.setStatus(i);
        wechatInfo.setOpenid(openId);
        wechatInfoDao.updateStatus(wechatInfo);
    }

    @Override
    public WeChatInfoEntity findWecahtUserByID(String parentid) {
        return wechatInfoDao.selectWecahtUserByID(parentid);
    }


    @Override
    public void createAssisTicket(WeChatInfoEntity weChatInfo) {
        wechatInfoDao.insert(weChatInfo);
    }

    @Override
    public String findAskTicket(String openId) {
        WeChatInfoEntity weChatInfoEntity = wechatInfoDao.findByOpenID(openId);
        WeChatInfoEntity askUserInfo = wechatInfoDao.selectWecahtUserByID(weChatInfoEntity.getParentid());
        return askUserInfo.getTicket();
    }

    @Override
    public void modifyReward(String openId, Double amount) {
        WeChatInfoEntity weChatInfoEntity = wechatInfoDao.findByOpenID(openId);
        double reward = weChatInfoEntity.getReward();
        reward = reward - amount;
        weChatInfoEntity.setReward(reward);
        wechatInfoDao.updateStatus(weChatInfoEntity);

    }
}
