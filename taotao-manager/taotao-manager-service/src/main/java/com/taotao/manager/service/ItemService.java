package com.taotao.manager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.common.service.ApiService;
import com.taotao.manager.mapper.ItemMapper;
import com.taotao.manager.pojo.Item;
import com.taotao.manager.pojo.ItemDesc;
import com.taotao.manager.pojo.ItemParamItem;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemService extends BaseService<Item>{
    @Autowired
    private ItemDescService itemDescService;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemParamItemService itemParamItemService;
    @Autowired
    private ApiService apiService;
    @Value("${TAOTAO_WEB_URL}")
    private String TAOTAO_WEB_URL;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Boolean saveItem(Item item, String desc,String itemParams) {
        item.setStatus(1);
        item.setId(null);
        Integer count1 = super.save(item );

        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        Integer count2 = this.itemDescService.save(itemDesc);
        
        //保存商品规格参数数据
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(itemParams);
        Integer count3 = this.itemParamItemService.save(itemParamItem);

        //发送消息到mq的交换机
        sendMsg(item.getId(),"insert");
        return count1.intValue() ==1 && count2.intValue()==1 && count3.intValue()==1;

    }

    public EasyUIResult queryItemList(Integer page, Integer rows) {
        //设置分页参数
        PageHelper.startPage(page,rows);
        Example example = new Example(Item.class);
        example.setOrderByClause("updated DESC");
        List<Item> list = this.itemMapper.selectByExample(example);
        PageInfo<Item> pageInfo = new PageInfo<Item>(list);
        return new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());
    }

    public Boolean updateItem(Item item, String desc,String itemParams) {
        //强制设置不能更新的字段null;
        item.setCreated(null);
        item.setStatus(null);
        Integer count1 = super.updateSelective(item);
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        Integer count2 = this.itemDescService.updateSelective(itemDesc);
        Integer count3 = this.itemParamItemService.updateItemParamItem(item.getId(),itemParams);
        //通知其他系统该商品已经更新(前台系统)
        /*try {
            String url = TAOTAO_WEB_URL + "/item/cache/" + item.getId() + ".html";
            this.apiService.doPost(url);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //第二种方案
        sendMsg(item.getId(),"update");

        return count1.intValue()==1&&count2.intValue()==1&&count3.intValue()==1;
    }

    private void sendMsg(Long itemId,String type){
        try {
            Map<String, Object> msg = new HashMap<String, Object>();
            msg.put("type", type);
            msg.put("itemId", itemId);
            msg.put("date", System.currentTimeMillis());
            this.rabbitTemplate.convertAndSend("item." + type, MAPPER.writeValueAsString(msg));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
