package com.taotao.manager.service;

import com.taotao.manager.mapper.ItemCatMapper;
import com.taotao.manager.pojo.ItemCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemCatService {
    @Autowired
    private ItemCatMapper itemCatMapper;
    public List<ItemCat> queryItemCatListByParentId(Long pid){
        ItemCat record = new ItemCat();
        record.setParentId(pid);
        return itemCatMapper.select(record);
    }
}
