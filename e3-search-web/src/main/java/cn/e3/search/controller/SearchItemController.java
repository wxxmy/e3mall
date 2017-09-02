package cn.e3.search.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3.search.pojo.SolrPageBean;
import cn.e3.search.service.SearchItemService;

@Controller
public class SearchItemController {

    @Autowired
    private SearchItemService searchItemService;
    
    @RequestMapping("/search")
    public String getSolrIndexWithCondition(@RequestParam(value="q")String qName,
            @RequestParam(defaultValue="1")Integer page,
            @RequestParam(defaultValue="60")Integer rows,Model model){
        
        
        try {
            // 对查询字段编解码
            qName = new String(qName.getBytes("ISO8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SolrPageBean pageBean = searchItemService.getSolrIndexWithCondition(qName, page, rows);
        // 回显查询关键字
        model.addAttribute("query", qName);
        
        // 回显当前页
        model.addAttribute("page", pageBean.getCurPage());
        
        // 回显总页数
        model.addAttribute("totalPages", pageBean.getTotalPage());
        
        // 回显商品列表
        model.addAttribute("itemList", pageBean.getsList());
        
        return "search";
    }
    
}
