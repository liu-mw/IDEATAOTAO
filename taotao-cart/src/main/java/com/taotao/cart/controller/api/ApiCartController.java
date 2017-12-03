package com.taotao.cart.controller.api;

import com.taotao.cart.pojo.Cart;
import com.taotao.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author liu_mw
 * @date 2017/12/3 15:29
 */
@Controller
@RequestMapping("api/cart")
public class ApiCartController {
    @Autowired
    private CartService cartService;

    /**
     * 对外提供的查询接口服务
     * @param userId
     * @return
     */
    @RequestMapping(value = "{userId}",method = RequestMethod.GET)
    public ResponseEntity<List<Cart>> queryCartListByUserId(@PathVariable("userId") Long userId){
        try {
            List<Cart> carts = this.cartService.queryCartList(userId);
            if(null == carts || carts.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(carts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
