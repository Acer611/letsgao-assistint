package com.umessage.letsgo.assistant.service;

import com.umessage.letsgo.assistant.model.po.WeChatInfoEntity;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

/**
 * Created by gaofei on 2016/12/15.
 */
public interface IWeChatInfoService {


    /**
     * 根据openID 获取用户信息
     * @param openId
     * @return
     */
    WeChatInfoEntity findWecahtUserByOpenID(String openId);

    /**
     * 创建微信用户
     * @param userWxInfo
     */
    void createWechatuser(WxMpUser userWxInfo,String pid);

    /**
     * 根据openid 更新ticketURl和ticket
     *  @param ticketUrl
     * @param openId
     */
    void updateTickURLByOpenID(String ticketUrl,String ticket, String openId);

    /**
     * 修改微信信息及status 状态
     * @param i
     */
    void updateStatus(WeChatInfoEntity wechatInfo,int i,String openId);

    /**
     * 修改status 状态
     * @param i
     * @param openId
     */
    void updateByStatus(int i,String openId);

    /**
     * 根据ID 获取微信信息
     * @param parentid
     * @return
     */
    WeChatInfoEntity findWecahtUserByID(String parentid);

    /**
     * 此方法只在系统部署后执行一次
     * 生成初始的100推广的二位码
     * @param weChatInfo
     */
    void createAssisTicket(WeChatInfoEntity weChatInfo);

    /**
     * 获取邀请二维码
     * @param openId
     * @return
     */
    String findAskTicket(String openId);

    /**
     * 修改收益
     * @param openId
     * @param amount
     */
    void modifyReward(String openId, Double amount);
}
