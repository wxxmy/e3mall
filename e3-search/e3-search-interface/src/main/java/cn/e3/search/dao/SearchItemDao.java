package cn.e3.search.dao;

import org.apache.solr.client.solrj.SolrQuery;

import cn.e3.search.pojo.SolrPageBean;

public interface SearchItemDao {
    
    /**
     * 需求:通过查询索引库,获得查询结果的包装类对象集合,再传递给业务层
     * 参数:SolrQuery solrQuery
     * 返回值:分页PageBean
     */
    public SolrPageBean getSolrIndexWithCondition(SolrQuery solrQuery);
}
