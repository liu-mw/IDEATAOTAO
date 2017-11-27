package com.taotao.sso.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.mapper.Mapper;
import com.taotao.common.service.RedisServiceOptimize;
import com.taotao.sso.mapper.UserMapper;
import com.taotao.sso.pojo.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
 * @author liu_mw
 * @date 2017/11/27 20:36
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisServiceOptimize redisServiceOptimize;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Integer REDIS_TIME = 60 * 30;

    public Boolean check(String param, Integer type) {
        User record = new User();
        switch (type) {
            case 1:
                record.setUsername(param);
                break;
            case 2:
                record.setPhone(param);
                break;
            case 3:
                record.setEmail(param);
                break;
            default:
                return null;
        }
        return this.userMapper.selectOne(record) == null;
    }

    public Boolean doRegister(User user) {
        user.setId(null);
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());
        //用apache下的加密包
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        //等于1表示插入成功，返回值就是1
        return this.userMapper.insert(user) ==1;
    }

    public String doLogin(String username,String password) throws Exception{
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        if(null == user){
            //用户不存在
            return null;
        }
        if(!StringUtils.equals(DigestUtils.md5Hex(password),user.getPassword())){
            //密码不匹配
            return null;
        }
        //登入成功，将用户的信息保存到redis中，这里只能抛出异常，不能捕获，要是捕获了
        //后面逻辑还能执行，影响了原有逻辑，因为这里redis是作为存储对象
        //生产自己的token，这样不用依赖cookie的sessionid了，即使session过期也可以用自己的
        String token = DigestUtils.md5Hex(username+System.currentTimeMillis());
        //保存数据到redis的密码必须为空,第一种方案
//        user.setPassword(null);
        this.redisServiceOptimize.set("TOKEN_"+token,MAPPER.writeValueAsString(user),REDIS_TIME);
        return token;
    }

    public User queryByToken(String token) {
        String key = "TOKEN_" + token;
        String jsonData = this.redisServiceOptimize.get(key);
        if(StringUtils.isEmpty(jsonData)){
            return null;
        }
        //成功
        try {
            //需要重新设置过期时间
            this.redisServiceOptimize.expire(key,REDIS_TIME);
            return MAPPER.readValue(jsonData,User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
