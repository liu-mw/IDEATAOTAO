package com.taotao.web.interceptors;

import com.taotao.common.utils.CookieUtils;
import com.taotao.web.bean.User;
import com.taotao.web.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liu_mw
 * @date 2017/11/28 20:38
 */
public class UserLoginHandlerInterceptor implements HandlerInterceptor{
    @Autowired
    private UserService userService;

    public static final String COOKIE_NAME = "TT_TOKEN";

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String loginUrl = this.userService.TAOTAO_SSO_URL + "/user/login.html";

        String token = CookieUtils.getCookieValue(httpServletRequest,COOKIE_NAME);
        if(StringUtils.isEmpty(token)){
            //未登入
            httpServletResponse.sendRedirect(loginUrl);
            return false;
        }
        User user = this.userService.queryByToken(token);
        if(null == user){
            //登入超时，跳转到登入页面
            httpServletResponse.sendRedirect(loginUrl);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
