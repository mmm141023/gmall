package com.fendou.gmall.search.controller;

import com.fendou.gmall.bean.PmsSearchSkuInfo;
import com.fendou.gmall.bean.PmsSkuInfo;
import com.fendou.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * MysqlToESController class
 *  因为使用测试类 无法正常引用skuservice
 *  在配置文件中加入时间超时注解  dubbo.consumer.timeout=600000  可以解决以上问题
 *  该controller用来对mysql数据整体进行转化es
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
    public String dataTrans() throws IOException {
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

        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
            Index build = new Index.Builder(pmsSearchSkuInfo).index("gmall").type("PmsSkuInfo").id(pmsSearchSkuInfo.getId()).build();
            jestClient.execute(build);
        }
        return "success";
    }
}

