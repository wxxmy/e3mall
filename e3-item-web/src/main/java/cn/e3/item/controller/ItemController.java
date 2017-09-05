package cn.e3.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3.domain.TbItem;
import cn.e3.domain.TbItemDesc;
import cn.e3.service.ItemService;

@Controller
public class ItemController {
    
    @Autowired
    private ItemService itemService;
    /**
     * 需求:根据商品id跳转到相应的详情页面
     * http://localhost:8086/${item.id }.html
     * 
     */
    @RequestMapping("{ItemId}")
    public String showItemDetail(@PathVariable Long ItemId,Model model){
        
        // 根据id查询商品详情
        TbItem tbItem = itemService.findTbItemById(ItemId);
        model.addAttribute("item", tbItem);
        // 根据id查询商品描述
        TbItemDesc tbItemDesc = itemService.findTbItemDescByTbItemId(ItemId);
        model.addAttribute("itemDesc", tbItemDesc);
        return "item";
    }
}
