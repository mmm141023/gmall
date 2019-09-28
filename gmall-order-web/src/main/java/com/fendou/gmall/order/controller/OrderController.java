package com.fendou.gmall.order.controller;

import com.fendou.gmall.annotation.LoginRequired;
import com.fendou.gmall.bean.OmsCartItem;
import com.fendou.gmall.bean.OmsOrder;
import com.fendou.gmall.bean.OmsOrderItem;
import com.fendou.gmall.bean.UmsMemberReceiveAddress;
import com.fendou.gmall.service.CartService;
import com.fendou.gmall.service.OrderService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * OrderController class
 *
 * @author maochaoying
 * @date 2019/9/24
 */

@Controller
public class OrderController {

    @Reference
    OrderService orderService;
    @Reference
    CartService cartService;


    @RequestMapping("/toTrade")
    @LoginRequired(loginSuccess = true)
    public String toTrade(HttpServletRequest request, ModelMap modelMap) {
        String memberId =(String) request.getAttribute("memberId");
        String nickname =(String) request.getAttribute("nickname");
        // 收货地址
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = orderService.getUmsMemberReceiveAddressByMemberId(memberId);
        // 拿到购物车List
        List<OmsCartItem> omsCartItems = cartService.cartList(memberId);
        // 将选中的购物车物品封装为order对象
        List<OmsOrderItem> omsOrderItems = orderService.castCartItemToOrderItem(omsCartItems);
        // 获得总价钱
        BigDecimal totalAmount = cartService.getTotalAmount(omsCartItems);
        modelMap.put("totalAmount", totalAmount);
        modelMap.put("orderDetailList", omsOrderItems);
        modelMap.put("userAddressList", umsMemberReceiveAddresses);
        modelMap.put("nickName", nickname);
        return "trade";
    }

    @RequestMapping("/submitOrder")
    @LoginRequired(loginSuccess = true)
    public String submitOrder(String deliveryAddressId, HttpServletRequest request) {
        String memberId =(String) request.getAttribute("memberId");
        // 根据deliveryAddressId查询地址信息
        UmsMemberReceiveAddress umsMemberReceiveAddress = orderService.getUmsMemberReceiveAddressById(deliveryAddressId);
        // 拿到购物车List
        List<OmsCartItem> omsCartItems = cartService.cartList(memberId);
        // 将选中的购物车物品封装为order对象
        List<OmsOrderItem> omsOrderItems = orderService.castCartItemToOrderItem(omsCartItems);
        OmsOrder omsOrder = orderService.generateOmsOrder(omsOrderItems, umsMemberReceiveAddress, memberId, omsCartItems);
        // 获得总价格
        BigDecimal totalAmount = omsOrder.getTotalAmount();
        // 订单号
        String orderSn = omsOrder.getOrderSn();

        return "redirect:http://localhost:8089/index?totalAmount=" + totalAmount + "&orderSn=" + orderSn;
    }
}
