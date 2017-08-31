package cn.e3.content.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3.content.service.ContentService;
import cn.e3.domain.TbContent;
import cn.e3.domain.TbContentCategory;
import cn.e3.domain.TbContentExample;
import cn.e3.domain.TbContentExample.Criteria;
import cn.e3.mapper.TbContentCategoryMapper;
import cn.e3.mapper.TbContentMapper;
import cn.e3.utils.DatagridPageBean;
import cn.e3.utils.E3mallResult;

public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;
    
    @Autowired
    private TbContentMapper tbContentMapper;
    
    @Override
    public E3mallResult createNode(Long parentId, String name) {
        // 保存内容内容分类表, 补全属性
        TbContentCategory category = new TbContentCategory();
        // 设置属性参数
        category.setParentId(parentId);
        category.setName(name);
        // 补全属性
        // 状态 ,可选值:1(正常),2(删除)
        category.setStatus(1);
        // 取值范围:大于零的整数
        category.setSortOrder(1);
        // 该类目是否为父类,1为true,0为false
        category.setIsParent(false);
        //时间
        Date date = new Date();
        category.setCreated(date);
        category.setUpdated(date);
        //保存:返回主键
        tbContentCategoryMapper.insert(category);
        
        //如果创建节点是子节点?修改节点状态
        //根据id产线父节点状态
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        // 判断当前节点是否是父节点
        if (!tbContentCategory.getIsParent()) {
            //子节点,修改节点状态
            tbContentCategory.setIsParent(true);
            
            tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
        }
        return E3mallResult.ok(tbContentCategory);
    }

    @Override
    public DatagridPageBean findContentListByCategoryId(Long categoryId,
            Integer page, Integer rows) {
        // 创建example对象
        TbContentExample example = new TbContentExample();
        Criteria createCriteria = example.createCriteria();
        createCriteria.andCategoryIdEqualTo(categoryId);
        // 设置分页参数
        PageHelper.startPage(page, rows);
        // 查询
        List<TbContent> list = tbContentMapper.selectByExample(example);
        // 创建pageInfo对象,获取分页信息
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);
        
        // 构造返回值对象
        DatagridPageBean pageBean = new DatagridPageBean();
        pageBean.setTotal(pageInfo.getTotal());
        pageBean.setRows(list);
        return pageBean;
    }

    @Override
    public E3mallResult saveContent(TbContent tbContent) {
        // 保存
        Date date = new Date();
        tbContent.setCreated(date);
        tbContent.setUpdated(date);
        tbContentMapper.insert(tbContent);
        return E3mallResult.ok();
    }

}
