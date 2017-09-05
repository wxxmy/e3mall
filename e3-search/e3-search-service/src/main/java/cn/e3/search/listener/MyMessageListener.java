package cn.e3.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3.search.mapper.SearchItemMapper;
import cn.e3.search.pojo.SearchItem;
import cn.e3.search.service.SearchItemService;

public class MyMessageListener implements MessageListener{
    /**
     * 需求:监听新增商品传过来的id,根据id查询数据库,并将信息更新到索引库
     */
    // 注入
    @Autowired
    private SearchItemMapper searchItemMapper;
    
    // 注入solrServer
    @Autowired
    private SolrServer solrServer;
    
    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage m = (TextMessage) message;
            try {
                // 从消息总线中获得商品id
                Long itemId = Long.parseLong(m.getText());
                // 从数据库查询到包装类对象 
                SearchItem searchItem = searchItemMapper.updateSolrIndexWithDatabase(itemId);
                // 更新索引库
                // 获得文档对象
                SolrInputDocument doc = new SolrInputDocument();
                // 添加索引信息
                doc.addField("id", searchItem.getId());
                doc.addField("item_title", searchItem.getTitle());
                doc.addField("item_sell_point", searchItem.getSell_point());
                doc.addField("item_price", searchItem.getPrice());
                doc.addField("item_image", searchItem.getImage());
                doc.addField("item_category_name", searchItem.getCategory_name());
                doc.addField("item_desc", searchItem.getDesc());
                // 提交文档,写入索引库
                solrServer.add(doc);
                // 提交所有
                solrServer.commit();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
