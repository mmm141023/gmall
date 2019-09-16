package com.fendou.gmall.search.service.impl;

import com.fendou.gmall.bean.*;
import com.fendou.gmall.search.dao.PmsBaseAttrInfoMapper;
import com.fendou.gmall.service.SearchService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

/**
 * SearchServiceImpl class
 *
 * @author maochaoying
 * @date 2019/9/13
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    JestClient jestClient;
    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;


    /**
     * 根据pmsSearchParam实现查询商品功能
     *
     * @param pmsSearchParam
     * @return
     */
    @Override
    public List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam) {
        // 从索引库根据相关API查询数据并返回
        String dslStr = getSearchDsl(pmsSearchParam);
        String keyWord = pmsSearchParam.getKeyword();
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();

        Search search = new Search.Builder(dslStr).addIndex("gmall").addType("PmsSkuInfo").build();

        SearchResult execute = null;
        try {
            //execute 即为执行dsl语句后的结果
            execute = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = execute.getHits(PmsSearchSkuInfo.class);
        for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
            PmsSearchSkuInfo source = hit.source;
            Map<String, List<String>> highlight = hit.highlight;
            // 从highlight中取出关键字赋值给source 来展示
            if (StringUtils.isNotBlank(keyWord)) {
                String skuName = highlight.get("skuName").get(0);
                source.setSkuName(skuName);
            }
            pmsSearchSkuInfos.add(source);
        }
        return pmsSearchSkuInfos;
    }

    /**
     * 获得筛选商品列表
     *
     * @param pmsSearchSkuInfoList
     * @return
     */
    @Override
    public List<PmsBaseAttrInfo> getAttrValueAndAttrValueList(List<PmsSearchSkuInfo> pmsSearchSkuInfoList) {
        // 根据set不可重复的特性存储valueId去重
        Set<String> attrValueIds = new HashSet<>();
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfoList) {
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                String valueId = pmsSkuAttrValue.getValueId();
                attrValueIds.add(valueId);
            }
        }
        // 通过工具类将set集合转化为 用逗号分隔的字符串 以便进行联合查询
        String join = StringUtils.join(attrValueIds, ",");
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.selectAttrValueAndAttrValueList(join);
        return pmsBaseAttrInfos;
    }

    /**
     * 得到urlParam
     *
     * @param pmsSearchParam
     * @return
     */
    @Override
    public String getUrlParam(PmsSearchParam pmsSearchParam) {
        String urlParam = "";
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String keyword = pmsSearchParam.getKeyword();
        String[] skuAttrValueList = pmsSearchParam.getValueId();
        // 当为第一个参数时不要&符号，当不是第一个参数时加入&
        if (StringUtils.isNotBlank(catalog3Id)) {
            urlParam += ("catalog3Id=" + catalog3Id);
        } else {
            if (StringUtils.isNotBlank(keyword)) {
                urlParam += ("keyword=" + keyword);
            }
            if (skuAttrValueList != null) {
                for (String pmsSkuAttrValue : skuAttrValueList) {
                    urlParam += ("&valueId=" + pmsSkuAttrValue);
                }
            }
            return urlParam;
        }
        if (StringUtils.isNotBlank(keyword)) {
            urlParam += ("&keyword=" + keyword);
        }
        if (skuAttrValueList != null) {
            for (String pmsSkuAttrValue : skuAttrValueList) {
                urlParam += ("&valueId=" + pmsSkuAttrValue);
            }
        }
        return urlParam;
    }

    /**
     * 删除已经点击过的筛选条件
     * 使用iterator迭代器，对集合进行删除操作
     *
     * @param pmsSearchParam
     * @param pmsBaseAttrInfos
     * @return
     */
    @Override
    public List<PmsBaseAttrInfo> delClickedAttrValue(PmsSearchParam pmsSearchParam, List<PmsBaseAttrInfo> pmsBaseAttrInfos) {
        Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfos.iterator();
        String[] valueId = pmsSearchParam.getValueId();
        if (valueId != null) {
            while (iterator.hasNext()) {
                PmsBaseAttrInfo next = iterator.next();
                List<PmsBaseAttrValue> attrValueList = next.getAttrValueList();
                for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                    String id = pmsBaseAttrValue.getId();
                    for (String s : valueId) {
                        if (s.equals(id)) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
        return pmsBaseAttrInfos;
    }

    /**
     * 实现面包屑功能
     *
     * @param pmsSearchParam
     * @param pmsBaseAttrInfos
     * @return
     */
    @Override
    public List<PmsSearchCromb> getCrombs(PmsSearchParam pmsSearchParam, List<PmsBaseAttrInfo> pmsBaseAttrInfos) {
        List<PmsSearchCromb> pmsSearchCrombs = new ArrayList<>();
        String[] valueId = pmsSearchParam.getValueId();
        if (valueId != null) {
            for (String s : valueId) {
                // 每存在一个valueID说明点击了一次筛选条件，需要创建一个面包屑
                PmsSearchCromb pmsSearchCromb = new PmsSearchCromb();
                // 必须每次循环都创建一个iterator 否则下次循环拿不到名字。
                Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfos.iterator();
                while (iterator.hasNext()) {
                    PmsBaseAttrInfo next = iterator.next();
                    List<PmsBaseAttrValue> attrValueList = next.getAttrValueList();
                    for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                        String id = pmsBaseAttrValue.getId();
                        if (id.equals(s)) {
                            pmsSearchCromb.setValueName(pmsBaseAttrValue.getValueName());
                        }
                    }
                }
                pmsSearchCromb.setValueId(s);
                pmsSearchCromb.setUrlParam(getUrlParamByCromb(pmsSearchParam, s));
                pmsSearchCrombs.add(pmsSearchCromb);
            }
        }
        return pmsSearchCrombs;
    }

    /**
     * 生成面包屑的urlParam
     * if (!s.equals(pmsSkuAttrValue)) {
     * urlParam += ("&valueId=" + pmsSkuAttrValue);
     * }
     * 只有当当前面包屑的valueId不等于传来pmsSearchParam中的value时才加入valueID = pmsSkuAttrValue。
     *
     * @param pmsSearchParam
     * @param s
     * @return
     */
    private String getUrlParamByCromb(PmsSearchParam pmsSearchParam, String s) {
        String urlParam = "";
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String keyword = pmsSearchParam.getKeyword();
        String[] skuAttrValueList = pmsSearchParam.getValueId();

        if (StringUtils.isNotBlank(catalog3Id)) {
            urlParam += ("catalog3Id=" + catalog3Id);
        } else {
            if (StringUtils.isNotBlank(keyword)) {
                urlParam += ("keyword=" + keyword);
            }
            if (skuAttrValueList != null) {
                for (String pmsSkuAttrValue : skuAttrValueList) {
                    if (!s.equals(pmsSkuAttrValue)) {
                        urlParam += ("&valueId=" + pmsSkuAttrValue);
                    }
                }
            }
            return urlParam;
        }
        if (StringUtils.isNotBlank(keyword)) {
            urlParam += ("&keyword=" + keyword);
        }
        if (skuAttrValueList != null) {
            for (String pmsSkuAttrValue : skuAttrValueList) {
                if (!s.equals(pmsSkuAttrValue)) {
                    urlParam += ("&valueId=" + pmsSkuAttrValue);
                }
            }
        }
        return urlParam;
    }

    /**
     * 使用jest的dsl工具对skuAttrValueList / keyWord / catalog3Id进行语句生成
     *
     * @param pmsSearchParam
     * @return
     */
    private String getSearchDsl(PmsSearchParam pmsSearchParam) {
        String[] skuAttrValueList = pmsSearchParam.getValueId();
        String keyWord = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        //jest 的 dsl工具
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        // filter
        if (skuAttrValueList != null) {
            for (String pmsSkuAttrValue : skuAttrValueList) {
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("SkuAttrValueList.valueId", pmsSkuAttrValue);
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }
        if (StringUtils.isNotBlank(catalog3Id)) {
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id", catalog3Id);
            boolQueryBuilder.filter(termQueryBuilder);
        }
        //must
        if (StringUtils.isNotBlank(keyWord)) {
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName", keyWord);
            boolQueryBuilder.must(matchQueryBuilder);
        }
        //query
        searchSourceBuilder.query(boolQueryBuilder);
        //from
        searchSourceBuilder.from(0);
        //size
        searchSourceBuilder.size(20);
//        highlight
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("skuName");
        highlightBuilder.preTags("<span style='color:red;'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);
        //sort
        searchSourceBuilder.sort("id", SortOrder.DESC);
        return searchSourceBuilder.toString();
    }
}
