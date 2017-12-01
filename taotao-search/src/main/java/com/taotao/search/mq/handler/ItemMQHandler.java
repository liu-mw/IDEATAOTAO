package com.taotao.search.mq.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.search.bean.Item;
import com.taotao.search.service.ItemSearchService;
import com.taotao.search.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * @author liu_mw
 * @date 2017/12/1 14:19
 */
public class ItemMQHandler {
    @Autowired
    private ItemService itemService;
    @Autowired
    private HttpSolrServer httpSolrServer;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public void execute(String msg){
        try {
            JsonNode jsonNode = MAPPER.readTree(msg);
            Long itemId = jsonNode.get("itemId").asLong();
            String type = jsonNode.get("type").asText();
            if(StringUtils.equals(type,"insert") || StringUtils.equals(type,"update")){
                Item item = this.itemService.queryById(itemId);
                this.httpSolrServer.addBean(item);
                this.httpSolrServer.commit();
            }else if(StringUtils.equals(type,"delete")){
                this.httpSolrServer.deleteById(itemId.toString());
                this.httpSolrServer.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
