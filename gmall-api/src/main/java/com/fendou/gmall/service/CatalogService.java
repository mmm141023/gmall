package com.fendou.gmall.service;

import com.fendou.gmall.bean.PmsBaseCatalog1;
import com.fendou.gmall.bean.PmsBaseCatalog2;
import com.fendou.gmall.bean.PmsBaseCatalog3;

import java.util.List;

/**
 * CatalogService class
 *
 * @author maochaoying
 * @date 2019/8/31
 */

public interface CatalogService {
    /**
     * 获得一级列表
     * @return
     */
    public List<PmsBaseCatalog1> getCatalog1();

    /**
     * 获得二级列表
     * @return
     */
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id);

    /**
     * 获得三级列表
     * @param catalog2Id
     * @return
     */
    List<PmsBaseCatalog3> getCatalog3(String catalog2Id);
}
