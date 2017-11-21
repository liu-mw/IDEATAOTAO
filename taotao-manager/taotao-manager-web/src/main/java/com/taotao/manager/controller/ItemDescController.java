package com.taotao.manager.controller;

import com.taotao.manager.pojo.ItemDesc;
import com.taotao.manager.service.ItemDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("item/desc")
@Controller
public class ItemDescController {
    @Autowired
    private ItemDescService itemDescService;
    /**
     * 根据商品Id查询商品描述的数据
     */
    @RequestMapping(value="{itemId}",method = RequestMethod.GET)
    public ResponseEntity<ItemDesc> queryByItemId(@PathVariable("itemId") Long itemId){
        try {
            ItemDesc itemDesc = this.itemDescService.queryById(itemId);
            if(null==itemDesc){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemDesc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
