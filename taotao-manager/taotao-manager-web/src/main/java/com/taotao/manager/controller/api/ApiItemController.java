package com.taotao.manager.controller.api;

import com.taotao.manager.pojo.Item;
import com.taotao.manager.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author liu_mw
 * @date 2017/11/25 15:25
 */
@Controller
@RequestMapping("api/item")
public class ApiItemController {
    @Autowired
    private ItemService itemService;

    /**
     * 根据商品id查询商品数据
     * @param itemId
     * @return
     */
    @RequestMapping(value="{itemId}",method = RequestMethod.GET)
    public ResponseEntity<Item> queryById(@PathVariable("itemId" ) Long itemId){
        try {
            Item item = this.itemService.queryById(itemId);
            if(null==item){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
