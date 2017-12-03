package com.taotao.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.web.bean.Cart;
import com.taotao.web.bean.User;
import com.taotao.web.threadlocal.UserThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author liu_mw
 * @date 2017/12/3 15:41
 */
@Service
public class CartService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Autowired
    private ApiService apiService;
    @Value("${TAOTAO_CART_URL}")
    private String TAOTAO_CART_URL;
    public List<Cart> queryCartList(){
        try {
            User user = UserThreadLocal.get();
            String url = TAOTAO_CART_URL + "/service/api/cart/" +user.getId();
            String jsonData = this.apiService.doGet(url);
            if(StringUtils.isNotEmpty(jsonData)){
                return MAPPER.readValue(jsonData,MAPPER.getTypeFactory().constructCollectionType(List.class,Cart.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
