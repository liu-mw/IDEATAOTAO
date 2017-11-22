package com.taotao.manager.controller;

import com.taotao.manager.pojo.ContentCategory;
import com.taotao.manager.service.ContentCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.naming.Context;
import java.util.List;

/**
 * @author liu_mw
 * @date 2017/11/22 19:58
 */
@RequestMapping("content/category")
@Controller
public class ContentCategoryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentCategoryController.class);
    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     *根据父节点id查询内容分类列表
     * @param parentId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ContentCategory>> queryContentCategoryList(
            @RequestParam(value = "id",defaultValue = "0") Long parentId){
        try {
            ContentCategory record = new ContentCategory();
            record.setParentId(parentId);
            List<ContentCategory> contentCategories = this.contentCategoryService.queryListByWhere(record);
            if(null==contentCategories||contentCategories.size()==0){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(contentCategories);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 新增子节点
     * @param contentCategory
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ContentCategory> saveContentCategory(ContentCategory contentCategory){
        try {
            this.contentCategoryService.saveContentCategory(contentCategory);
            LOGGER.debug("新增商品,contentCategory={}",contentCategory);
            return ResponseEntity.status(HttpStatus.CREATED).body(contentCategory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 删除子节点也包括孙子节点
     * @param contentCategory
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(ContentCategory contentCategory){
        try {
            this.contentCategoryService.deleteAll(contentCategory);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
