package cn.e3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    /**
     * 需求：范文静态资源的时候，动态根据url跳转到相应的页面
     * @param page
     * @return
     */
    @RequestMapping("{page}")
    public String findPage(@PathVariable String page){
        return page;
    }
}
