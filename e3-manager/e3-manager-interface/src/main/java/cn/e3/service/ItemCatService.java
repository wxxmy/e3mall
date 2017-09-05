package cn.e3.service;

import java.util.List;

import cn.e3.utils.TreeNode;

public interface ItemCatService {
    /**
     * 需求：获得商品类名表,获得easyUI所需要的Tree节点
     * @param parentId
     * @return
     */
    public List<TreeNode> findItemCatList(Long parentId);
}
