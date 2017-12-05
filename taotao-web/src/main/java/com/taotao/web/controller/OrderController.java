package com.taotao.web.controller;

import com.taotao.manager.pojo.Item;
import com.taotao.sso.query.bean.User;
import com.taotao.web.bean.Cart;
import com.taotao.web.bean.Order;
//import com.taotao.web.bean.User;
import com.taotao.web.interceptors.UserLoginHandlerInterceptor;
import com.taotao.web.service.CartService;
import com.taotao.web.service.ItemService;
import com.taotao.web.service.OrderService;
import com.taotao.web.service.UserService;
import com.taotao.web.threadlocal.UserThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liu_mw
 * @date 2017/11/28 20:25
 */
@RequestMapping("order")
@Controller
public class OrderController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ItemService itemService;
    @RequestMapping(value="{itemId}",method = RequestMethod.GET)
    public ModelAndView toOrder(@PathVariable("itemId") Long itemId){
        ModelAndView mv = new ModelAndView("order");
        Item item = this.itemService.queryItemById(itemId);
        mv.addObject("item",item);
        return mv;
    }

    /**
     * j基于购物车实现下单确认页的跳转（全部商品）
     * @return
     */
    @RequestMapping(value = "create",method = RequestMethod.GET)
    public ModelAndView toCartOrder(){
        ModelAndView mv = new ModelAndView("order-cart");
        List<Cart> cartList = this.cartService.queryCartList();
        mv.addObject("carts",cartList);
        return mv;
    }
    /**
     * 提交订单
     * @param order
     * @return
     */
    @RequestMapping(value="submit",method = RequestMethod.POST)
    @ResponseBody
//    public Map<String,Object> submit(Order order, @CookieValue(UserLoginHandlerInterceptor.COOKIE_NAME) String token){第一种方案
    public Map<String,Object> submit(Order order){

        Map<String,Object> result = new HashMap<String,Object>();
        //填充当前用户信息，要不然提交时候报错，格式没有对应
        User user = UserThreadLocal.get();//this.userService.queryByToken(token);防止两次查询用户
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());

        String orderId = this.orderService.submit(order);
        if(StringUtils.isEmpty(orderId)){
            //订单提交失败
            result.put("status",500);
        }else {
            result.put("status",200);
            result.put("data",orderId);
        }
        return result;
    }
    //订单支付后跳转到成功页面
    @RequestMapping(value="success",method = RequestMethod.GET)
    public ModelAndView success(@RequestParam("id")String orderId){
        ModelAndView mv = new ModelAndView("success");
        Order order = this.orderService.queryByOrderId(orderId);
        mv.addObject("order",order);
        //送货时间，当前时间向后退后2天，格式：08月19日
        mv.addObject("date",new DateTime().plusDays(2).toString("MM月dd日"));
        return mv;
    }
}
