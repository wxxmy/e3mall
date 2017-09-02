package cn.e3.search.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3.search.dao.SearchItemDao;
import cn.e3.search.pojo.SearchItem;
import cn.e3.search.pojo.SolrPageBean;
@Repository
public class SearchItemDaoImpl implements SearchItemDao {

    @Autowired
    private SolrServer solrServer;
    /**
     * 需求:通过查询索引库,获得查询结果的包装类对象集合,再传递给业务层
     * 参数:SolrQuery solrQuery
     * 返回值:分页PageBean
     */
    public SolrPageBean getSolrIndexWithCondition(SolrQuery solrQuery) {
        // 创建SolrPageBean对象
        SolrPageBean pageBean = new SolrPageBean();
        
        // 创建SearchItem集合
        List<SearchItem> sList = new ArrayList<>();
        try {
            // 注入solrServer
            QueryResponse response = solrServer.query(solrQuery);
            
            // 获得查询到的结果集
            SolrDocumentList results = response.getResults();
            
            // 获得总命中数
            Long numFound = results.getNumFound();
            // 设置给分页pageBean
            pageBean.setRecoredCount(numFound.intValue());
            
            // 遍历结果集,将查询到的信息分别封装到SearchItem对象中
            for (SolrDocument doc : results) {
                // 创建SearchItem对象
                SearchItem searchItem = new SearchItem();
                
                // 首先封装id
                String id = (String) doc.get("id");
                searchItem.setId(Long.parseLong(id));
                // 封装title
                String item_title = (String) doc.get("item_title");
                // 设置高亮
                Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
                Map<String, List<String>> map = highlighting.get(id);
                List<String> list = map.get("item_title");
                if (list!=null && list.size()>0) {
                    item_title = list.get(0);
                }
                // 把高亮设置商品回显
                searchItem.setTitle(item_title);
                // 封装卖点
                String item_sell_point = (String) doc.get("item_sell_point");
                searchItem.setSell_point(item_sell_point);
                // 封装价格
                Long item_price = (Long) doc.get("item_price");
                searchItem.setPrice(item_price);
                // 封装图片
                String item_image = (String) doc.get("item_image");
                searchItem.setImage(item_image);
                // 封装所属种类
                String item_category_name = (String) doc.get("item_category_name");
                searchItem.setCategory_name(item_category_name);
                // 封装描述信息
                String item_desc = (String) doc.get("item_desc");
                searchItem.setDesc(item_desc);
                
                // 封装包含完整信息的SearchItem对象
                sList.add(searchItem);
            }
            // 设置总记录
            pageBean.setsList(sList);
            
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return pageBean;
    }

}
