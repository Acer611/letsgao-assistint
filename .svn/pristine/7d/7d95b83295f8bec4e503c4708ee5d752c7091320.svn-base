package com.umessage.letsgo.assistant.wechat.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class WxVerificationAuthenticationToken extends UsernamePasswordAuthenticationToken {


    private String unionId;

    public WxVerificationAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public WxVerificationAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
     }
}
