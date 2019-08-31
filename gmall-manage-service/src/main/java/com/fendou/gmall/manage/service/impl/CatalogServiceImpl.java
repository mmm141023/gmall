package com.fendou.gmall.manage.service.impl;

import com.fendou.gmall.bean.PmsBaseCatalog1;
import com.fendou.gmall.manage.dao.PmsBaseCatalog1Mapper;
import com.fendou.gmall.service.CatalogService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * CatalogServiceImpl class
 *
 * @author maochaoying
 * @date 2019/8/31
 */
@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper;

    @Override
    public List<PmsBaseCatalog1> getCatalog1() {
        return pmsBaseCatalog1Mapper.selectAll();
    }
}
