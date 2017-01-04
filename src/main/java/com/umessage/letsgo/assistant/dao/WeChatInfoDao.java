package com.umessage.letsgo.assistant.dao;

import com.umessage.letsgo.assistant.dao.provider.WeChatInfoSqlProvider;
import com.umessage.letsgo.assistant.model.po.WeChatInfoEntity;
import org.apache.ibatis.annotations.*;

/**
 * Created by gaofei on 2016/12/15.
 */
@Mapper
public interface WeChatInfoDao {


    /**
     * 插入微信信息
     * @param wechatInfo
     * @return
     */
    @Insert("INSERT INTO wechat_info(parentid,unionid,openid,nickname,sex,province,city,country,headImgUrl," +
            "status,ticket_url,ticket,is_guide,reward) VALUES(#{parentid},#{unionid},#{openid},#{nickname},#{sex},#{province}" +
            ",#{city},#{country},#{headImgUrl},#{status},#{ticketUrl},#{ticket},#{isGuide},#{reward})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    int insert(WeChatInfoEntity wechatInfo);

    /**
     * 根据openID 查找用户信息
     * @param openid
     * @return
     */
    @Select("SELECT * FROM wechat_info WHERE openid = #{openid}")
    @Results(
            {
                    @Result(id = true, column = "id", property = "ID"),
                    @Result(column = "unionid", property = "unionid"),
                    @Result(column = "parentid", property = "parentid"),
                    @Result(column = "openid", property = "openid"),
                    @Result(column = "ticket_url", property = "ticketUrl"),
                    @Result(column = "ticket", property = "ticket"),
                    @Result(column = "nickname", property = "nickname"),
                    @Result(column = "is_guide", property = "isGuide"),
                    @Result(column = "reward", property = "reward")
            })
    WeChatInfoEntity findByOpenID(@Param("openid") String openid);

    /**
     * 根据openID 修改二维码信息
     * @param wechatInfo
     */
    @Update("UPDATE wechat_info SET ticket_url=#{ticketUrl},ticket=#{ticket} WHERE openid=#{openid}")
    void updateTicketUrlByOpenID(WeChatInfoEntity wechatInfo);

    /**
     * 取消关注
     * @param wechatInfo
     */
    @Update("UPDATE wechat_info SET status=1 WHERE openid=#{openid}")
    void updateStatusByOpenID(WeChatInfoEntity wechatInfo);


    /**
     * 更改status 状态
     * @param wechatInfo
     */
    @SelectProvider(type=WeChatInfoSqlProvider.class, method="updateByOpenIdSql")
    void updateStatus(@Param("wechatInfo") WeChatInfoEntity wechatInfo);

    @Select("SELECT * FROM wechat_info WHERE id = #{ID}")
    @Results(
            {
                    @Result(id = true, column = "id", property = "ID"),
                    @Result(column = "unionid", property = "unionid"),
                    @Result(column = "parentid", property = "parentid"),
                    @Result(column = "openid", property = "openid"),
                    @Result(column = "ticket_url", property = "ticketUrl"),
                    @Result(column = "ticket", property = "ticket"),
                    @Result(column = "is_guide", property = "isGuide"),
                    @Result(column = "reward", property = "reward")
            })
    WeChatInfoEntity selectWecahtUserByID(@Param("ID")String ID);
}
