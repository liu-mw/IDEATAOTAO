package com.taotao.manager.service;

import com.github.abel533.mapper.Mapper;
import com.taotao.manager.mapper.ItemCatMapper;
import com.taotao.manager.pojo.ItemCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
