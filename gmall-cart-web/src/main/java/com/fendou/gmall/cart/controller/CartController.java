package com.fendou.gmall.cart.controller;

import com.alibaba.fastjson.JSON;
import com.fendou.gmall.annotation.LoginRequired;
import com.fendou.gmall.bean.OmsCartItem;
import com.fendou.gmall.bean.PmsSkuInfo;
import com.fendou.gmall.service.CartService;
import com.fendou.gmall.service.SkuService;
import com.fendou.gmall.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * CartController class
 *
 * @author maochaoying
 * @date 2019/9/17
 */
@Controller
public class CartController {

    @Reference
    SkuService skuService;
    @Reference
    CartService cartService;

    @RequestMapping("/addToCart")
    @LoginRequired(loginSuccess = false)
    public String addToCart(String skuId, BigDecimal quantity, ModelMap modelMap, HttpServletResponse response, HttpServletRequest request) {
        // 购物车商品信息可能不止一条，设置为集合
        List<OmsCartItem> omsCartItems = new ArrayList<>();
        // 从skuService中根据skuId查询info
        PmsSkuInfo skuListById = skuService.getSkuListById(skuId);
        // 将skuInfo封装为购物车对象
        //设置memberId 来代表用户是否登录
        String memberId =(String) request.getAttribute("memberId");
        if (StringUtils.isBlank(memberId)) {
            OmsCartItem omsCartItem = cartService.transSkuInfoToCartItem(skuListById, quantity);
            // 用户未登录 购物车存入cookie
            String cartListCookie = CookieUtils.getCookieValue(request, "cartListCookie", true);
            if (StringUtils.isNotBlank(cartListCookie)) {
                // 说明该cookie已经存在
                List<OmsCartItem> omsCartItems1 = JSON.parseArray(cartListCookie, OmsCartItem.class);
                for (OmsCartItem cartItem : omsCartItems1) {
                    omsCartItems.add(cartItem);
                    if (omsCartItem.getProductSkuId().equals(cartItem.getProductSkuId())) {
                        omsCartItems.remove(cartItem);
                    }
                }
                omsCartItems.add(omsCartItem);
                // 将购物车对象转化为String
                String jsonString = JSON.toJSONString(omsCartItems);
                CookieUtils.setCookie(request, response, "cartListCookie", jsonString, 60 * 60 * 72, true);
            }else {
                // 该cookie不存在
                omsCartItems.add(omsCartItem);
                String jsonString = JSON.toJSONString(omsCartItems);
                CookieUtils.setCookie(request, response, "cartListCookie", jsonString, 60 * 60 * 72, true);
            }
        }else {
            // 用户已登录 购物车存入db
            // 封装为购物车对象
            OmsCartItem omsCartItem = cartService.transSkuInfoToCartItemWhenLogin(skuListById, quantity,memberId);
            // 获取缓存中的数据
            String cartListCookie = CookieUtils.getCookieValue(request, "cartListCookie", true);
            List<OmsCartItem> omsCartItems1 = JSON.parseArray(cartListCookie, OmsCartItem.class);
            // 缓存中数据也存入数据库
            if (omsCartItems1 == null) {
                omsCartItems1 = new ArrayList<>();
            }
            omsCartItems1.add(omsCartItem);
            String result1 = cartService.saveOmsCartItemFromCache(omsCartItems1, skuId, memberId);
            // 清除cookie
            CookieUtils.deleteCookie(request, response, "cartListCookie");
            //加入缓存
            cartService.flushCartCache(memberId);
        }
        modelMap.put("skuInfo", skuListById);
        modelMap.put("skuNum", quantity);
        return "success";
    }
    @RequestMapping("/cartList")

    public String cartList(ModelMap modelMap, HttpServletRequest request) {
        String memberId =(String) request.getAttribute("memberId");
        List<OmsCartItem> omsCartItems = new ArrayList<>();
        if (StringUtils.isNotBlank(memberId)) {
            // 用户登录 ，从缓存中拿到数据
            omsCartItems = cartService.cartList(memberId);
        }else {
            // 用户未登录 从cookie中拿数据
            String cartListCookie = CookieUtils.getCookieValue(request, "cartListCookie", true);
            omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
            for (OmsCartItem omsCartItem : omsCartItems) {
                omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(omsCartItem.getQuantity()));
            }
        }
        BigDecimal totalAmount = cartService.getTotalAmount(omsCartItems);
        modelMap.put("totalAmount",totalAmount);
        modelMap.put("cartList",omsCartItems);
        return "cartList";
    }

    @RequestMapping("/checkCart")
    @LoginRequired(loginSuccess = true)
    public String checkCart(String isChecked, String skuId,ModelMap modelMap,HttpServletRequest request) {

        String memberId =(String) request.getAttribute("memberId");
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setIsChecked(isChecked);
        omsCartItem.setProductSkuId(skuId);
        cartService.checkCart(omsCartItem);
        // 从缓存中拿到最新数据
        List<OmsCartItem> omsCartItems = cartService.cartList(memberId);
        BigDecimal totalAmount = cartService.getTotalAmount(omsCartItems);
        modelMap.put("totalAmount",totalAmount);
        modelMap.put("cartList",omsCartItems);
        return "CartListInner";
    }
}
