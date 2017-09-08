package cn.e3.service.impl;

import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import cn.e3.domain.TbItem;
import cn.e3.domain.TbItemDesc;
import cn.e3.domain.TbItemExample;
import cn.e3.item.jedis.JedisService;
import cn.e3.mapper.TbItemDescMapper;
import cn.e3.mapper.TbItemMapper;
import cn.e3.service.ItemService;
import cn.e3.utils.DatagridPageBean;
import cn.e3.utils.E3mallResult;
import cn.e3.utils.IDUtils;
import cn.e3.utils.JsonUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Service
public class ItemServiceImpl implements ItemService {

    
    @Autowired
    private TbItemMapper tbItemMapper;
    
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    
    // 注入消息队列模版发送消息
    @Autowired
    private JmsTemplate jmsTemplate;
    
    // 注入目的地对象
    @Autowired
    private ActiveMQTopic activeMQTopic;
    
    // 注入redis缓存Dao对象
    @Autowired
    private JedisService jedisService;
    
    // 注入商品详情缓存常量
    @Value("${ITEM_DETAIL}")
    private String ITEM_DETAIL;
    
    @Value("${ITEM_DETAIL_EXPIRE_TIME}")
    private Integer ITEM_DETAIL_EXPIRE_TIME;
    /**
     * 需求：根据id查询商品信息
     * 参数：Long itemId
     * 返回值：TbItem
     * 方法名：findTbItemById
     * 解决高并发服务器压力方案:redis缓存
     * redis里面存储数据类型:String(只有此种类型能设置过期时间)
     * key:ITEM_DETAIL:BASE:itemId
     * value:TbItem对象的字符串
     * 
     */
    @Override
    public TbItem findTbItemById(Long itemId) {
        // 1.获取缓存
        String TbItemJson = jedisService.get(ITEM_DETAIL+":BASE:"+itemId);
        // 2.判断是否为空
        if (StringUtils.isNotBlank(TbItemJson)) {
            // 不为空,直接返回
            TbItem tbItem = JsonUtils.jsonToPojo(TbItemJson, TbItem.class);
            return tbItem;
        }
        // 如果没有缓存,从数据库查询得到商品详情对象
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        // 设置缓存
        jedisService.set(ITEM_DETAIL+":BASE:"+itemId, JsonUtils.objectToJson(tbItem));
        // 设置过期时间
        jedisService.expire(ITEM_DETAIL+":BASE:"+itemId, ITEM_DETAIL_EXPIRE_TIME);
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
     * 需求：后台新增商品,同时通过activeMQ来同步修改索引库
     * 参数：TbItem tbItem, TbItemDesc tbItemDesc
     * 返回值：E3mallResult
     * 方法名：saveTbItem
     */
    public E3mallResult saveTbItem(TbItem tbItem, TbItemDesc tbItemDesc) {
        
        // 生成商品id
        final long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        //商品状态:1正常,2下架,3删除
        tbItem.setStatus((byte)1);
        //商品添加时间
        Date date = new Date();
        tbItem.setCreated(date);
        tbItem.setUpdated(date);
        //保存商品表信息
        tbItemMapper.insert(tbItem);
        
        // 保存商品描述信息
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
        tbItemDescMapper.insert(tbItemDesc);
        
        // 发送消息
        jmsTemplate.send(activeMQTopic, new MessageCreator() {
            
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage(itemId+"");
                return message;
            }
        });
        
        return E3mallResult.ok();
    }
    /**
     * 需求:根据商品ID查询到商品描述信息
     * 参数:ItemId itemId
     * 返回值:TbItemDesc tbItemDesc
     * 解决高并发服务器压力方案:redis缓存
     * redis里面存储数据类型:String(只有此种类型能设置过期时间)
     * key:ITEM_DETAIL:BASE:itemId
     * value:TbItem对象的字符串
     */
    public TbItemDesc findTbItemDescByTbItemId(Long ItemId) {
        // 1.先从缓存中获取商品描述详情
        String tbItemJson = jedisService.get(ITEM_DETAIL+":DESC:"+ItemId);
        // 2. 判断缓存中是否存在商品描述详情
        if (StringUtils.isNotBlank(tbItemJson)) {
            // 3.如果存在
            TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(tbItemJson, TbItemDesc.class);
            return tbItemDesc;
        }
        // 4. 如果不存在,从数据库获取
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(ItemId);
        // 5.设置进redis缓存中
        jedisService.set(ITEM_DETAIL+":DESC:"+ItemId, JsonUtils.objectToJson(tbItemDesc));
        // 6.设置缓存过期时间
        jedisService.expire(ITEM_DETAIL+":DESC:"+ItemId, ITEM_DETAIL_EXPIRE_TIME);
        return tbItemDesc;
    }
    @Override
    public TbItem findTbItemWithItemId(Long itemId) {
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        return tbItem;
    }
    @Override
    public TbItemDesc findTbItemDescWithItemId(Long itemId) {
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
        return tbItemDesc;
    }
    
  
}
