package cn.e3.service;

import cn.e3.domain.TbItem;
import cn.e3.domain.TbItemDesc;
import cn.e3.utils.DatagridPageBean;
import cn.e3.utils.E3mallResult;

public interface ItemService {
    /**
     * 需求：根据id查询商品信息
     * 参数：Long itemId
     * 返回值：TbItem
     * 方法名：findTbItemById
     */
    public TbItem findTbItemById(Long itemId);
    
    /**
     * 需求：分页查询商品列表
     * 参数：Integer page,Integer rows
     * 返回值：DatagridPageBean
     * 方法名：findItemList
     */
    public DatagridPageBean findItemLIst(Integer page,Integer rows);
    
    /**
     * 需求:添加商品信息
     * 参数:TbItem tbItem,TbItemDesc tbItemDesc
     * 返回值:E3mallResult
     */
    public E3mallResult saveTbItem(TbItem tbItem,TbItemDesc tbItemDesc);
}
