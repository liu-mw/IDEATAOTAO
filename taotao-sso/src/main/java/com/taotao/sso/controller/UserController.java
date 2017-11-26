package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author liu_mw
 * @date 2017/11/26 11:33
 */
@RequestMapping("user")
@Controller
public class UserController {
    /**
     * 注册页面
     * @return
     */
    @RequestMapping(value = "register",method = RequestMethod.GET)
    public String toRegister(){
        return "register";
    }
}
