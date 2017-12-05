package com.taotao.sso.controller;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.taotao.common.service.RedisServiceOptimize;
import com.taotao.common.utils.CookieUtils;
import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import sun.awt.SunHints;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liu_mw
 * @date 2017/11/26 11:33
 */
@RequestMapping("user")
@Controller
public class UserController {
    @Autowired
    private RedisServiceOptimize redisServiceOptimize;
    @Autowired
    private UserService userService;

    private static final String COOKIE_NAME = "TT_TOKEN";
    /**
     * 注册页面
     *
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String toRegister() {
        return "register";
    }

    /**
     * 登入
     * @return
     */
    @RequestMapping(value="login",method = RequestMethod.GET)
    public String toLogin(){
        return "login";
    }
    @RequestMapping(value="doLogin",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> doLogin(User user, HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> result = new HashMap<String,Object>();
        try {
            String token = this.userService.doLogin(user.getUsername(),user.getPassword());
            if(StringUtils.isEmpty(token)){
                //登入失败
                result.put("status",500);
                return result;
            }
            //登入成功
            result.put("status",200);
            CookieUtils.setCookie(request,response,COOKIE_NAME,token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    @RequestMapping(value="quit",method = RequestMethod.GET)
    public ResponseEntity<Boolean> toLogin(HttpServletRequest request, HttpServletResponse response){
        String token = CookieUtils.getCookieValue(request,COOKIE_NAME);
        String loginUrl = "http://sso.taotao.com/user/login.html";
        //清除缓存中的数据
        Long del = this.redisServiceOptimize.del("TOKEN_" + token);
        try {
            response.sendRedirect(loginUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(true);
    }
    /**
     * 查询用户是否存在
     *
     * @param param
     * @param type
     * @return
     */
    @RequestMapping(value = "{param}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> check(@PathVariable("param") String param,
                                         @PathVariable("type") Integer type) {
        try {
            Boolean bool = this.userService.check(param, type);
            if (null == bool) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            return ResponseEntity.ok(!bool);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "doRegister", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doRegister(@Valid User user, BindingResult bindingResult) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (bindingResult.hasErrors()) {
            List<String> msgs = new ArrayList<String>();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError objectError : allErrors) {
                msgs.add(objectError.getDefaultMessage());
            }
            result.put("status", "400");
            result.put("data", StringUtils.join(msgs, "|"));
            return result;
        }
        try {
            Boolean bool = this.userService.doRegister(user);
            if (bool) {
                result.put("status", "200");
            } else {
                result.put("status", "500");
                result.put("data", "登入有误！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据token获取用户信息用于登入时候展现
     * @param token
     * @return
     */
    @RequestMapping(value="{token}",method = RequestMethod.GET)
    public ResponseEntity<User> queryByToken(@PathVariable("token") String token){
        /*try {
            User user = this.userService.queryByToken(token);
            if(null == user){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);*/
        User user = new User();
        user.setUsername("该服务没有，已经转移到dubbo上，请访问dubbo服务");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
    }
}
