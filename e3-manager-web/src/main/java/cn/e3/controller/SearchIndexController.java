package cn.e3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3.search.service.SearchItemService;
import cn.e3.utils.E3mallResult;

@Controller
public class SearchIndexController {
    
    // 注入索引服务
    @Autowired
    private SearchItemService searchItemService;
    
    @RequestMapping("/search/dataImport")
    @ResponseBody
    public E3mallResult imporSolrIndexWithDatabase(){
        E3mallResult result = searchItemService.importSolrIndexWithDatabase();
        return result;
    }
}
