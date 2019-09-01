package com.fendou.gmall.manage.controller;

import com.fendou.gmall.bean.PmsBaseAttrInfo;
import com.fendou.gmall.bean.PmsBaseAttrValue;
import com.fendou.gmall.service.AttrService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * AttrController class
 *
 * @author maochaoying
 * @date 2019/9/1
 */
@Controller
@CrossOrigin
public class AttrController {
    @Reference
    AttrService attrService;

    /**
     * 查询所有平台属性
     * @param catalog3Id
     * @return
     */
    @ResponseBody
    @RequestMapping("/attrInfoList")
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
        return attrService.attrInfoList(catalog3Id);
    }

    /**
     * 保存平台属性
     * @param pmsBaseAttrInfo
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveAttrInfo")
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo) {
        return attrService.saveAttrInfo(pmsBaseAttrInfo);
    }
    @ResponseBody
    @RequestMapping("/getAttrValueList")
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        return attrService.getAttrValueList(attrId);
    }


}
