package cn.e3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3.domain.TbItem;
import cn.e3.domain.TbItemDesc;
import cn.e3.service.ItemService;
import cn.e3.utils.DatagridPageBean;
import cn.e3.utils.E3mallResult;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;
    /**
     * 需求：根据id查询商品信息
     * 请求：/item/{11111}
     * 参数：Long itemId
     * 返回值：TbItem
     * 方法：findTbItemById
     * @param itemId
     * @return
     */
    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem findTbItemById(@PathVariable Long itemId){
        TbItem tbItem = itemService.findTbItemById(itemId);
        return tbItem;
    }
    
    /**
     * 需求：分页查询商品列表
     * 请求：/item/list
     * 参数：Integer page,Integer rows
     * 返回值：DagegridPageBean
     */
    @RequestMapping("/item/list")
    @ResponseBody
    public DatagridPageBean findItemList(@RequestParam(defaultValue="1") Integer page,
            @RequestParam(defaultValue="30") Integer rows){
        //调用远程service服务对象方法，查询商品分页列表
        DatagridPageBean pageBean = itemService.findItemLIst(page, rows);
        return pageBean;
    }
    
    @RequestMapping("/item/save")
    @ResponseBody
    public E3mallResult saveItem(TbItem tbItem,TbItemDesc tbItemDesc){
        E3mallResult e3mallResult = itemService.saveTbItem(tbItem, tbItemDesc);
        return e3mallResult;
    }
}
