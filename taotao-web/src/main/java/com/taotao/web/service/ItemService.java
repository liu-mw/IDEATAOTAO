package com.taotao.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.service.RedisServiceOptimize;
import com.taotao.manager.pojo.ItemDesc;
import com.taotao.manager.pojo.ItemParamItem;
import com.taotao.web.bean.Item;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @author liu_mw
 * @date 2017/11/25 15:18
 */
@Service
public class ItemService {
    @Autowired
    private RedisServiceOptimize redisServiceOptimize;
    @Autowired
    private ApiService apiService;
    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    //前台也加缓存
    private static final String REDIS_KEY = "TAOTAO_WEB_ITEM_DETAIL_";
    private static final Integer REDIS_TIME = 60 * 60 * 24;

    /**
     * 根据商品Id查询商品数据
     * 通过后台系统提供的接口服务进行查询
     * @param itemId
     * @return
     */
    public Item queryItemById(Long itemId) {
        //添加缓存操作
        String key = REDIS_KEY + itemId;
        try {
            String cacheData = this.redisServiceOptimize.get(key);
            if(StringUtils.isNotEmpty(cacheData)){
                return MAPPER.readValue(cacheData,Item.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String url =TAOTAO_MANAGE_URL + "/rest/api/item/" +itemId;
        try {
            String jsonData = this.apiService.doGet(url);
            if(StringUtils.isEmpty(jsonData)){
                return null;
            }
            //把数据加入redis缓存,此处一定要try-catch，遇到错误后直接跳入到A处，
            // 后面的return MAPPER.readValue(jsonData,Item.class);不会运行，影响原有逻辑
            try {
                this.redisServiceOptimize.set(key,jsonData,REDIS_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //反序列化成对象
            return MAPPER.readValue(jsonData,Item.class);
        } catch (IOException e) {//A
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询商品描述信息
     * @param itemId
     * @return
     */
    public ItemDesc queryDescByItemId(Long itemId) {
        String url =TAOTAO_MANAGE_URL + "/rest/api/item/desc/" +itemId;
        try {
            String jsonData = this.apiService.doGet(url);
            if(StringUtils.isEmpty(jsonData)){
                return null;
            }
            //反序列化成对象
            return MAPPER.readValue(jsonData,ItemDesc.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询商品规格参数
     * @param itemId
     * @return
     */
    public String queryItemParamByItemId(Long itemId) {
        String url =TAOTAO_MANAGE_URL + "/rest/api/item/param/item/" +itemId;
        try {
            String jsonData = this.apiService.doGet(url);
            if(StringUtils.isEmpty(jsonData)){
                return null;
            }
            //反序列化成对象
            ItemParamItem itemParamItem = MAPPER.readValue(jsonData, ItemParamItem.class);
            String paramData = itemParamItem.getParamData();
            //解析json
            ArrayNode arrayNode = (ArrayNode) MAPPER.readTree(paramData);
            //stringBuilder放到方法内部不会有线程安全问题，放到全局变量的话就有线程安全问题
            StringBuilder sb = new StringBuilder();
            sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"><tbody>");

            for(JsonNode param:arrayNode){
                sb.append("<tr><th class=\"tdTitle\" colspan=\"2\">"+param.get("group").asText()
                            +"</th></tr>");
                ArrayNode params = (ArrayNode) param.get("params");
                for(JsonNode p : params){
                    sb.append("<tr><td class=\"tdTitle\">"+p.get("k").asText()+"/td><td>"
                                +p.get("v").asText()+"</td></tr>");
                }
            }
            sb.append("</tbody></table>");
            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
