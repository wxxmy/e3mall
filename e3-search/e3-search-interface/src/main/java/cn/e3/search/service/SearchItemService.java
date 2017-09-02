package cn.e3.search.service;

import cn.e3.search.pojo.SolrPageBean;
import cn.e3.utils.E3mallResult;

public interface SearchItemService {
    /**
     * 需求:通过数据库获得solr索引对象
     * 参数:无
     * 返回值:包装类对象SearchItem,
     *              包含域字段:Long id;
                                    private String title;
                                    private String sell_point;
                                    private Long price;
                                    private String image;
                                    private String category_name;
     */
    public E3mallResult importSolrIndexWithDatabase();
    
    /**
     * 需求:通过表现层传过来的qName,Integer page,Integer rows返回SolrPageBean对象
     * 参数:String qName,Integer page,Integer rows
     * 返回值:SolrPageBean
     */
    public SolrPageBean getSolrIndexWithCondition(String qName,Integer page,Integer rows);
}
