package cn.e3.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3.domain.TbItem;
import cn.e3.domain.TbItemDesc;
import cn.e3.domain.TbItemExample;
import cn.e3.mapper.TbItemDescMapper;
import cn.e3.mapper.TbItemMapper;
import cn.e3.service.ItemService;
import cn.e3.utils.DatagridPageBean;
import cn.e3.utils.E3mallResult;
import cn.e3.utils.IDUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Service
public class ItemServiceImpl implements ItemService {

    
    @Autowired
    private TbItemMapper tbItemMapper;
    
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    /**
     * 需求：根据id查询商品信息
     * 参数：Long itemId
     * 返回值：TbItem
     * 方法名：findTbItemById
     */
    @Override
    public TbItem findTbItemById(Long itemId) {
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        return tbItem;
    }
    /**
     * 需求：分页查询商品列表
     * 参数：Integer page,Integer rows
     * 返回值：DatagridPageBean
     * 方法名：findItemList
     * 业务：1.分页查询商品列表
     *            2.使用pagehelper分页查询进行分页查
     */
    @Override
    public DatagridPageBean findItemLIst(Integer page, Integer rows) {
        //创建example对象
        TbItemExample example = new TbItemExample();
        //在执行之前，设置分页查询
        PageHelper.startPage(page, rows);
        //执行查询
        List<TbItem> list = tbItemMapper.selectByExample(example);
        //创建分页插件提供PageInfo包装类对象,获得分页信息
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        //把分页数据封装分页包装类对象DagegridPageBean
        DatagridPageBean pageBean = new DatagridPageBean();
        //设置总条数
        pageBean.setTotal(pageInfo.getTotal());
        //设置分页列表数据
        pageBean.setRows(list);
        
        return pageBean;
    }
    /**
     * 需求：分页查询商品列表
     * 参数：Integer page,Integer rows
     * 返回值：DatagridPageBean
     * 方法名：findItemList
     */
    public E3mallResult saveTbItem(TbItem tbItem, TbItemDesc tbItemDesc) {
        // 生成商品id
        long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        //商品状态:1正常,2下架,3删除
        tbItem.setStatus((byte)1);
        //商品添加时间
        Date date = new Date();
        tbItem.setCreated(date);
        tbItem.setUpdated(date);
        //保存商品表
        tbItemMapper.insert(tbItem);
        
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
        tbItemDescMapper.insert(tbItemDesc);
        return E3mallResult.ok();
    }
    
  
}
