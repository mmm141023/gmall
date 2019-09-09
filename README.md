# gmall
商城实战项目

gmall-user-service  8070
gmall-user-web  8080

gmall-manage-service  8071
gmall-manage-web  8081

gmall-search-service 8075
gmall-search-web 8085
g

# gmall-item-service   前台的商品详情服务 8073  其中的service与gmall-manage-service相冲突
gmall-item-web  前台的商品详情展示 8083

# 销售属性高亮选择的sql

SELECT 
sa.*,sav.*,if(ssav.sku_id,1,0) as isChecked
FROM
pms_product_sale_attr sa
INNER JOIN pms_product_sale_attr_value sav ON
sa.product_id = sav.product_id
AND sa.sale_attr_id = sav.sale_attr_id AND sa.product_id = 70
LEFT JOIN pms_sku_sale_attr_value ssav
on sav.id = ssav.sale_attr_value_id AND ssav.sku_id = 108
