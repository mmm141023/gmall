package com.fendou.gmall.manage.dao;

import com.fendou.gmall.bean.PmsProductSaleAttr;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * PmsProductSaleAttrMapper class
 *
 * @author maochaoying
 * @date 2019/9/3
 */

public interface PmsProductSaleAttrMapper extends Mapper<PmsProductSaleAttr> {
    /**
     * 联合查询
     *
             SELECT
             sa.*,sav.*,if(ssav.sku_id,1,0) as isChecked
             FROM
             pms_product_sale_attr sa
             INNER JOIN pms_product_sale_attr_value sav ON
             sa.product_id = sav.product_id
             AND sa.sale_attr_id = sav.sale_attr_id AND sa.product_id = 70
             LEFT JOIN pms_sku_sale_attr_value ssav
             on sav.id = ssav.sale_attr_value_id AND ssav.sku_id = 108
     * @param productId
     * @param skuId
     * @return
     */
    List<PmsProductSaleAttr> SelectSpuSaleAttrListCheckBySku(@Param("productId") String productId,@Param("skuId") String skuId);
}
