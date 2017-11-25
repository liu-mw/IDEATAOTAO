package com.taotao.manager.controller.api;

import com.taotao.manager.pojo.ItemParamItem;
import com.taotao.manager.service.ItemParamItemService;
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
@RequestMapping("api/item/param/item")
@Controller
public class ApiItemParamController {
    /**
     * 根据商品Id查找商品规格参数数据
     */
    @Autowired
    private ItemParamItemService itemParamItemService;
    @RequestMapping(value="{itemId}",method = RequestMethod.GET)
    public ResponseEntity<ItemParamItem> queryByItemId(@PathVariable("itemId") Long itemId){
        try {
            ItemParamItem record = new ItemParamItem();
            record.setItemId(itemId);
            ItemParamItem itemParamItem = this.itemParamItemService.queryOne(record);
            if(null==itemParamItem){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemParamItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
