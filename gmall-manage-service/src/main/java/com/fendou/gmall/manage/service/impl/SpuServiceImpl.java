package com.fendou.gmall.manage.service.impl;

import com.fendou.gmall.bean.PmsProductInfo;
import com.fendou.gmall.manage.dao.PmsProductInfoMapper;
import com.fendou.gmall.service.SpuService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * SpuServiceImpl class
 *
 * @author maochaoying
 * @date 2019/9/1
 */
@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;
    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        return pmsProductInfoMapper.select(pmsProductInfo);
    }
}
