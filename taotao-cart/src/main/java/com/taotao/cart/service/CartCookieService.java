package com.taotao.cart.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.bean.Item;
import com.taotao.cart.pojo.Cart;
import com.taotao.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liu_mw
 * @date 2017/12/3 14:10
 */
@Service
public class CartCookieService {
    private static final String COOKIE_NAME = "TT_CART";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final Integer COOKIE_TIME = 60 * 60 * 24 * 30 * 12;
    @Autowired
    private ItemService itemService;
    /**
     * 添加商品到购物车
     * @param itemId
     * @param request
     * @param response
     */
    public void addItemToCart(Long itemId, HttpServletRequest request, HttpServletResponse response) throws Exception{
        /*String jsonData = CookieUtils.getCookieValue(request,COOKIE_NAME);
        List<Cart> carts = null;
        if(StringUtils.isEmpty(jsonData)){
            carts = new ArrayList<Cart>();
        }else{
            //反序列化
            carts = MAPPER.readValue(jsonData,MAPPER.getTypeFactory().constructCollectionType(List.class,Cart.class));
        }*/
        List<Cart> carts = this.queryCartList(request);
        Cart cart = null;
        for(Cart c:carts){
            if(c.getItemId().longValue()==itemId.longValue()){
                //该商品已在购物车
                cart = c;
                break;
            }
        }
        if(null==cart){
            //不存在
            cart = new Cart();
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            //商品基本数据查询
            Item item = this.itemService.queryById(itemId);
            cart.setItemId(itemId);
            cart.setItemTitle(item.getTitle());
            cart.setItemPrice(item.getPrice());
            cart.setItemImage(StringUtils.split(item.getImage(),",")[0]);
            cart.setNum(1);//TODO
            //商品加入购物车
            carts.add(cart);
        }else{
            //存在
            cart.setNum(cart.getNum()+1);//TODO 默认是1
            cart.setUpdated(new Date());
        }
        //将购物车数据写入到cookie中
        CookieUtils.setCookie(request,response,COOKIE_NAME,MAPPER.writeValueAsString(carts),COOKIE_TIME,true);
    }

    public List<Cart> queryCartList(HttpServletRequest request) throws Exception{
        String jsonData = CookieUtils.getCookieValue(request,COOKIE_NAME,true);
        List<Cart> carts = null;
        if(StringUtils.isEmpty(jsonData)){
            carts = new ArrayList<Cart>();
        }else{
            //反序列化
            carts = MAPPER.readValue(jsonData,MAPPER.getTypeFactory().constructCollectionType(List.class,Cart.class));
        }
        //TODO 排序
        return carts;
    }

    public void updateNum(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<Cart> carts = this.queryCartList(request);
        Cart cart = null;
        for(Cart c:carts){
            if(c.getItemId().longValue()==itemId.longValue()){
                //该商品已在购物车
                cart = c;
                break;
            }
        }
        if(cart != null) {
            cart.setNum(num);
            cart.setUpdated(new Date());
        }else{
            return;
        }
        //将数据写入到cooke中
        CookieUtils.setCookie(request,response,COOKIE_NAME,MAPPER.writeValueAsString(carts),true);
    }

    public void delete(Long itemId, HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<Cart> carts = this.queryCartList(request);
        Cart cart = null;
        for(Cart c:carts){
            if(c.getItemId().longValue()==itemId.longValue()){
                //该商品已在购物车
                cart = c;
                carts.remove(c);
                break;
            }
        }
        if(cart ==null){
            return;
        }
        //将数据写入到cooke中
        CookieUtils.setCookie(request,response,COOKIE_NAME,MAPPER.writeValueAsString(carts),true);
    }
}
