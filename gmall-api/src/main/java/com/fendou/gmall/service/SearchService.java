package com.fendou.gmall.service;

import com.fendou.gmall.bean.PmsBaseAttrInfo;
import com.fendou.gmall.bean.PmsSearchCromb;
import com.fendou.gmall.bean.PmsSearchParam;
import com.fendou.gmall.bean.PmsSearchSkuInfo;

import java.util.List;

public interface SearchService {
    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);

    List<PmsBaseAttrInfo> getAttrValueAndAttrValueList(List<PmsSearchSkuInfo> pmsSearchSkuInfoList);

    String getUrlParam(PmsSearchParam pmsSearchParam);

    List<PmsBaseAttrInfo> delClickedAttrValue(PmsSearchParam pmsSearchParam, List<PmsBaseAttrInfo> pmsBaseAttrInfos);

    List<PmsSearchCromb> getCrombs(PmsSearchParam pmsSearchParam, List<PmsBaseAttrInfo> pmsBaseAttrInfos);
}
