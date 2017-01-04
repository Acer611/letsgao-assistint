package com.umessage.letsgo.assistant.wechat.security;

import com.umessage.letsgo.assistant.wechat.security.token.WxVerificationAuthenticationToken;
import com.umessage.letsgo.assistant.wechat.security.verification.IVerification;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class CustomUserAuthenticationProvider extends DaoAuthenticationProvider implements ApplicationContextAware {

    private ApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }

    private Object getBean(String paramString) {
        return appContext.getBean(paramString);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (WxVerificationAuthenticationToken.class.isInstance(authentication)) {
            //微信授权登陆方式
            return customAuthenticate(authentication, (IVerification) getBean("wxVerification"));
        } else if (UsernamePasswordAuthenticationToken.class.isInstance(authentication)) {
            return super.authenticate(authentication);
        } else {
            return super.authenticate(authentication);
        }
    }

    private Authentication customAuthenticate(Authentication authentication, IVerification verification) throws AuthenticationException {
        Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication,
                messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports",
                        "Only UsernamePasswordAuthenticationToken is supported"));

        // Determine username
        String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();

        boolean cacheWasUsed = true;
        UserDetails user = this.getUserCache().getUserFromCache(username);

        //UserAccountUserDetailsService userAccountUserDetailsService = (UserAccountUserDetailsService) this.getUserDetailsService();

        if (user == null) {
            cacheWasUsed = false;

            try {
                verification.verificationAuthenticationChecks(username, (UsernamePasswordAuthenticationToken) authentication);
                user = verification.retrieveUser(username, (UsernamePasswordAuthenticationToken) authentication);
            } catch (AuthenticationException exception) {
                logger.debug("User '" + username + "' not found, exception:", exception);

                if (hideUserNotFoundExceptions) {
                    throw new BadCredentialsException(messages.getMessage(
                            "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
                } else {
                    throw exception;
                }
            }

            Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");
        }

        try {
            getPreAuthenticationChecks().check(user);
            //additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) authentication);
        } catch (AuthenticationException exception) {
            if (cacheWasUsed) {
                // There was a problem, so try again after checking
                // we're using latest data (i.e. not from the cache)
                cacheWasUsed = false;
                user = verification.retrieveUser(username, (UsernamePasswordAuthenticationToken) authentication);
                getPreAuthenticationChecks().check(user);
                //additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) authentication);
            } else {
                throw exception;
            }
        }

        getPostAuthenticationChecks().check(user);

        if (!cacheWasUsed) {
            this.getUserCache().putUserInCache(user);
        }

        Object principalToReturn = user;

        if (isForcePrincipalAsString()) {
            principalToReturn = user.getUsername();
        }

        // 如果原Authentication中用户名与获取到的UserDetails不一致,则返回UserDetails中用户名
        if (!username.equals(user.getUsername())) {
            authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        }

        return createSuccessAuthentication(principalToReturn, authentication, user);
    }

    @Autowired
    @Override
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
    }

}
