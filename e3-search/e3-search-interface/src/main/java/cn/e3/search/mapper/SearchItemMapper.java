package cn.e3.search.mapper;

import java.util.List;

import cn.e3.search.pojo.SearchItem;

public interface SearchItemMapper {
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
    public List<SearchItem> importSolrIndexWithDatabase();
}
