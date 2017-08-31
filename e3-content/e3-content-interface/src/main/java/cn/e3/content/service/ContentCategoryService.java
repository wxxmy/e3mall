package cn.e3.content.service;

import java.util.List;

import cn.e3.utils.TreeNode;

public interface ContentCategoryService {
    /**
     * 需求:根据父id返回easyUI树节点
     * 参数:parentId
     * 返回值TreeNode
     */
    public List<TreeNode> findContentCategoryByParentId(Long parentId);
}
