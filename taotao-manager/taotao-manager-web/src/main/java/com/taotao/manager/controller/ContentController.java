package com.taotao.manager.controller;

import com.taotao.common.bean.EasyUIResult;
import com.taotao.manager.pojo.Content;
import com.taotao.manager.pojo.ContentCategory;
import com.taotao.manager.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author liu_mw
 * @date 2017/11/22 19:58
 */
@RequestMapping("content")
@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;

    /**
     * 保存内容信息
     * @param content
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveContent(Content content){
        try {
            content.setId(null);
            this.contentService.save(content);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryListByCategoryId(@RequestParam("categoryId") Long categoryId,
              @RequestParam(value="page",defaultValue = "1") Integer page,
                @RequestParam(value="rows",defaultValue ="10") Integer rows){
        try {
            EasyUIResult easyUIResult = this.contentService.queryListBycategoryId(categoryId,page,rows);
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
