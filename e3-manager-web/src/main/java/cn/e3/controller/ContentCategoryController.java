package cn.e3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3.content.service.ContentCategoryService;
import cn.e3.utils.TreeNode;

@Controller
public class ContentCategoryController {
    
    @Autowired
    private ContentCategoryService contentCategoryService;
    /**
     * 需求:根据parentId获得List<TreeNode>集合对象
     * 参数
     * 返回值
     */
    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<TreeNode> findContentCatogoryTreeNodeByParentId(@RequestParam(defaultValue="0",value="id") Long parentId){
        
        List<TreeNode> list = contentCategoryService.findContentCategoryByParentId(parentId);
        return list;
    }
    
    
}
