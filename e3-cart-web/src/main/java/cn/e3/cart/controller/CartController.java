package cn.e3.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3.cart.service.CartService;
import cn.e3.cart.utils.CookieUtils;
import cn.e3.domain.TbItem;
import cn.e3.domain.TbUser;
import cn.e3.service.ItemService;
import cn.e3.utils.E3mallResult;
import cn.e3.utils.JsonUtils;

@Controller
public class CartController {
    
    // 注入购物车服务对象
    @Autowired
    private CartService cartService;
    
    // cookie购物车唯一标识
    @Value("${COOKIE_CART}")
    private String COOKIE_CART;
    
    // cookie购物车过期时间
    @Value("${COOKIE_CART_EXPIRE_TIME}")
    private Integer COOKIE_CART_EXPIRE_TIME;
    
    // 注入商品服务
    @Autowired
    private ItemService itemService;
    /**
     * 需求:添加购物车
     * 请求:/cart/add/***********.html?num=8
     * 参数:Long itemId,Integer num
     * 返回值:cartSuccess
     * 业务:
     * 1.登陆(redis购物车)
     * >判断购物车中是否有相同的商品
     * >有,商品数量相加
     * >无,商品直接添加
     * 2.未登陆(cookie购物车)
     * >获取cookie购物车列表
     * >判断购物车中是否有相同商品
     * >有,商品数量直接相加
     * >没有,直接添加
     */
    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId,
            Integer num,HttpServletRequest request,
            HttpServletResponse response){
        // 从request域中获取用户身份信息,判断用户是否处于登陆状态
        TbUser user = (TbUser) request.getAttribute("user");
        // 判断用户是否登陆
        if (user!=null) {
            // 用户此时登陆,添加redis购物车
            E3mallResult result = cartService.addRedisCart(user.getId(),itemId,num);
            // 返回购物车成功页面
            return "cartSuccess";
        }
        // 未登录
        // 先获取cookie中购物列表
        List<TbItem> cartList = this.getCookieCartList(request);
        
        // 定义一个标识,判断是否有相同商品
        boolean flag = false;
        
        // 判断此购物车中是否存在相同的商品
        for (TbItem tbItem : cartList) {
            
            // 如果添加的商品id和cookie购物车列表中商品id相等,表示有相同的商品
            if (tbItem.getId()== itemId.longValue()) {
                // 商品数量相加
                tbItem.setNum(tbItem.getNum()+num);
                
                // 设置标识
                flag =true;
                
                // 结束循环,每次只添加一个商品
                break;
            }
        }
        // 没有相同商品
        if (!flag) {
            // 直接购买
            TbItem item = itemService.findTbItemById(itemId);
            //设置购买数量
            item.setNum(num);
            // 放入购物车列表
            cartList.add(item);
        }
        // 把购物车列表写回到cookie购物车
        CookieUtils.setCookie(request,
                response,
                COOKIE_CART,
                JsonUtils.objectToJson(cartList),
                COOKIE_CART_EXPIRE_TIME,
                true);
        
        // 返回购物车成功页面
        return "cartSuccess";
    }
    /**
     * 需求:查询购物车列表,展示购物车清单
     * 请求:/cart/cart.html
     * 返回值:cart.jsp
     * 业务:
     * 1.登陆,redis购物车
     * >查询cookie购物车数据,如果cookie购物车有数据,需要合并购物车,把cookie购物车合并到redis
     * >清空cookie购物车数据
     * >查询redis购物车列表
     * 2.未登录,cookie购物车
     * >查询cookie购物车列表即可
     */

    @RequestMapping("/cart/cart")
    public String showCart(HttpServletRequest request,
            HttpServletResponse response){
        // 从reques获取用户信息,判断是否登陆
        TbUser user = (TbUser) request.getAttribute("user");
        
        //获取cookie购物车列表
        List<TbItem> cartList = this.getCookieCartList(request);
        
        // 判断用户是否登陆
        if (user!=null) {
            // 此时用户处于登陆状态
            // 判断cookie购物车是否为空,不为空,合并购物车数据
            if (!cartList.isEmpty()) {
                // 合并购物车
                E3mallResult result = cartService.mergeCart(user.getId(),cartList);
                // 清空cookie购物车
                CookieUtils.setCookie(request,
                        response, 
                        COOKIE_CART,
                        "",
                        0, 
                        true);
            }
            // 查询redis购物车
            cartList = cartService.findRedisCartList(user.getId());
            
        }
        // 把购物车列表数据放入request域,页面回显
        request.setAttribute("cartList", cartList);
        
        return "cart";
    }
    
    /**
     * 需求:获取cookie购物车列表
     * @param request
     * @return
     */
   List<TbItem> getCookieCartList(HttpServletRequest request) {
        String cartJson = CookieUtils.getCookieValue(request, COOKIE_CART, true);
        // 判断cookie购物车是否有数据
        if (StringUtils.isBlank(cartJson)) {
            // 返回空的购物车列表
            return new ArrayList<TbItem>();
        }
        // 如果有值
        List<TbItem> cartList = JsonUtils.jsonToList(cartJson, TbItem.class);
        return cartList;
    }
    
    /**
     * 需求:商品购物车商品
     * 请求:/cart/delete/**.html
     * 参数:Long itemId
     * 返回值: return "redirect:/cart/cart.html"
     * 业务:1.登陆2.未登录
     */
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCart(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response){
        // 从reques获取用户信息,判断是否登陆
        TbUser user = (TbUser) request.getAttribute("user");
        // 判断用户是否登陆
        if (user!=null) {
            // 用户登陆,删除redis购物车
            E3mallResult result = cartService.deleteCart(user.getId(),itemId);
            // 跳转购物车列表
            return "redirect:/cart/cart.html";
        }
        // 未登录
        // 获取购物车列表
        List<TbItem> cartList = this.getCookieCartList(request);
        // 判断应该删除哪个商品
        for (TbItem tbItem : cartList) {
            // 如果要删除的商品id等于集合列表中商品id,表示此商品要被删除
            if (tbItem.getId()==itemId.longValue()) {
                // 删除
                cartList.remove(tbItem);
                //
                break;
            }
        }
        // 把购物车列表写回cookie购物车
        CookieUtils.setCookie(request,
                response,
                COOKIE_CART,
                JsonUtils.objectToJson(cartList),
                true);
        return "redirect:/cart/cart.html";
    }
}
