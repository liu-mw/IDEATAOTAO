package com.taotao.sso.query.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisServiceOptimize;
import com.taotao.sso.query.api.UserQueryService;
import com.taotao.sso.query.bean.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author liu_mw
 * @date 2017/12/5 20:16
 */
@Service
public class UserQueryServiceImpl implements UserQueryService{
    @Autowired
    private RedisServiceOptimize redisServiceOptimize;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Integer REDIS_TIME = 60 * 30;
    @Override
    public User queryUserByToken(String token) {
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
