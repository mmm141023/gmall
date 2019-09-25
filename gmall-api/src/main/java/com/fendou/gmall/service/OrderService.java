package com.fendou.gmall.service;

import com.fendou.gmall.bean.OmsCartItem;
import com.fendou.gmall.bean.OmsOrderItem;
import com.fendou.gmall.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface OrderService {
    List<UmsMemberReceiveAddress> getUmsMemberReceiveAddressByMemberId(String memberId);

    List<OmsOrderItem> castCartItemToOrderItem(List<OmsCartItem> omsCartItems);
}
