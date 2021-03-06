package com.taotao.web.controller;


import com.taotao.web.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @author liu_mw
 * @date 2017/11/30 10:49
 */
@Controller
public class DataController {

    @Autowired
    private DataService dataService;
    /**
     * 数据导入solr服务
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "importData",method = RequestMethod.GET)
    public ResponseEntity<String> importData() throws Exception{

        this.dataService.importData();
        return ResponseEntity.status(HttpStatus.CREATED).body("import data ok");
    }
}
