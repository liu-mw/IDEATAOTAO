package com.taotao.cart.interceptors;

//import com.taotao.cart.bean.User;
import com.taotao.cart.service.UserService;
import com.taotao.cart.threadlocal.UserThreadLocal;
import com.taotao.common.utils.CookieUtils;
import com.taotao.sso.query.bean.User;
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
        String token = CookieUtils.getCookieValue(httpServletRequest,COOKIE_NAME);
        if(StringUtils.isEmpty(token)){
            //未登入
            return true;
        }
        User user = this.userService.queryByToken(token);
        if(null == user){
            //登入超时，跳转到登入页面
            return true;
        }
        UserThreadLocal.set(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        UserThreadLocal.set(null);//清空上一次数据，即上一次的用户,也可以写在拦截器的after方法里
    }
}
