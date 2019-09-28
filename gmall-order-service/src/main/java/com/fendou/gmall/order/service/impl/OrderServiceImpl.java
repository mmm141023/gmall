package com.fendou.gmall.order.service.impl;

import com.fendou.gmall.bean.OmsCartItem;
import com.fendou.gmall.bean.OmsOrder;
import com.fendou.gmall.bean.OmsOrderItem;
import com.fendou.gmall.bean.UmsMemberReceiveAddress;
import com.fendou.gmall.order.dao.OmsOrderItemMapper;
import com.fendou.gmall.order.dao.OmsOrderMapper;
import com.fendou.gmall.order.dao.UmsMemberReceiveAddressMapper;
import com.fendou.gmall.service.CartService;
import com.fendou.gmall.service.OrderService;
import com.fendou.gmall.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OrderServiceImpl class
 *
 * @author maochaoying
 * @date 2019/9/25
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;
    @Reference
    UserService userService;
    @Reference
    CartService cartService;
    @Autowired
    OmsOrderItemMapper omsOrderItemMapper;
    @Autowired
    OmsOrderMapper omsOrderMapper;
    @Override
    public List<UmsMemberReceiveAddress> getUmsMemberReceiveAddressByMemberId(String memberId) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(memberId);
        List<UmsMemberReceiveAddress> select = umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress);
        return select;
    }

    /**
     * 将购物车对象转化为订单对象
     * @param omsCartItems
     * @return
     */
    @Override
    public List<OmsOrderItem> castCartItemToOrderItem(List<OmsCartItem> omsCartItems) {
        List<OmsOrderItem> omsOrderItems = new ArrayList<>();
        for (OmsCartItem omsCartItem : omsCartItems) {
            if (omsCartItem.getIsChecked().equals("1")) {
                // 表明选中了的物品
                OmsOrderItem omsOrderItem = new OmsOrderItem();
                omsOrderItem.setOrderSn(getOrderCodeSn());
                omsOrderItem.setProductId(omsCartItem.getProductId());
                omsOrderItem.setProductPic(omsCartItem.getProductPic());
                omsOrderItem.setProductName(omsCartItem.getProductName());
                omsOrderItem.setProductBrand(omsCartItem.getProductBrand());
                omsOrderItem.setProductSn(omsCartItem.getProductSn());
                omsOrderItem.setProductPrice(omsCartItem.getPrice());
                omsOrderItem.setProductQuantity(omsCartItem.getQuantity());
                omsOrderItem.setProductSkuId(omsCartItem.getProductSkuId());
                omsOrderItem.setProductSkuCode(omsCartItem.getProductSkuCode());
                omsOrderItem.setProductCategoryId(omsCartItem.getProductCategoryId());
                omsOrderItem.setSp1(omsCartItem.getSp1());
                omsOrderItem.setSp2(omsCartItem.getSp2());
                omsOrderItem.setSp3(omsCartItem.getSp3());
                omsOrderItem.setPromotionName(omsCartItem.getProductName());
                omsOrderItem.setRealAmount(omsCartItem.getPrice());
                omsOrderItem.setProductAttr("[{\"key\":\"颜色\",\"value\":\"颜色\"},{\"key\":\"容量\",\"value\":\"4G\"}]");
                omsOrderItems.add(omsOrderItem);
            }
        }
        return omsOrderItems;
    }

    private String getOrderCodeSn() {
        long currentTimeMillis = System.currentTimeMillis();
        String orderCodeSn = "20190925" + currentTimeMillis;
        return orderCodeSn;
    }

    @Override
    public UmsMemberReceiveAddress getUmsMemberReceiveAddressById(String deliveryAddressId) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setId(deliveryAddressId);
        UmsMemberReceiveAddress umsMemberReceiveAddress1 = umsMemberReceiveAddressMapper.selectOne(umsMemberReceiveAddress);
        return umsMemberReceiveAddress1;
    }

    @Override
    public OmsOrder generateOmsOrder(List<OmsOrderItem> omsOrderItems, UmsMemberReceiveAddress umsMemberReceiveAddress, String memberId, List<OmsCartItem> omsCartItems) {

        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setOmsOrderItems(omsOrderItems);
        omsOrder.setMemberId(memberId);
        omsOrder.setCouponId("2");
        omsOrder.setOrderSn(getOrderCodeSn());
        omsOrder.setCreateTime(new Date());
        BigDecimal totalAmount = cartService.getTotalAmount(omsCartItems);
        omsOrder.setTotalAmount(totalAmount);
        omsOrder.setPayAmount(totalAmount);
        String usernameByMemberId = userService.getUsernameByMemberId(memberId);
        omsOrder.setMemberUsername(usernameByMemberId);
        omsOrder.setPayType("0");
        omsOrder.setSourceType("0");
        omsOrder.setStatus("0");
        omsOrder.setOrderType("0");
        omsOrder.setAutoConfirmDay("7");
        omsOrder.setIntegration("300");
        omsOrder.setGrowth("200");
        omsOrder.setBillType("0");
        omsOrder.setBillReceiverPhone(umsMemberReceiveAddress.getPhoneNumber());
        omsOrder.setReceiverName(umsMemberReceiveAddress.getName());
        omsOrder.setReceiverPhone(umsMemberReceiveAddress.getPhoneNumber());
        omsOrder.setReceiverPostCode(umsMemberReceiveAddress.getPostCode());
        omsOrder.setReceiverProvince(umsMemberReceiveAddress.getProvince());
        omsOrder.setReceiverCity(umsMemberReceiveAddress.getCity());
        omsOrder.setReceiverRegion(umsMemberReceiveAddress.getRegion());
        omsOrder.setReceiverDetailAddress(umsMemberReceiveAddress.getDetailAddress());
        omsOrder.setNote("麻烦发货迅速");
        omsOrder.setConfirmStatus("0");
        omsOrder.setDeleteStatus("0");
        omsOrder.setUseIntegration("0");
        omsOrder.setPaymentTime(new Date());
        omsOrder.setModifyTime(new Date());
        // 存入OmsOrder表
        omsOrderMapper.insertSelective(omsOrder);
        // 删除购物车
        for (OmsOrderItem omsOrderItem : omsOrderItems) {
            omsOrderItem.setOrderId(omsOrder.getId());
            //存入orderIterm表
            omsOrderItemMapper.insertSelective(omsOrderItem);
        }
        return omsOrder;
    }
}
