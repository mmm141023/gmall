package com.fendou.gmall.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fendou.gmall.bean.PmsSkuAttrValue;
import com.fendou.gmall.bean.PmsSkuImage;
import com.fendou.gmall.bean.PmsSkuInfo;
import com.fendou.gmall.bean.PmsSkuSaleAttrValue;
import com.fendou.gmall.manage.dao.*;
import com.fendou.gmall.service.SkuService;
import com.fendou.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

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
    @Autowired
    RedisUtil redisUtil;

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
     * 从数据库中查询
     * @param skuId
     * @return
     */
    private PmsSkuInfo getSkuListByIdFromDB(String skuId) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        PmsSkuInfo pmsSkuInfo1 = pmsSkuInfoMapper.selectOne(pmsSkuInfo);
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> select = pmsSkuImageMapper.select(pmsSkuImage);
        pmsSkuInfo1.setSkuImageList(select);
        return pmsSkuInfo1;
    }
    /**
     * 根据ID查询sku(商品详情页)    加入缓存
     * @param skuId
     * @return
     */
    @Override
    public PmsSkuInfo getSkuListById(String skuId) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        // 连接redis
        Jedis jedis = redisUtil.getJedis();
        String skuKey = "sku:" + skuId + ":info";
        String skuInfoJson = jedis.get(skuKey);
        if (StringUtils.isNotBlank(skuInfoJson)){
            pmsSkuInfo= JSON.parseObject(skuInfoJson, PmsSkuInfo.class);
        }else{
            String OK = jedis.set("sku:" + skuId + ":lock", "1", "nx", "px", 10);
            if (StringUtils.isNotBlank(OK) && OK.equals("OK")) {
                pmsSkuInfo = getSkuListByIdFromDB(skuId);
                if (pmsSkuInfo != null) {
                    jedis.set(skuKey, JSON.toJSONString(pmsSkuInfo));
                }else{
                    //防止缓存穿透  （访问一个数据库不存在的key，redis中也没有）  就将其访问的key设置为空串并设定过期时间
                    jedis.setex(skuKey, 60 * 3, JSON.toJSONString(""));
                }
            }else{
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return getSkuListById(skuId);
            }

        }
        jedis.close();
        return pmsSkuInfo;
    }
}
