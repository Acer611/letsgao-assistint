package com.umessage.letsgo.assistant.wechat.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created by gaofei on 2016/12/7.
 */

/**
 * 继承spring security user 对象
 */
public class WxUser extends User{

    private String unionId;
    public WxUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public WxUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String unionId) {
        super(username, password, authorities);
        this.unionId = unionId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
}
