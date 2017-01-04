package com.umessage.letsgo.assistant.wechat.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * Uses the internal map of exceptions types to URLs to determine the destination on authentication failure. The keys
 * are the full exception class names.
 * <p>
 * If a match isn't found, falls back to the behaviour of the parent class,
 * {@link SimpleUrlAuthenticationFailureHandler}.
 * <p>
 * The map of exception names to URLs should be injected by setting the <tt>exceptionMappings</tt> property.
 *
 * @author Luke Taylor
 * @since 3.0
 */
public class ReferUrlAuthenticationFailureHandler extends ExceptionMappingAuthenticationFailureHandler {
    private static final String TARGET_URL_PLACEHOLDER="_TARGET_URL_PLACEHOLDER_";

    private final Map<String, String> failureUrlMap = new HashMap<String, String>();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        String url = failureUrlMap.get(exception.getClass().getName());
        logger.debug("登陆失败，url:"+url);
        logger.debug("exception.getClass().getName():"+exception.getClass().getName());
        String targetUrl=URLEncoder.encode("?targetUrl="+request.getParameter("targetUrl"),"UTF-8");
        
        if (url != null) {
            url=url.replaceAll(TARGET_URL_PLACEHOLDER,targetUrl);
            getRedirectStrategy().sendRedirect(request, response, url);
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
    @Override
    public void setExceptionMappings(Map<?,?> failureUrlMap) {
        this.failureUrlMap.clear();
        for (Map.Entry<?,?> entry : failureUrlMap.entrySet()) {
            Object exception = entry.getKey();
            Object url = entry.getValue();
            Assert.isInstanceOf(String.class, exception, "Exception key must be a String (the exception classname).");
            Assert.isInstanceOf(String.class, url, "URL must be a String");
            Assert.isTrue(UrlUtils.isValidRedirectUrl((String)url), "Not a valid redirect URL: " + url);
            this.failureUrlMap.put((String)exception, (String)url);
        }
    }
}
