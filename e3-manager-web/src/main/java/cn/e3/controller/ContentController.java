package cn.e3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3.content.service.ContentService;
import cn.e3.domain.TbContent;
import cn.e3.utils.DatagridPageBean;
import cn.e3.utils.E3mallResult;


@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;
    
    /**
     * 需求:新增节点
     * @param parentId
     * @param name
     * @return
     */
    @RequestMapping("/content/category/create")
    @ResponseBody
    public E3mallResult createNode(Long parentId,String name){
        E3mallResult result = contentService.createNode(parentId, name);
        
        return result;
    }
    
    /**
     * 需求:查询广告类目(分页)
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/content/query/list")
    @ResponseBody
    public DatagridPageBean findContentListByCategoryId(Long categoryId,
            @RequestParam(defaultValue="1")Integer page,
            @RequestParam(defaultValue="30")Integer rows){
        DatagridPageBean pageBean = contentService.findContentListByCategoryId(categoryId, page, rows);
        
        return pageBean;
    }
    
    
    @RequestMapping(" /content/save")
    @ResponseBody
    public E3mallResult saveContent(TbContent tbContent){
        E3mallResult result = contentService.saveContent(tbContent);
        return result;
    }
}
