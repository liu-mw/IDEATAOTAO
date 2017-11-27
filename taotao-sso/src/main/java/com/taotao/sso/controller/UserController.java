package com.taotao.sso.controller;

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
    private static final String COOKIE_NAME = "TT_TOKEN";
    @Autowired
    private UserService userService;

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
        try {
            User user = this.userService.queryByToken(token);
            if(null == user){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
