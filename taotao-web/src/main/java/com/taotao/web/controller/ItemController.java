package com.taotao.web.controller;
import com.taotao.manager.pojo.ItemDesc;
import com.taotao.web.bean.Item;
import com.taotao.web.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author liu_mw
 * @date 2017/11/25 15:12
 */
@RequestMapping("item")
@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;
    @RequestMapping(value="{itemId}",method = RequestMethod.GET)
    public ModelAndView showDetail(@PathVariable("itemId") Long itemId){
        ModelAndView mv = new ModelAndView("item");
        //商品的展示数据
        Item item = this.itemService.queryItemById(itemId);
        mv.addObject("item",item);
        //商品的描述数据就是介绍
        ItemDesc itemDesc = this.itemService.queryDescByItemId(itemId);
        mv.addObject("itemDesc",itemDesc);
        //商品规格参数数据
        String itemParam = this.itemService.queryItemParamByItemId(itemId);
        mv.addObject("itemParam",itemParam);
        return mv;
    }
}
