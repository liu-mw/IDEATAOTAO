package com.taotao.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liu_mw
 * @date 2017/11/24 19:25
 */
@Service
public class IndexService {
    @Value("${INDEX_AD1_URL}")
    private String INDEX_AD1_URL;
    @Value("${INDEX_AD2_URL}")
    private String INDEX_AD2_URL;
    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;
    @Autowired
    private ApiService apiService;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public String queryIndexAD1(){
        try {
            String url = TAOTAO_MANAGE_URL+INDEX_AD1_URL;
            String jsonData = this.apiService.doGet(url);
            if(null == jsonData){
                return null;
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            ArrayNode rows = (ArrayNode) jsonNode.get("rows");
            //定义成map<String,Object>类型相当于是把map<String,Object>当做是一个object对象
            List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
            for(JsonNode row :rows){
                //用于排列序
                Map<String,Object> map = new LinkedHashMap<String,Object>();
                map.put("srcB",row.get("pic").asText());
                map.put("height",240);
                map.put("alt",row.get("title").asText());
                map.put("width",670);
                map.put("src",row.get("pic").asText());
                map.put("widthB",550);
                map.put("href",row.get("url").asText());
                map.put("height",240);
                result.add(map);
            }
            return MAPPER.writeValueAsString(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String queryIndexAD2() {
        try {
            String url = TAOTAO_MANAGE_URL+INDEX_AD2_URL;
            String jsonData = this.apiService.doGet(url);
            if(null == jsonData){
                return null;
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            ArrayNode rows = (ArrayNode) jsonNode.get("rows");
            //定义成map<String,Object>类型相当于是把map<String,Object>当做是一个object对象
            List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
            for(JsonNode row :rows){
                //用于排列序
                Map<String,Object> map = new LinkedHashMap<String,Object>();
                map.put("width",310);
                map.put("height",70);
                map.put("src",row.get("pic").asText());
                map.put("href",row.get("url").asText());
                map.put("alt",row.get("title").asText());
                map.put("widthB",210);
                map.put("height",70);
                map.put("srcB",row.get("pic").asText());
                result.add(map);
            }
            return MAPPER.writeValueAsString(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
