package com.umessage.letsgo.assistant.wechat.security.verification.impl;


import com.umessage.letsgo.assistant.wechat.security.UserAccountUserDetailsService;
import com.umessage.letsgo.assistant.common.exception.BusinessException;
import com.umessage.letsgo.assistant.wechat.security.token.WxVerificationAuthenticationToken;
import com.umessage.letsgo.assistant.wechat.security.verification.IVerification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class WxVerification implements IVerification {
    private final static Logger logger = LoggerFactory.getLogger(WxVerification.class);
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Autowired
    private UserAccountUserDetailsService userAccountUserDetailsService;

    public void verificationAuthenticationChecks(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (username == null) {
            logger.debug("Authentication failed: no username provided");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad username"));
        }
        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials", "no token"));
        }
        // filter中已经验证微信公众号合法行,不需要再次验证
    }

    public final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed:p no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials", "no token"));
        }
        // 微信的unionID

        String unionId = ((WxVerificationAuthenticationToken)authentication).getUnionId();
        String openId = authentication.getName();
        logger.info("WxVerification 类获取微信unionid:" + unionId );
        UserDetails user;
        try {
            user = userAccountUserDetailsService.loadUserByUsername(openId);
            logger.info("WxVerification 类获取微信user对象:" + user.getUsername() );
        } catch (BusinessException e) {
            user = userAccountUserDetailsService.createWxUserDetails(openId, authentication.getCredentials().toString(),unionId);
            logger.debug(e.getMessage());
            //throw e;
        } catch (Exception repositoryProblem) {
            logger.debug(repositoryProblem.getMessage());

            throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }

        if (user == null) {
            throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }
        return user;
    }

}