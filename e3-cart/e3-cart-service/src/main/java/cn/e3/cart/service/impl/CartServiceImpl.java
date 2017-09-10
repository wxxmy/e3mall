package cn.e3.cart.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import cn.e3.cart.jedis.JedisService;
import cn.e3.cart.service.CartService;
import cn.e3.domain.TbItem;
import cn.e3.mapper.TbItemMapper;
import cn.e3.utils.E3mallResult;
import cn.e3.utils.JsonUtils;

public class CartServiceImpl implements CartService{

    // 注入jedisDao
    @Autowired
    private JedisService jedisService;
    
    // 注入redis购物车唯一标识
    @Value("${REDIS_CART_KEY}")
    private String REDIS_CART_KEY;
    
    // 注入redis商品id排序唯一标识
    @Value("${REDIS_SORT_CART_KEY}")
    private String REDIS_SORT_CART_KEY;
    
    // 注入商品mapper接口代码对象
    @Autowired
    private TbItemMapper tbItemMapper;
    /**
     * 需求:添加redis购物
     * 业务:
     * 1.判断redis购物车是否有相同商品
     * 2.有,数量相加
     * 3.没有直接添加
     * 注意:后添加的在前面展示
     */
    @Override
    public E3mallResult addRedisCart(Long userId, Long itemId, Integer num) {
        // 1.判断redis购物车是否有相同商品
        Boolean hexists = jedisService.hexists(REDIS_CART_KEY+":"+userId,itemId+"");
        //判断状态
        if (hexists) {
            // 购物车存在相同商品
            // 获取商品数据
            String itemJson = jedisService.hget(REDIS_CART_KEY+":"+userId, itemId+"");
            // 把json转换成对象
            TbItem item = JsonUtils.jsonToPojo(itemJson, TbItem.class);
            // 商品数量相加
            item.setNum(item.getNum()+num);
            
            // 添加redis购物车,并且给商品id排序
            this.addRedisWithSortCarrt(userId,item);
            
           
            
        }
        // 如果没有相同的 商品
        if (!hexists) {
            // 根据商品id查询新的商品
            TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
            // 设置购买数量
            tbItem.setNum(num);
            
            // 添加redis购物车,并且给商品id排序
            this.addRedisWithSortCarrt(userId,tbItem);
        }
        //2.有,数量相加
        // 3.没有直接添加
        // 默认返回status==200
        return E3mallResult.ok();
    }
    
    
    
    private void addRedisWithSortCarrt(Long userId, TbItem item) {
        // 放入redis购物车
        jedisService.hset(REDIS_CART_KEY+":"+userId, item.getId()+"", JsonUtils.objectToJson(item));
        // 获取当前时间毫秒
        Long currentTimeMillis = System.currentTimeMillis();
        // 使用sorted-set给商品id设置排序,根据时间
        jedisService.zadd(REDIS_SORT_CART_KEY+":"+userId, currentTimeMillis.doubleValue(), item.getId()+"");
    }



    /**
     * 需求:登陆状态时,把cookie购物车数据合并到redis购物车
     * 
     * @param userId
     * @param cartList
     * @return
     */
    public E3mallResult mergeCart(Long userId, List<TbItem> cartList) {
        // 循环购物车列表集合
        for (TbItem tbItem : cartList) {
            this.addRedisCart(userId, tbItem.getId(),tbItem.getNum());
        }
        return E3mallResult.ok();
    }



    /**
     * 需求:查询redis购物所有商品数据,并且进行有序展示(后添加,先展示)
     * @param userId
     * 业务:
     * 1.先获取有序商品id集合
     * 2.根据有序的商品id,获得购物车商品数据
     */
    public List<TbItem> findRedisCartList(Long userId) {
        
        // 创建商品List<TbItem>,封装购物车商品数据
        List<TbItem> cartList = new ArrayList<>();
        
        // 1.现货去有序商品id集合
        Set<String> itemids = jedisService.zrevrange(REDIS_SORT_CART_KEY+":"+userId,0l, -1l);
        // 2.循环集合,获取每一个商品id
        for (String itemId : itemids) {
            // 3.根据有序的商品id,获取购物车商品数据
            String itemJson = jedisService.hget(REDIS_CART_KEY+":"+userId, itemId);
            // 4. 转换
            TbItem tbItem = JsonUtils.jsonToPojo(itemJson, TbItem.class);
            // 5.把有序商品对象,放入list集合
            cartList.add(tbItem);
        }
        return cartList;
    }



    /**
     * 需求:此用户下面此商品id对象商品
     */
    public E3mallResult deleteCart(Long userId, Long itemId) {
        // 1.先查出商品id
        jedisService.zrem(REDIS_SORT_CART_KEY+":"+userId, itemId+"");
        // 2.删除购物车数据
        jedisService.hdel(REDIS_CART_KEY +":"+userId,itemId+"");
        return E3mallResult.ok();
    }

}
