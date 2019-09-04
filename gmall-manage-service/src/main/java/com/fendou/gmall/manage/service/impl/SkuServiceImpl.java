package com.fendou.gmall.manage.service.impl;

import com.fendou.gmall.bean.PmsSkuAttrValue;
import com.fendou.gmall.bean.PmsSkuImage;
import com.fendou.gmall.bean.PmsSkuInfo;
import com.fendou.gmall.bean.PmsSkuSaleAttrValue;
import com.fendou.gmall.manage.dao.*;
import com.fendou.gmall.service.SkuService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * SkuServiceImpl class
 *
 * @author maochaoying
 * @date 2019/9/3
 */
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;
    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;
    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;
    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Override
    public String saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setSkuId(pmsSkuInfo.getId());
            pmsSkuImage.setProductImgId(pmsSkuInfo.getSpuId());
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }
        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId(pmsSkuInfo.getId());
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }
        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            pmsSkuAttrValue.setSkuId(pmsSkuInfo.getId());
            pmsSkuAttrValueMapper.insert(pmsSkuAttrValue);
        }
        return "success";
    }

    /**
     * 根据ID查询sku(商品详情页)
     * @param skuId
     * @return
     */
    @Override
    public PmsSkuInfo getSkuListById(String skuId) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        PmsSkuInfo pmsSkuInfo1 = pmsSkuInfoMapper.selectOne(pmsSkuInfo);
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> select = pmsSkuImageMapper.select(pmsSkuImage);
        pmsSkuInfo1.setSkuImageList(select);
        return pmsSkuInfo1;
    }
}
