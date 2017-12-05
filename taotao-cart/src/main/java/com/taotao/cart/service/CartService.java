package com.taotao.cart.service;

import com.github.abel533.entity.Example;
import com.taotao.cart.bean.Item;
//import com.taotao.cart.bean.User;
import com.taotao.cart.mapper.CartMapper;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.threadlocal.UserThreadLocal;
import com.taotao.sso.query.bean.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import java.util.Date;
import java.util.List;

/**
 * @author liu_mw
 * @date 2017/12/3 10:05
 */
@Service
public class CartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ItemService itemService;

    public void addItemToCart(Long itemId) {
        //判断商品在购物车中是否存在
        User user = UserThreadLocal.get();
        Cart record = new Cart();
        record.setItemId(itemId);
        record.setUserId(user.getId());
        Cart cart = this.cartMapper.selectOne(record);
        if(null==cart){
            //购物车不存在商品
            cart = new Cart();
            cart.setItemId(itemId);
            cart.setUserId(user.getId());
            cart.setNum(1);//TODO 默认W为1
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            //通过后台系统查询商品信息
            Item item = this.itemService.queryById(itemId);
            cart.setItemImage(StringUtils.split(item.getImage(),",")[0]);
            cart.setItemPrice(item.getPrice());
            cart.setItemTitle(item.getTitle());
            this.cartMapper.insert(cart);
        }else{
            //购物车有商品
            cart.setNum(cart.getNum() + 1);//TODO 默认W为1
            cart.setUpdated(new Date());
            this.cartMapper.updateByPrimaryKey(cart);
        }
    }

    /**
     * 查询购物车中的商品数据
     * @return
     */
    public List<Cart> queryCartList(Long userId) {
        Example example = new Example(Cart.class);
        example.setOrderByClause("created Desc");
        example.createCriteria().andEqualTo("userId",userId);
        return this.cartMapper.selectByExample(example);
    }
    /**
     * 查询购物车中的商品数据
     * @return
     */
    public List<Cart> queryCartList() {
        /*Example example = new Example(Cart.class);
        example.setOrderByClause("created Desc");
        //TODO 分页查询
        User user = UserThreadLocal.get();
        example.createCriteria().andEqualTo("userId",UserThreadLocal.get().getId());
        return this.cartMapper.selectByExample(example);*/
        return this.queryCartList(UserThreadLocal.get().getId());
    }

    public void updateNum(Long itemId, Integer num) {
        //更新的数据
        Cart cart = new Cart();
        cart.setNum(num);
        cart.setUpdated(new Date());
        //更新的条件
        Example example = new Example(Cart.class);
        example.createCriteria().andEqualTo("itemId",itemId)
                .andEqualTo("userId",UserThreadLocal.get().getId());
        this.cartMapper.updateByExampleSelective(cart,example);
    }

    public void deleteItem(Long itemId) {
        Cart cart = new Cart();
        cart.setItemId(itemId);
        cart.setUserId(UserThreadLocal.get().getId());
        this.cartMapper.delete(cart);
    }
}
