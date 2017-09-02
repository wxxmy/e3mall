package cn.e3.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3.search.dao.SearchItemDao;
import cn.e3.search.mapper.SearchItemMapper;
import cn.e3.search.pojo.SearchItem;
import cn.e3.search.pojo.SolrPageBean;
import cn.e3.search.service.SearchItemService;
import cn.e3.utils.E3mallResult;
@Service
public class SearchItemServiceImpl implements SearchItemService {
    // 注入SearchItemMapper
    @Autowired
    private SearchItemMapper searchItemMapper;
    
    // 注入solrServer
    @Autowired
    private SolrServer solrServer;
    
    // 注入SearchItemDao
    @Autowired
    private SearchItemDao searchItemDao;
    /**
     * 需求:通过数据库获得solr索引对象
     * 参数:无
     * 返回值:包装类对象E3mallResult
     *              域字段:Long id;
                                    private String title;
                                    private String sell_point;
                                    private Long price;
                                    private String image;
                                    private String category_name;
     */
    public E3mallResult importSolrIndexWithDatabase() {
        // 1.首先注入SearchItemMapper,需要依赖e3-search-interface
        try {
            List<SearchItem> list = searchItemMapper.importSolrIndexWithDatabase();
            // 2.循环商品包装类集合,将信息添加到索引文档对象中
     for (SearchItem searchItem : list) {
              SolrInputDocument doc = new SolrInputDocument();
              // 3.添加包装类信息
              doc.addField("id", searchItem.getId());
              doc.addField("item_title", searchItem.getTitle());
              doc.addField("item_sell_point", searchItem.getSell_point());
              doc.addField("item_price", searchItem.getPrice());
              doc.addField("item_image", searchItem.getImage());
              doc.addField("item_category_name", searchItem.getCategory_name());
              doc.addField("item_desc", searchItem.getDesc());
              solrServer.add(doc);
   }
         // 提交信息
         solrServer.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        return E3mallResult.ok();
    }
    /**
     * 需求:通过表现层传过来的qName,Integer page,Integer rows返回SolrPageBean对象
     * 参数:String qName,Integer page,Integer rows
     * 返回值:SolrPageBean
     */
    public SolrPageBean getSolrIndexWithCondition(String qName, Integer page,
            Integer rows) {
        // 创建solrQuery对象,封装查询条件
        SolrQuery solrQuery = new SolrQuery();
        if (qName!=null && !"".equals(qName)) {
            solrQuery.setQuery(qName);
        }else {
            solrQuery.setQuery("*:*");
        }
        
        // 设置分页
        int start = (page - 1)*rows;
        solrQuery.setStart(start);
        solrQuery.setRows(rows);
        
        // 设置高亮
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");
        
        //设置默认查询字段
        solrQuery.set("df", "item_keywords");
        
        SolrPageBean pageBean = searchItemDao.getSolrIndexWithCondition(solrQuery);
        
        // 设置当前页和总页数
        pageBean.setCurPage(page);
        Integer recoredCount =pageBean.getRecoredCount();
        Double totalPage = Math.ceil(1.0*recoredCount/rows);
        pageBean.setTotalPage(totalPage.intValue());
        /*Integer recordCount = pageBean.getRecoredCount();
        int pages = recordCount/rows;
        if(recordCount%rows>0){
            pages++;
        }
        pageBean.setTotalPage(pages);*/
        return pageBean;
    }

}
