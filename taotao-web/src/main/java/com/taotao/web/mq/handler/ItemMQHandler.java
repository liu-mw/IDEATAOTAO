package com.taotao.web.mq.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisServiceOptimize;
import com.taotao.web.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * @author liu_mw
 * @date 2017/12/1 12:24
 */
public class ItemMQHandler {
    @Autowired
    private RedisServiceOptimize redisServiceOptimize;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 删除缓存双排数据，完成数据同步
     *
     * @param msg
     */
    public void execute(String msg) {
        try {
            JsonNode jsonNode = MAPPER.readTree(msg);
            Long itemId = jsonNode.get("itemId").asLong();
            String key = ItemService.REDIS_KEY + itemId;
            this.redisServiceOptimize.del(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
