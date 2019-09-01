package com.fendou.gmall.manage.service.impl;

import com.fendou.gmall.bean.PmsBaseAttrInfo;
import com.fendou.gmall.bean.PmsBaseAttrValue;
import com.fendou.gmall.manage.dao.AttrInfoMapper;
import com.fendou.gmall.manage.dao.AttrValueMapper;
import com.fendou.gmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import javax.swing.*;
import java.util.List;

/**
 * AttrServiceImpl class
 *
 * @author maochaoying
 * @date 2019/9/1
 */
@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    AttrInfoMapper attrInfoMapper;
    @Autowired
    AttrValueMapper attrValueMapper;
    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {

        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        return attrInfoMapper.select(pmsBaseAttrInfo);
    }

    @Override
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
        String attrName = pmsBaseAttrInfo.getAttrName();
        PmsBaseAttrInfo pmsBaseAttrInfo1 = new PmsBaseAttrInfo();
        pmsBaseAttrInfo1.setAttrName(attrName);
        if (attrInfoMapper.select(pmsBaseAttrInfo1) == null) {
            // 保存平台属性
            attrInfoMapper.insertSelective(pmsBaseAttrInfo);
            // 保存平台属性值
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                attrValueMapper.insertSelective(pmsBaseAttrValue);
            }
        }else {
            // 执行修改操作
            Example example = new Example(PmsBaseAttrInfo.class);
            example.createCriteria().andEqualTo("attrName", attrName);
            List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrInfoMapper.selectByExample(example);
            String id = "";
            if (pmsBaseAttrInfos.size() > 0) {
                id = pmsBaseAttrInfos.get(0).getId();
                attrInfoMapper.deleteByExample(example);

            }
            attrInfoMapper.insertSelective(pmsBaseAttrInfo);
            // 修改属性值
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                Example example1 = new Example(PmsBaseAttrValue.class);
                example1.createCriteria().andEqualTo("attrId", id);
                attrValueMapper.deleteByExample(example1);
                pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                attrValueMapper.insertSelective(pmsBaseAttrValue);
            }
        }
        return "success";
    }
}
