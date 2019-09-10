package com.fendou.gmall.search.controller;

import com.fendou.gmall.bean.PmsSearchSkuInfo;
import com.fendou.gmall.bean.PmsSkuInfo;
import com.fendou.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * MysqlToESController class
 *
 * @author maochaoying
 * @date 2019/9/10
 */
@Controller
public class MysqlToESController {

    @Reference
    SkuService skuService;

    @Autowired
    JestClient jestClient;

    @RequestMapping("/dataTrans")
    @ResponseBody
    public String dataTrans() {
        List<PmsSkuInfo> allSkuInfo = skuService.getAllSkuInfo();
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();
        for (PmsSkuInfo pmsSkuInfo : allSkuInfo) {
            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
            try {
                BeanUtils.copyProperties(pmsSearchSkuInfo, pmsSkuInfo);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            pmsSearchSkuInfos.add(pmsSearchSkuInfo);
        }
        return "success";
    }
}

