package com.hua.college.hotel.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.college.hotel.mapper.HotelMapper;
import com.hua.college.hotel.pojo.Hotel;
import com.hua.college.hotel.pojo.HotelDoc;
import com.hua.college.hotel.pojo.PageResult;
import com.hua.college.hotel.pojo.RequestParams;
import com.hua.college.hotel.service.IHotelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class HotelService extends ServiceImpl<HotelMapper, Hotel> implements IHotelService {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    //导入RabbitTemplate
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
    /**
     *  根据params参数构建分页查询的请求
     * @param params
     * @return
     */
    @Override
    public PageResult search(RequestParams params) {
        try {
            // 1.准备Request
            SearchRequest request = new SearchRequest("hotel");
            // 2.准备请求参数
            // 2.1.query
            buildBasicQuery(params, request);
            // 2.2.分页
            int page = params.getPage();
            int size = params.getSize();
            request.source().from((page - 1) * size).size(size);
            // 2.3.高亮
            request.source().highlighter(new HighlightBuilder()
                    .field("name").field("address").field("business")
                    .requireFieldMatch(false)
                    .preTags("<span style=\"color: red;\">").postTags("</span>"))
                    ;
//            request.source().highlighter(new HighlightBuilder().field("address").requireFieldMatch(false));
//            request.source().highlighter(new HighlightBuilder().field("business").requireFieldMatch(false));


            // 3.发送请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 4.解析响应
            return handleResponse(response);
        } catch (IOException e) {
            throw new RuntimeException("搜索数据失败", e);
        }
    }

    /**
     * 在buildBasicQuery()方法构建的查询条件下，对city，brand，starName进行聚合
     * @param params
     * @return
     */
    @Override
    public Map<String,List<String>> searchFilters(RequestParams params) {
        try {
            // 1.准备Request
            SearchRequest request = new SearchRequest("hotel");
            // 2.准备请求参数
            // 2.1.query
            buildBasicQuery(params, request);
            // 2.2.分页
            int page = params.getPage();
            int size = params.getSize();
            request.source().from((page - 1) * size).size(size);
            // 2.3.聚合
            request.source().aggregation(AggregationBuilders.terms("brandAgg").field("brand"))
                    .aggregation(AggregationBuilders.terms("cityAgg").field("city"))
                    .aggregation(AggregationBuilders.terms("starNameAgg").field("starName"));
            // 3.发送请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 4.解析响应
            Aggregations aggregations = response.getAggregations();
            Terms brandTerms = aggregations.get("brandAgg");
            Terms cityTerms = aggregations.get("cityAgg");
            Terms starNameTerms = aggregations.get("starNameAgg");
            List<String> brandList = new ArrayList<>();
            List<String> cityList = new ArrayList<>();
            List<String> starNameList = new ArrayList<>();
            brandTerms.getBuckets().forEach(bucket -> {
                brandList.add(bucket.getKeyAsString());
            });
            cityTerms.getBuckets().forEach(bucket -> {
                cityList.add(bucket.getKeyAsString());
            });
            starNameTerms.getBuckets().forEach(bucket -> {
                starNameList.add(bucket.getKeyAsString());
            });
            Map<String,List<String>> map = new java.util.HashMap<>();
            map.put("brand",brandList);
            map.put("city",cityList);
            map.put("starName",starNameList);
            return map;
        } catch (IOException e) {
            throw new RuntimeException("搜索数据失败", e);
        }
    }

    /**
     * 根据前缀获取联想词，并解析响应
     * @param prefix
     * @return
     */
    @Override
    public List<String> getSuggestion(String prefix) throws IOException {
        if(StringUtils.isEmpty(prefix)){
            throw new RuntimeException("前缀不能为空");
//            return null;
        }
        SearchRequest searchRequest = new SearchRequest("hotel");
        searchRequest.source().suggest(
                new org.elasticsearch.search.suggest.SuggestBuilder()
                        .addSuggestion("suggestion",
                                org.elasticsearch.search.suggest.SuggestBuilders
                                        .completionSuggestion("suggestion").text(prefix).size(10).skipDuplicates(true))
        );
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        CompletionSuggestion suggestion = response.getSuggest().getSuggestion("suggestion");
        List<String> list = new ArrayList<>();
        //这样遍历，效率低
        suggestion.getOptions().forEach(option -> {
            list.add(option.getText().toString());
        });
        return list;
    }


    @Override
    public HotelDoc deleteFromEs(HotelDoc hotelDoc) {
        try {
            // 1.准备Request对象
            DeleteRequest request = new DeleteRequest("hotel").id(hotelDoc.getId().toString());
            // 3.发送请求
            restHighLevelClient.delete(request, RequestOptions.DEFAULT);
            return hotelDoc;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HotelDoc updateFromEs(Hotel hotel) {
        if(hotel.getId() == null){
            throw new RuntimeException("id不能为空");
        }
        HotelDoc hotelDoc = new HotelDoc(hotel);
        UpdateRequest request = new UpdateRequest("hotel", hotelDoc.getId().toString());
        request.doc(JSON.toJSONString(hotelDoc), XContentType.JSON);
        try {
            restHighLevelClient.update(request, RequestOptions.DEFAULT);
            restHighLevelClient.search(new SearchRequest("hotel"), RequestOptions.DEFAULT);
            return hotelDoc;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HotelDoc insertIntoEs(Hotel hotel) {
        HotelDoc hotelDoc = new HotelDoc(hotel);
        IndexRequest request = new IndexRequest("hotel")/*.id(hotelDoc.getId().toString())*/;
        request.source(JSON.toJSONString(hotelDoc), XContentType.JSON);
        try {
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
            return hotelDoc;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void buildBasicQuery(RequestParams params, SearchRequest request) {
        // 1.准备Boolean查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // 1.1.关键字搜索，match查询，放到must中
        String key = params.getKey();
        if (StringUtils.isNotBlank(key)) {
            // 不为空，根据关键字查询
            boolQuery.must(QueryBuilders.matchQuery("all", key));
        } else {
            // 为空，查询所有
            boolQuery.must(QueryBuilders.matchAllQuery());
        }

        // 1.2.品牌
        String brand = params.getBrand();
        if (StringUtils.isNotBlank(brand)) {
            boolQuery.filter(QueryBuilders.termQuery("brand", brand));
        }
        // 1.3.城市
        String city = params.getCity();
        if (StringUtils.isNotBlank(city)) {
            boolQuery.filter(QueryBuilders.termQuery("city", city));
        }
        // 1.4.星级
        String starName = params.getStarName();
        if (StringUtils.isNotBlank(starName)) {
            boolQuery.filter(QueryBuilders.termQuery("starName", starName));
        }
        // 1.5.价格范围
        Integer minPrice = params.getMinPrice();
        Integer maxPrice = params.getMaxPrice();
        if (minPrice != null && maxPrice != null) {
            maxPrice = maxPrice == 0 ? Integer.MAX_VALUE : maxPrice;
            boolQuery.filter(QueryBuilders.rangeQuery("price").gte(minPrice).lte(maxPrice));
        }

//        // 2.算分函数查询
//        FunctionScoreQueryBuilder functionScoreQuery = QueryBuilders.functionScoreQuery(
//                boolQuery, // 原始查询，boolQuery
//                new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{ // function数组
//                        new FunctionScoreQueryBuilder.FilterFunctionBuilder(
//                                QueryBuilders.termQuery("isAD", true), // 过滤条件
//                                ScoreFunctionBuilders.weightFactorFunction(10) // 算分函数
//                        )
//                }
//        );

        // 3.设置查询条件
        request.source().query(boolQuery);
    }

    private PageResult handleResponse(SearchResponse response) {
        SearchHits searchHits = response.getHits();
        // 4.1.总条数
        long total = searchHits.getTotalHits().value;
        // 4.2.获取文档数组
        SearchHit[] hits = searchHits.getHits();
        // 4.3.遍历
        List<HotelDoc> hotels = new ArrayList<>(hits.length);
        for (SearchHit hit : hits) {
            // 4.4.获取source
            String json = hit.getSourceAsString();
            // 4.5.反序列化，非高亮的
            HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
            // 4.6.处理高亮结果
            // 1)获取高亮map
            Map<String, HighlightField> map = hit.getHighlightFields();
            if (map != null && !map.isEmpty()) {
                // 2）根据字段名，获取高亮结果
                HighlightField highlightField = map.get("name");
                HighlightField addressHighlightField = map.get("address");
                HighlightField businessHighlightField = map.get("business");
                String pre="";
                String post="";
                if (highlightField != null) {
                    // 3）获取高亮结果字符串数组中的第1个元素

                    String hName =pre+ highlightField.getFragments()[0].toString()+post;
                    // 4）把高亮结果放到HotelDoc中
                    hotelDoc.setName(hName);
                }
                if(addressHighlightField != null){
                    String hAddress =pre+ addressHighlightField.getFragments()[0].toString() +post;
                    hotelDoc.setAddress(hAddress);
                }
                if(Objects.nonNull(businessHighlightField)){
                    String hBusiness =pre+ businessHighlightField.getFragments()[0].toString() +post;
                    hotelDoc.setBusiness(hBusiness);
                }
            }
            // 4.8.排序信息
            Object[] sortValues = hit.getSortValues();
            if (sortValues.length > 0) {
                hotelDoc.setDistance(sortValues[0]);
            }

            // 4.9.放入集合
            hotels.add(hotelDoc);
        }
        return new PageResult(total, hotels);
    }


    @Override
    public void insertById(Long id) {
        try {
            // 0.根据id查询酒店数据
            Hotel hotel = getById(id);
            // 转换为文档类型
            HotelDoc hotelDoc = new HotelDoc(hotel);

            // 1.准备Request对象
            IndexRequest request = new IndexRequest("hotel").id(hotel.getId().toString());
            // 2.准备Json文档
            request.source(JSON.toJSONString(hotelDoc), XContentType.JSON);
            // 3.发送请求
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
