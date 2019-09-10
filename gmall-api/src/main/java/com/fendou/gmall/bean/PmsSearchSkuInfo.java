package com.fendou.gmall.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * PmsSearchSkuInfo class
 *
 * @author maochaoying
 * @date 2019/9/10
 */

public class PmsSearchSkuInfo implements Serializable {
    private String id;
    private BigDecimal price;
    private String skuName;
    private String skuDesc;
    private String catalog3Id;
    private String skuDefaultImg;
    private double hotScore;
    private String productId;
    private List<PmsSkuAttrValue> SkuAttrValueList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuDesc() {
        return skuDesc;
    }

    public void setSkuDesc(String skuDesc) {
        this.skuDesc = skuDesc;
    }

    public String getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(String catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public String getSkuDefaultImg() {
        return skuDefaultImg;
    }

    public void setSkuDefaultImg(String skuDefaultImg) {
        this.skuDefaultImg = skuDefaultImg;
    }

    public double getHotScore() {
        return hotScore;
    }

    public void setHotScore(double hotScore) {
        this.hotScore = hotScore;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<PmsSkuAttrValue> getSkuAttrValueList() {
        return SkuAttrValueList;
    }

    public void setSkuAttrValueList(List<PmsSkuAttrValue> skuAttrValueList) {
        SkuAttrValueList = skuAttrValueList;
    }

}
