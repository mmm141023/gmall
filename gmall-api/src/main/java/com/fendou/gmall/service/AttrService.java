package com.fendou.gmall.service;

import com.fendou.gmall.bean.PmsBaseAttrInfo;
import com.fendou.gmall.bean.PmsBaseAttrValue;

import java.util.List;

/**
 * AttrService class
 *
 * @author maochaoying
 * @date 2019/9/1
 */

public interface AttrService {
    List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseAttrValue> getAttrValueList(String attrId);
}
