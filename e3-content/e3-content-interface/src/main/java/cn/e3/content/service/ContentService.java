package cn.e3.content.service;

import cn.e3.domain.TbContent;
import cn.e3.utils.DatagridPageBean;
import cn.e3.utils.E3mallResult;

public interface ContentService {
    
    public E3mallResult createNode(Long parentId,String name);
    
    /**
     * 需求:根据id查询内容列表
     */
    public DatagridPageBean findContentListByCategoryId(Long categoryId,Integer page,Integer rows);
    
    /**
     * 需求:
     * 参数:
     * 返回值:
     */
    public E3mallResult saveContent(TbContent tbContent);
}
