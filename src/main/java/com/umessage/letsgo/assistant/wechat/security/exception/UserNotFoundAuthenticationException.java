package com.umessage.letsgo.assistant.wechat.security.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNotFoundAuthenticationException extends AuthenticationException{
    private static final long serialVersionUID = -5163053514061026964L;

    public UserNotFoundAuthenticationException(String msg) {
        super(msg);
    }

}
