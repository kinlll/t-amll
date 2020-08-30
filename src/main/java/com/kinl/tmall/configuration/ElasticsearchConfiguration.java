package com.kinl.tmall.configuration;

import com.alibaba.fastjson.JSON;
import com.kinl.tmall.enums.ResultEnum;
import com.kinl.tmall.exception.AllException;
import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
@Data
@ConfigurationProperties(prefix = "elastic-search")
@PropertySource("elasticsearch.properties")
@Component
public class ElasticsearchConfiguration {

   //ES集群名
   private String clusterName;

   //ES查询的索引名称
   private String index;

   //ES集群中节点的域名或IP
   private String ip;

   //ES连接端口号
   private Integer port;

   @Bean
   public RestHighLevelClient getClient(){
      return new RestHighLevelClient(RestClient.builder(new HttpHost(ip, port, "http")));
   }

   //判断是否存在index
   public boolean indexExist() throws IOException {
      RestHighLevelClient client = getClient();
      GetIndexRequest request = new GetIndexRequest(index);
      return client.indices().exists(request, RequestOptions.DEFAULT);
   }


   //添加
   public void add(Object object){
      try {
         IndexRequest request = new IndexRequest(index);
         boolean b = indexExist();
         if (!b) {
            createIndex();
         }
         request.source(JSON.toJSON(object), XContentType.JSON);
         getClient().index(request, RequestOptions.DEFAULT);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   //删除
   public void delete(Integer id){
      try {
         DeleteRequest request = new DeleteRequest(index, "_doc", String.valueOf(id));
         boolean b = indexExist();
         if (!b) {
            createIndex();
            return;
         }
         DeleteResponse delete = getClient().delete(request, RequestOptions.DEFAULT);
         if (delete.status() == RestStatus.OK) {
            System.out.println("----------删除文档成功！！------------");
         }else {
            throw new AllException(ResultEnum.DELETE_DOCUMENT_ERROR);
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   //添加索引
   public void createIndex(){
      try {
         boolean b = indexExist();
         if (b) {
            return;
         }
         XContentBuilder builder = XContentFactory.jsonBuilder();
         builder.startObject()
                 .field("properties")
                 .startObject()
                 .field("id").startObject().field("type","integer").endObject()
                 .field("name").startObject().field("type","text").field("analyzer","ik_max_word").endObject()
                 .field("subtitle").startObject().field("type","text").field("analyzer","ik_max_word").endObject()
                 .endObject()
                 .endObject();
         CreateIndexRequest request = new CreateIndexRequest(index);
         request.mapping(builder);
         CreateIndexResponse createIndexResponse = getClient().indices().create(request, RequestOptions.DEFAULT);
         if (!createIndexResponse.isAcknowledged()) {
            throw new AllException(ResultEnum.CREATE_INDEX_ERROR);
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   //搜索
   public SearchHits search(String search){
      try {

         BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
         SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
         MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", search);
         MatchQueryBuilder matchQueryBuilder1 = QueryBuilders.matchQuery("subtitle", search);

         boolBuilder.should(matchQueryBuilder);
         boolBuilder.should(matchQueryBuilder1);
         sourceBuilder.query(boolBuilder);
         sourceBuilder.size(1000);
         sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

         SearchRequest request = new SearchRequest(index);
         //request.searchType("_doc");
         request.source(sourceBuilder);

         SearchResponse response = getClient().search(request, RequestOptions.DEFAULT);

         return response.getHits();
      } catch (IOException e) {
         e.printStackTrace();
         throw new AllException(ResultEnum.SEARCH_ERROR);
      }
   }

}
