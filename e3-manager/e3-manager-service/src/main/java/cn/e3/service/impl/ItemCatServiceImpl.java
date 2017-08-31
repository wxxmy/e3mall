package cn.e3.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3.domain.TbItemCat;
import cn.e3.domain.TbItemCatExample;
import cn.e3.domain.TbItemCatExample.Criteria;
import cn.e3.mapper.TbItemCatMapper;
import cn.e3.service.ItemCatService;
import cn.e3.utils.TreeNode;
@Service
public class ItemCatServiceImpl implements ItemCatService {
    
    @Autowired
    private TbItemCatMapper itemCatMapper;
    /**
     * 需求：获得easyUI的Tree需要的节点集合对象
     */
    public List<TreeNode> findItemCatList(Long parentId) {
        // 1.创建集合，封装树形列表
        List<TreeNode> treeNodes = new ArrayList<>();
        
        // 2.根据父id查询商品类别
        // 创建example对象：类似hibernate的qbc查询
        TbItemCatExample example = new TbItemCatExample();
        Criteria createCriteria = example.createCriteria();
        // 3.设置查询参数paren_id
        createCriteria.andParentIdEqualTo(parentId);
        
        // 4.获得TbItemCat集合
        List<TbItemCat> list = itemCatMapper.selectByExample(example);
        
        // 循环查询列表，封装节点信息
        for (TbItemCat tbItemCat : list) {
            TreeNode treeNode = new TreeNode();
            // 设置节点id
            treeNode.setId(tbItemCat.getId().intValue());
            // 设置节点名字
            treeNode.setText(tbItemCat.getName());
            // 设置节点状态
            treeNode.setState(tbItemCat.getIsParent()?"closed":"open");
            
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }

}
