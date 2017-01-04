package com.umessage.letsgo.assistant.model.po;

import java.io.Serializable;

/**
 * Created by gaofei on 2016/12/15.
 */
public class WeChatInfoEntity implements Serializable {

    //系统id
    private Long ID;
    //父ID
    private String parentid;
    //微信的唯一标识
    private String unionid;
    //微信公众号对应用户的唯一标识openid
    private String openid;
    //微信昵称
    private String nickname;
    //性别
    private String sex;
    //省份
    private String province;
    //微信用户注册的城市
    private String city;
    //微信用户注册的国家
    private String country;
    //微信头像的url
    private String headImgUrl;
    //微信用户是否取消关注公众号【0 没有取消 ， 1 取消关注】
    private Integer status;
    //二维码ticket
    private String ticket;
    //二维码URL
    private String ticketUrl;
    //是否是导游 0 是导游 1 不是导游
    private Integer isGuide;
    //奖励收益
    private Double reward;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public Integer getIsGuide() {
        return isGuide;
    }

    public void setIsGuide(Integer isGuide) {
        this.isGuide = isGuide;
    }

    public Double getReward() {
        return reward;
    }

    public void setReward(Double reward) {
        this.reward = reward;
    }

    @Override
    public String toString() {
        return "WeChatInfoEntity{" +
                "ID=" + ID +
                ", parentid='" + parentid + '\'' +
                ", unionid='" + unionid + '\'' +
                ", openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", status=" + status +
                ", ticket='" + ticket + '\'' +
                ", ticketUrl='" + ticketUrl + '\'' +
                ", isGuide=" + isGuide +
                ", reward=" + reward +
                '}';
    }
}
