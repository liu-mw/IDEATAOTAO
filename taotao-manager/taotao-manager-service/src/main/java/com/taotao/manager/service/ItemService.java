package com.taotao.manager.service;

import com.github.abel533.mapper.Mapper;
import com.taotao.manager.mapper.ItemMapper;
import com.taotao.manager.pojo.Item;
import com.taotao.manager.pojo.ItemDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService extends BaseService<Item>{
    @Autowired
    private ItemDescService itemDescService;

    public Boolean saveItem(Item item, String desc) {
        item.setStatus(1);
        item.setId(null);
        Integer count1 = super.save(item );

        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        Integer count2 = this.itemDescService.save(itemDesc);
        return count1.intValue() ==1 && count2.intValue()==1;

    }
}
