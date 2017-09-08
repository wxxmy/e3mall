package cn.e3.freemarker.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3.domain.TbItem;
import cn.e3.domain.TbItemDesc;
import cn.e3.service.ItemService;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;

public class FreeMarkerListener implements MessageListener{
    /**
     * 需求:接收消息,同步静态页面
     * 流程:
     * 1.数据
     * 2.api
     * 3.模版
     */
    // 注入freemarker配置中心
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    
    // 注入商品服务
    @Autowired
    private ItemService itemService;
    
    // 注入静态页面存放路径
    @Value("${STATIC_HTML_PATH}")
    private String STATIC_HTML_PATH;
    
    @Override
    public void onMessage(Message message) {
        try {
            Long itemId = null;
            // 1.获得配置对象
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            // 2. 获得模版
            Template template = configuration.getTemplate("item.ftl");
            // 3. 创建map集合
            Map<String, Object> maps = new HashMap<>();
            // 4. 从activeMQ消息队列中获得itemId
            if (message instanceof TextMessage) {
                TextMessage m = (TextMessage) message;
                itemId = Long.parseLong(m.getText());
            }
            // 4. 根据ItemId获得商品详情信息
            TbItem tbItem = itemService.findTbItemWithItemId(itemId);
            maps.put("item", tbItem);
            // 为了避免事务还未提交就查询,线程休眠1秒
            Thread.sleep(1000);
            // 5.根据ItemId获得商品描述信息
            TbItemDesc tbItemDesc = itemService.findTbItemDescWithItemId(itemId);
            maps.put("itemDesc", tbItemDesc);
            // 6. 指定模版地址
            Writer out = new FileWriter(new File(STATIC_HTML_PATH+itemId+".html"));
            template.process(maps, out);
            // 7.释放资源
            out.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
