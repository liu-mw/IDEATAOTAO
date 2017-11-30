package com.taotao.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.web.beanData.Item;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liu_mw
 * @date 2017/11/30 10:52
 */
@Service
public class DataService {
    @Value("${TAOTAO_SOLR_URL}")
    private String TAOTAO_SOLR_URL;
    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;
    @Autowired
    private ApiService apiService;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public void importData() throws Exception{
        String solrUrl = TAOTAO_SOLR_URL + "/taotao"; //服务地址
        HttpSolrServer httpSolrServer = new HttpSolrServer(solrUrl); //定义solr的server
        httpSolrServer.setParser(new XMLResponseParser()); // 设置响应解析器
        httpSolrServer.setMaxRetries(1); // 设置重试次数，推荐设置为1
        httpSolrServer.setConnectionTimeout(500); // 建立连接的最长时间

        String url = TAOTAO_MANAGE_URL + "/rest/item?page={page}&rows=100";
        int page = 1;
        int pageSize = 0;
        do{
            String u = StringUtils.replace(url, "{page}", ""+page);
            System.out.println(u);
            String jsonData = this.apiService.doGet(u);
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            String rowsStr = jsonNode.get("rows").toString();
            List<Item> items = MAPPER.readValue(rowsStr,MAPPER.getTypeFactory().constructCollectionType(List.class,Item.class));
            pageSize = items.size();
            httpSolrServer.addBeans(items);
            httpSolrServer.commit();
            page++;
        }while(pageSize ==100);
    }
}
