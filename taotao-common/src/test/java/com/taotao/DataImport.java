package com.taotao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author liu_mw
 * @date 2017/11/29 22:29
 */
public class DataImport {
    private HttpSolrServer httpSolrServer;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Autowired(required = false)
    private RequestConfig requestConfig;

    @Autowired
    private CloseableHttpClient httpClient;

    @Before
    public void setUp() throws Exception {
        // 在url中指定core名称：taotao
        //http://solr.taotao.com/#/taotao  -- 界面地址
        String url = "http://solr.taotao.com/taotao"; //服务地址
        HttpSolrServer httpSolrServer = new HttpSolrServer(url); //定义solr的server
        httpSolrServer.setParser(new XMLResponseParser()); // 设置响应解析器
        httpSolrServer.setMaxRetries(1); // 设置重试次数，推荐设置为1
        httpSolrServer.setConnectionTimeout(500); // 建立连接的最长时间

        this.httpSolrServer = httpSolrServer;
        this.httpClient = httpClient;
        this.requestConfig = requestConfig;
    }

    @Test
    public void testInsert() throws Exception{
        Item item = new Item();
        item.setCid(1L);
        item.setId(999L);
        item.setImage("image");
        item.setPrice(100L);
        item.setSellPoint("很好啊，赶紧来买吧.");
        item.setStatus(1);
        item.setTitle("飞利浦 老人手机 (X2560) 深情蓝 移动联通2G手机 双卡双待");
        this.httpSolrServer.addBean(item);
        this.httpSolrServer.commit();
    }

    @Test
    public void testUpdate() throws Exception{
        Item item = new Item();
        item.setCid(1L);
        item.setId(999L);
        item.setImage("image");
        item.setPrice(100L);
        item.setSellPoint("很好啊，赶紧来买吧. 豪啊");
        item.setStatus(1);
        item.setTitle("飞利浦 老人手机 (X2560) 深情蓝 移动联通2G手机 双卡双待");
        this.httpSolrServer.addBean(item);
        this.httpSolrServer.commit();
    }

    @Test
    public void testDelete() throws Exception{
        this.httpSolrServer.deleteById("999");
        this.httpSolrServer.commit();
    }

    @Test
    public void testQuery() throws Exception{
        int page = 2;
        int rows = 1;
        String keywords = "手机";
        SolrQuery solrQuery = new SolrQuery(); //构造搜索条件
        solrQuery.setQuery("title:" + keywords); //搜索关键词
        // 设置分页 start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
        solrQuery.setStart((Math.max(page, 1) - 1) * rows);
        solrQuery.setRows(rows);

        //是否需要高亮
        boolean isHighlighting = !StringUtils.equals("*", keywords) && StringUtils.isNotEmpty(keywords);

        if (isHighlighting) {
            // 设置高亮
            solrQuery.setHighlight(true); // 开启高亮组件
            solrQuery.addHighlightField("title");// 高亮字段
            solrQuery.setHighlightSimplePre("<em>");// 标记，高亮关键字前缀
            solrQuery.setHighlightSimplePost("</em>");// 后缀
        }

        // 执行查询
        QueryResponse queryResponse = this.httpSolrServer.query(solrQuery);
        List<Item> items = queryResponse.getBeans(Item.class);
        if (isHighlighting) {
            // 将高亮的标题数据写回到数据对象中
            Map<String, Map<String, List<String>>> map = queryResponse.getHighlighting();
            for (Map.Entry<String, Map<String, List<String>>> highlighting : map.entrySet()) {
                for (Item item : items) {
                    if (!highlighting.getKey().equals(item.getId().toString())) {
                        continue;
                    }
                    item.setTitle(StringUtils.join(highlighting.getValue().get("title"), ""));
                    break;
                }
            }
        }

        for (Item item : items) {
            System.out.println(item);
        }
    }

    @Test
    public void testData() throws Exception{
        String url = "http://manager.taotao.com/rest/item?page={page}&rows=100";
        int page = 1;
        int pageSize = 0;
        do{
            String u = StringUtils.replace(url, "{page}", ""+page);
            System.out.println(u);
            String jsonData = doGet(url);
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            String rowsStr = jsonNode.get("rows").toString();
            List<Item> items = MAPPER.readValue(rowsStr,MAPPER.getTypeFactory().constructType(List.class,Item.class));
            pageSize = items.size();
            this.httpSolrServer.addBeans(items);
            this.httpSolrServer.commit();
            page++;
        }while(pageSize ==100);
    }

    private String doGet(String url) throws Exception{

        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }
}
