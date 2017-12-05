package com.taotao.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.sso.query.api.UserQueryService;
//import com.taotao.web.bean.User;
import com.taotao.sso.query.bean.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author liu_mw
 * @date 2017/11/28 20:41
 */
@Service
public class UserService {
    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private ApiService apiService;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Value("${TAOTAO_SSO_URL}")
    public String TAOTAO_SSO_URL;
    /**
     * 根据token获取用户信息
     * @param token
     * @return
     */
    /*public User queryByToken(String token){
        try {
            String url =  TAOTAO_SSO_URL + "/service/user/" + token;
            String jsonData = this.apiService.doGet(url);
            if(StringUtils.isNotEmpty(jsonData)){
                return MAPPER.readValue(jsonData,User.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * 上述方法通过dubbo改造
     * @param token
     * @return
     */
    public User queryByToken(String token){
        return this.userQueryService.queryUserByToken(token);
    }
}
