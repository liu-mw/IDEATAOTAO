package com.taotao.manager.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.taotao.common.bean.ItemCatResult;
import com.taotao.manager.pojo.ItemCat;
import com.taotao.manager.service.ItemCatService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author liu_mw
 * @date 2017/11/21 20:58
 */
@RequestMapping("api/item/cat")
@Controller
public class ApiItemCatController {
    @Autowired
    private ItemCatService itemCatService;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    //第一种处理jsonp方式
    /*@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> queryItemCat(
            @RequestParam(value = "callback",required = false) String callback){
        try {
            ItemCatResult itemCatResult = this.itemCatService.queryAllToTree();
            if(null==itemCatResult){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            String result = MAPPER.writeValueAsString(itemCatResult);
            if(StringUtils.isEmpty(callback)){
                //无需跨域支持
                return ResponseEntity.ok(result);
            }else{
                return ResponseEntity.ok(callback+"("+result+");");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }*/
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ItemCatResult> queryItemCatList(){
        try{
            ItemCatResult itemCatResult = this.itemCatService.queryAllToTree();
            if(null == itemCatResult){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemCatResult);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
