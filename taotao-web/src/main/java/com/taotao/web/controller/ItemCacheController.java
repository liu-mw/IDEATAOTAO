package com.taotao.web.controller;

import com.taotao.common.service.RedisServiceOptimize;
import com.taotao.web.service.ItemService;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author liu_mw
 * @date 2017/11/26 9:27
 */
@RequestMapping("item/cache")
@Controller
public class ItemCacheController {
    @Autowired
    private RedisServiceOptimize redisServiceOptimize;
    /**
     * 接受商品id，删除对应的商品的缓存数据
     */
    @RequestMapping(value="{itemId}",method = RequestMethod.POST)
    public ResponseEntity<Void> deleteCache(@PathVariable("itemId")Long itemId){
        try {
            String key = ItemService.REDIS_KEY + itemId;
            this.redisServiceOptimize.del(key);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
