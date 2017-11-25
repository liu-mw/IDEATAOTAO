package com.taotao.manager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.ItemCatData;
import com.taotao.common.bean.ItemCatResult;
import com.taotao.common.service.RedisServiceOptimize;
import com.taotao.manager.pojo.ItemCat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemCatService extends BaseService<ItemCat>{
    //该行代码已经在baseService通过泛型注入的方式实现了
    /*@Autowired
    private ItemCatMapper itemCatMapper;

    @Override
    public Mapper<ItemCat> getMapper() {
        return this.itemCatMapper;
    }*/

    /*public List<ItemCat> queryItemCatListByParentId(Long pid){
        ItemCat record = new ItemCat();
        record.setParentId(pid);
        return itemCatMapper.select(record);
    }*/
    @Autowired
    private RedisServiceOptimize redisServiceOptimize;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String REDIS_KEY = "TAOTAO_MANAGER_ITEM_CAT_ALL";
    private static final Integer REDIS_TIME= 60 * 60 * 24 * 30 * 3;
    /**
     * 全部查询，并且生成树状结构
     *
     * @return
     */
    public ItemCatResult queryAllToTree() {
        //try-catch这样写为了不影响原有逻辑，即使报错也执行原有逻辑
        try {
            //从缓存中命中
            //String key = "TAOTAO_MANAGER_ITEM_CAT_ALL";//定义key的规则：项目名_模块名__业务名
            String cacheData = this.redisServiceOptimize.get(REDIS_KEY);
            if(StringUtils.isNoneEmpty(cacheData)){
                    //把String类型反序列话成对象
                    return MAPPER.readValue(cacheData,ItemCatResult.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ItemCatResult result = new ItemCatResult();
        // 全部查出，并且在内存中生成树形结构
        List<ItemCat> cats = super.queryAll();

        // 转为map存储，key为父节点ID，value为数据集合
        Map<Long, List<ItemCat>> itemCatMap = new HashMap<Long, List<ItemCat>>();
        for (ItemCat itemCat : cats) {
            if (!itemCatMap.containsKey(itemCat.getParentId())) {
                itemCatMap.put(itemCat.getParentId(), new ArrayList<ItemCat>());
            }
            itemCatMap.get(itemCat.getParentId()).add(itemCat);
        }

        // 封装一级对象
        List<ItemCat> itemCatList1 = itemCatMap.get(0L);
        for (ItemCat itemCat : itemCatList1) {
            ItemCatData itemCatData = new ItemCatData();
            itemCatData.setUrl("/products/" + itemCat.getId() + ".html");
            itemCatData.setNname("<a href='" + itemCatData.getUrl() + "'>" + itemCat.getName() + "</a>");
            result.getItemCats().add(itemCatData);
            if (!itemCat.getIsParent()) {
                continue;
            }

            // 封装二级对象
            List<ItemCat> itemCatList2 = itemCatMap.get(itemCat.getId());
            List<ItemCatData> itemCatData2 = new ArrayList<ItemCatData>();
            itemCatData.setItems(itemCatData2);
            for (ItemCat itemCat2 : itemCatList2) {
                ItemCatData id2 = new ItemCatData();
                id2.setNname(itemCat2.getName());
                id2.setUrl("/products/" + itemCat2.getId() + ".html");
                itemCatData2.add(id2);
                if (itemCat2.getIsParent()) {
                    // 封装三级对象
                    List<ItemCat> itemCatList3 = itemCatMap.get(itemCat2.getId());
                    List<String> itemCatData3 = new ArrayList<String>();
                    id2.setItems(itemCatData3);
                    for (ItemCat itemCat3 : itemCatList3) {
                        itemCatData3.add("/products/" + itemCat3.getId() + ".html|" + itemCat3.getName());
                    }
                }
            }
            if (result.getItemCats().size() >= 14) {
                break;
            }
        }

        try {
            this.redisServiceOptimize.set(REDIS_KEY,MAPPER.writeValueAsString(result),REDIS_TIME);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
