package cn.e3.cart.service;

import java.util.List;

import cn.e3.domain.TbItem;
import cn.e3.utils.E3mallResult;

public interface CartService {

    /**
     * 需求:添加redis购物车
     */
    E3mallResult addRedisCart(Long userId, Long itemId, Integer num);
    /**
     * 需求:登陆状态时,把cookie购物车数据合并到redis购物车
     * 
     * @param userId
     * @param cartList
     * @return
     */
    E3mallResult mergeCart(Long userId, List<TbItem> cartList);
    
    /**
     * 需求:查询redis购物所有商品数据,并且进行有序展示(后添加,先展示)
     * @param userId
     */
    List<TbItem> findRedisCartList(Long userId);
    /**
     * 需求:删除此用户下面商品id对应商品
     * @param id
     * @param itemId
     * @return
     */
    E3mallResult deleteCart(Long userId, Long itemId);
}
