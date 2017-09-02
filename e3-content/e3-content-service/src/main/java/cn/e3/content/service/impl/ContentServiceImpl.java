package cn.e3.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3.content.jedis.JedisService;
import cn.e3.content.service.ContentService;
import cn.e3.domain.TbContent;
import cn.e3.domain.TbContentCategory;
import cn.e3.domain.TbContentExample;
import cn.e3.domain.TbContentExample.Criteria;
import cn.e3.mapper.TbContentCategoryMapper;
import cn.e3.mapper.TbContentMapper;
import cn.e3.utils.AdItem;
import cn.e3.utils.DatagridPageBean;
import cn.e3.utils.E3mallResult;
import cn.e3.utils.JsonUtils;
@Service
public class ContentServiceImpl implements ContentService {
    
  //注入内容表Mapper接口代理对象
    @Autowired
    private TbContentMapper contentMapper;
    
    @Autowired
    private JedisService jedisService;
    
    //注入广告图片宽
    @Value("${WIDTH}")
    private Integer WIDTH;
    
    @Value("${WIDTHB}")
    private Integer WIDTHB;
    
    //注入广告图片高
    @Value("${HEIGHT}")
    private Integer HEIGHT;
    
    @Value("${HEIGHTB}")
    private Integer HEIGHTB;
    
    @Value("${INDEX_CACHE}")
    private String INDEX_CACHE;

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

    
    /**
     * 需求:根据所选商品类名查询其下的所有内容信息
     * 参数:Long categoryId,Integer page, Integer rows
     * 返回值:DatagridPageBean
     */
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

    /**
     * 需求:新增大广告内容
     * 参数:TbContent tbContent
     * 返回值:E3mallResult
     * 描述:新增先清除缓存
     */
    public E3mallResult saveContent(TbContent tbContent) {
        // 新增广告内容时删除缓存
        jedisService.hdel(INDEX_CACHE, tbContent.getCategoryId()+"");
        
        // 保存
        Date date = new Date();
        tbContent.setCreated(date);
        tbContent.setUpdated(date);
        tbContentMapper.insert(tbContent);
        return E3mallResult.ok();
    }
    
    /**
     * 需求:查询首页大广告
     * 参数:广告类目ID,Long categoryId
     * 返回值:广告对象集合,List<AdItem>
     * 描述:为了查询效率加入redis缓存集群
     */
    @Override
    public List<AdItem> findAdItemListWithCategoryId(Long categoryId) {
        // 创建广告集合对象
        List<AdItem> adItems = new ArrayList<>();
        // 从缓存中获取
        String jsonStr;
        try {
            jsonStr = jedisService.hget(INDEX_CACHE, categoryId+"");
            if (StringUtils.isNotBlank(jsonStr)) {
                
                List<AdItem> list = JsonUtils.jsonToList(jsonStr, AdItem.class);
                
                return list;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // 创建example对象
        TbContentExample example = new TbContentExample();
        Criteria createCriteria = example.createCriteria();
        createCriteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> list = tbContentMapper.selectByExample(example);
        
        // 遍历内容
        for (TbContent tbContent : list) {
            AdItem adItem = new AdItem();
            // 设置图片地址
            adItem.setSrc(tbContent.getPic());
            adItem.setSrcB(tbContent.getPic2());
            
            // 设置图片连接和鼠标悬停文本内容
            adItem.setAlt(tbContent.getTitleDesc());
            adItem.setHref(tbContent.getUrl());
            
            // 设置图片高
            adItem.setHeight(HEIGHT);
            adItem.setHeightB(HEIGHTB);
            
            // 设置图片宽
            adItem.setWidth(WIDTH);
            adItem.setWidthB(WIDTHB);
            
            adItems.add(adItem);
            
            jsonStr = JsonUtils.objectToJson(adItems);
            jedisService.hset(INDEX_CACHE, categoryId+"", jsonStr);
        }
        return adItems;
    }

}
