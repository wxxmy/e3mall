package cn.e3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3.service.ItemCatService;
import cn.e3.utils.TreeNode;

@Controller
public class ItemCatController {
    
    @Autowired
    private ItemCatService itemCatService;
    
    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<TreeNode> findTreeNodesWithItemCat(@RequestParam(value="id",defaultValue="0") Long parentId){
        List<TreeNode> list = itemCatService.findItemCatList(parentId);
        return list;
    }
}
