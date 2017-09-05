package cn.e3.solr.cloud;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

public class MySolrCloud {
    
    /**
     * 需求:测试solr集群
     * @throws Exception 
     */
    @Test
    public void testSolrCloud() throws Exception{
        // 指定zookeeper配置服务中心地址
        String zkHost = "192.168.32.129:2182,192.168.32.129:2183,192.168.32.129:2184";
        // 创建solr集群服务对象,连接远程集群服务
        CloudSolrServer cloudSolrServer = new CloudSolrServer(zkHost);
        // 设置操作索引库
        cloudSolrServer.setDefaultCollection("item");
        // 创建封装查询对象参数对象
        SolrQuery solrQuery = new SolrQuery();
        // 设置查询参数
        solrQuery.setQuery("*:*");
        // 使用集群对象查询索引库
        QueryResponse response = cloudSolrServer.query(solrQuery);
        // 获得查询结果集文档对象
        SolrDocumentList results = response.getResults();
        // 获得总命中数
        long numFound = results.getNumFound();
        System.out.println(numFound);
        // 遍历,获得默认前10条
        for (SolrDocument solrDocument : results) {
            String title = (String) solrDocument.get("item_title");
            System.out.println(title);
        }
    }
}
