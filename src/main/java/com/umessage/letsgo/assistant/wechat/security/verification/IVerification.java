package com.umessage.letsgo.assistant.wechat.security.verification;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by zhajl on 16/11/24.
 */
public interface IVerification {

    void verificationAuthenticationChecks(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException;

    UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication);
}
