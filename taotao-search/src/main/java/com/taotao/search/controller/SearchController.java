package com.taotao.search.controller;

import com.taotao.search.bean.Item;
import com.taotao.search.bean.SearchResult;
import com.taotao.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author liu_mw
 * @date 2017/11/30 14:54
 */
@Controller
public class SearchController {
    private static final Integer ROWS = 32;
    @Autowired
    private ItemSearchService itemSearchService;

    /**
     * 关键字查询
     * @param keywords
     * @param page
     * @return
     */
    @RequestMapping(value = "search",method = RequestMethod.GET)
    public ModelAndView search(@RequestParam("q")String keywords,@RequestParam(value="page",defaultValue = "1") Integer page){
        ModelAndView mv = new ModelAndView("search");
        List<Item> itemList = null;
        SearchResult searchResult = null;
        try {
            keywords = new String(keywords.getBytes("ISO_8859-1"),"UTF-8");
            searchResult = this.itemSearchService.searchItem(keywords,page,ROWS);
        } catch (Exception e) {
            e.printStackTrace();
            searchResult = new SearchResult(0L,null);
        }
        //搜索关键字
        mv.addObject("query",keywords);
        //搜索的结果数据
        mv.addObject("itemList",searchResult.getList());
        //页数
        mv.addObject("page",page);
        int total = searchResult.getTotal().intValue();
        int pages = total % ROWS == 0 ? total/ROWS : total/ROWS +1;
        //总页数
        mv.addObject("pages",pages);
        return mv;
    }
}
