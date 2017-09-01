package cn.e3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3.content.jedis.JedisService;
import cn.e3.content.service.ContentService;
import cn.e3.utils.AdItem;
import cn.e3.utils.JsonUtils;

@Controller
public class PageController {
    
    @Value("${BIG_AD_ID}")
    private Long BIG_AD_ID;
    
    // 注入内容服务
    @Autowired
    private ContentService contentService;
    
    // 注入jedisService服务
    @Autowired
    private JedisService jedisService;
    /**
     * 需求:页面一加载就显示大广告图片
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String showIndex(Model model){
        
        List<AdItem> list = contentService.findAdItemListWithCategoryId(BIG_AD_ID);
        String adJson = JsonUtils.objectToJson(list);
        // 对象转换成json
        model.addAttribute("ad1", adJson);
        return "index";
    }
}
