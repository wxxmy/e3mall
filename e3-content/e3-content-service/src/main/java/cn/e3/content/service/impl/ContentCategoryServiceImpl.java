package cn.e3.content.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3.content.service.ContentCategoryService;
import cn.e3.domain.TbContentCategory;
import cn.e3.domain.TbContentCategoryExample;
import cn.e3.domain.TbContentCategoryExample.Criteria;
import cn.e3.mapper.TbContentCategoryMapper;
import cn.e3.utils.TreeNode;
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    //注入TbContentCategoryMapper接口
    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;
    
    /**
     * 需求:根据父id返回easyUI树节点
     * 参数:parentId
     * 返回值TreeNode
     * 发布服务
     */
    @Override
    public List<TreeNode> findContentCategoryByParentId(Long parentId) {
        //创建List<TreeNode>对象
        List<TreeNode> treeNodes = new ArrayList<>();
        
        //获得example对象
        TbContentCategoryExample example = new TbContentCategoryExample();
        Criteria createCriteria = example.createCriteria();
        createCriteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        
        for (TbContentCategory tbContentCategory : list) {
            TreeNode node = new TreeNode();
            node.setId(tbContentCategory.getId().intValue());
            node.setText(tbContentCategory.getName());
            //tbContentCategory.getIsParent()不为0是closed,为0是open
            node.setState(tbContentCategory.getIsParent()?"closed":"open");
            treeNodes.add(node);
        }
        return treeNodes;
    }

    public static void main(String[] args) {
    }

}
