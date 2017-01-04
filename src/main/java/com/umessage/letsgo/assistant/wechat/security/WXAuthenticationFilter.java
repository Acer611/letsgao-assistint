/* Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.umessage.letsgo.assistant.wechat.security;


import com.umessage.letsgo.assistant.wechat.security.exception.UserNotFoundAuthenticationException;
import com.umessage.letsgo.assistant.wechat.security.token.WxVerificationAuthenticationToken;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 微信用户授权处理逻辑
 *
 * @author yongteng.huo 2015年7月28日
 * @see
 * @since 1.0
 */

public class WXAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    protected WxMpService wxMpService;

    public WxMpService getWxMpService() {
        return wxMpService;
    }

    public void setWxMpService(WxMpService wxMpService) {
        this.wxMpService = wxMpService;
    }

    public WXAuthenticationFilter() {
        super("/splat.dispatch");
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String code=request.getParameter("code");
        code= StringUtils.isBlank(code)?"":code;
        logger.debug("用户从微信登录入口进入， code："+code);

        WxMpOAuth2AccessToken token = null;
        WxMpUser wxMpUser = null;
        try {
            try {
                token = wxMpService.oauth2getAccessToken(code);
            } catch (WxErrorException e) {
                logger.error("获取用户token失败, code：" + code);
                throw new UserNotFoundAuthenticationException("不能获取用户access token");
            }

            if (token == null || StringUtils.isBlank(token.getOpenId())) {
                throw new UserNotFoundAuthenticationException("不能获取用户access token");
            }

            try {
                wxMpUser = wxMpService.getUserService().userInfo(token.getOpenId());
            } catch (WxErrorException e) {
                throw new UserNotFoundAuthenticationException("不能获取用户unionid");
            }

            if (wxMpUser == null){
                throw new UserNotFoundAuthenticationException("不能获取用户信息");
            }

            logger.debug("获取到登录用户的openid："+token.getOpenId() + "，unionid：" + token.getUnionId());

        } catch (UserNotFoundAuthenticationException e) {
            logger.debug(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("获取用户token失败",e);
            throw new BadCredentialsException(e.getMessage());
        }

        WxVerificationAuthenticationToken authRequest = new WxVerificationAuthenticationToken(token.getOpenId(), token.getAccessToken());
        authRequest.setUnionId(wxMpUser.getUnionId());
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * Provided so that subclasses may configure what is put into the authentication request's details
     * property.
     *
     * @param request that an authentication request is being created for
     * @param authRequest the authentication request object that should have its details set
     */
    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

}
