package com.taotao.cart.controller;

//import com.taotao.cart.bean.User;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.service.CartCookieService;
import com.taotao.cart.service.CartService;
import com.taotao.cart.threadlocal.UserThreadLocal;
import com.taotao.sso.query.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author liu_mw
 * @date 2017/12/3 10:06
 */
@RequestMapping("cart")
@Controller
public class CartController {
    @Autowired
    private CartCookieService cartCookieService;
    @Autowired
    private CartService cartService;
    @RequestMapping(value="list",method = RequestMethod.GET)
    public ModelAndView cartList(HttpServletRequest request){
        ModelAndView mv = new ModelAndView("cart");
        List<Cart> cartList = null;
        User user = UserThreadLocal.get();
        if(null==user){
            //未登入
            try {
                cartList = this.cartCookieService.queryCartList(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            //登入状态
            cartList = this.cartService.queryCartList();
        }
        mv.addObject("cartList",cartList);
        return mv;
    }

    /**
     * 加入商品到购物车
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}",method = RequestMethod.GET)
    public String addItemToCart(@PathVariable("itemId") Long itemId,
                                HttpServletRequest request, HttpServletResponse response){
        User user = UserThreadLocal.get();
        if(null==user){
            //未登入状态
            try {
                this.cartCookieService.addItemToCart(itemId,request,response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            //登入状态
            this.cartService.addItemToCart(itemId);
        }
        return "redirect:/cart/list.html";
    }

    /**
     * 修改购买数量
     * @param itemId
     * @param num
     * @return
     */
    @RequestMapping(value = "update/num/{itemId}/{num}",method = RequestMethod.POST)
    public ResponseEntity<Void> updateNum(@PathVariable("itemId") Long itemId,@PathVariable("num") Integer num,
                                          HttpServletRequest request,HttpServletResponse response){
        User user = UserThreadLocal.get();
        if(null==user){
            //未登入
            try {
                this.cartCookieService.updateNum(itemId,num,request,response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            //已登入
            this.cartService.updateNum(itemId,num);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 删除购物车的商品
     * @param itemId
     * @return
     */
    @RequestMapping(value="delete/{itemId}",method = RequestMethod.GET)
    public String deleteItem(@PathVariable("itemId") Long itemId,HttpServletRequest request,HttpServletResponse response){
        //判断用户是否登入
        User user = UserThreadLocal.get();
        if(null==user){
            //未登入
            try {
                this.cartCookieService.delete(itemId,request,response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            this.cartService.deleteItem(itemId);
        }
        return "redirect:/cart/list.html";
    }
}
