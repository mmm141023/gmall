package com.fendou.gmall.bean;

import java.io.Serializable;

/**
 * PmsSearchCromb class
 *
 * @author maochaoying
 * @date 2019/9/15
 */

public class PmsSearchCromb implements Serializable {
    private String valueId;
    private String valueName;
    private String urlParam;

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getUrlParam() {
        return urlParam;
    }

    public void setUrlParam(String urlParam) {
        this.urlParam = urlParam;
    }
}
