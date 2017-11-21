package com.taotao.manager.controller;

import com.taotao.common.bean.EasyUIResult;
import com.taotao.manager.pojo.Item;
import com.taotao.manager.pojo.ItemDesc;
import com.taotao.manager.service.ItemDescService;
import com.taotao.manager.service.ItemService;
import org.omg.PortableServer.REQUEST_PROCESSING_POLICY_ID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("item")
@Controller
//@Slog4j，下面的private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);可以省略直接用log.err,log.info方法输入日志
// 就行用了一个插件lombok插件该插件可以用于对象的set,get方法的简写
public class ItemController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemDescService itemDescService;
    @RequestMapping(method= RequestMethod.POST)
    public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc") String desc,
                                         @RequestParam("itemParams") String itemParams){
        try {
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("新增商品,item={},desc={}",item,desc);
            }
            if(StringUtils.isEmpty(item.getTitle())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            /*item.setStatus(1);
            item.setId(null);
            this.itemService.save(item );

            ItemDesc itemDesc = new ItemDesc();
            itemDesc.setItemId(item.getId());
            itemDesc.setItemDesc(desc);
            this.itemDescService.save(itemDesc);*/
            Boolean bool = this.itemService.saveItem(item,desc,itemParams);
            if(!bool){
                if(LOGGER.isInfoEnabled()){
                    LOGGER.info("新增商品失败,item={}",item);
                }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            if(LOGGER.isInfoEnabled()){
                LOGGER.info("新增商品成功,itemId={}",item.getId());
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
//            e.printStackTrace();
            LOGGER.error("新增商品失败！item="+item,e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    //查询商品列表
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemList(
            @RequestParam(value="page",defaultValue = "1") Integer page,
            @RequestParam(value="rows",defaultValue = "30") Integer rows){
        try{
            return ResponseEntity.ok(this.itemService.queryItemList(page,rows));
        }catch (Exception e){
            LOGGER.error("查询商品列表出错！page="+page+",rows="+rows,e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    //编辑单件商品
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItem(Item item,@RequestParam("desc") String desc,
                                           @RequestParam("itemParams") String itemParams){
        try {
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("编辑商品,item={},desc={}",item,desc);
            }
            if(StringUtils.isEmpty(item.getTitle())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            Boolean bool = this.itemService.updateItem(item,desc,itemParams);
            if(!bool){
                if(LOGGER.isInfoEnabled()){
                    LOGGER.info("编辑商品失败,item={}",item);
                }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            if(LOGGER.isInfoEnabled()){
                LOGGER.info("编辑商品成功,itemId={}",item.getId());
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
//            e.printStackTrace();
            LOGGER.error("编辑商品失败！item="+item,e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
