package com.boot.security.service.impl;

import com.alibaba.fastjson.JSON;
import com.boot.security.domain.Book;
import com.boot.security.service.ElasticsearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Autowired
    RestHighLevelClient client;

    @Value("${Elasticsearch.BookIndexName}")
    String indexName;

    @Override
    public List<Book> listBook(String keyword) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(QueryBuilders.matchQuery("bname",keyword)).
                should(QueryBuilders.matchQuery("bauthor",keyword)).
                should(QueryBuilders.matchQuery("btype",keyword)).
                should(QueryBuilders.matchQuery("ISBN",keyword)).
                should(QueryBuilders.fuzzyQuery("bdes",keyword).fuzziness(Fuzziness.AUTO));
        SearchSourceBuilder sourceBuilder=new SearchSourceBuilder().query(boolQueryBuilder);
        SearchRequest request=new SearchRequest(indexName).source(sourceBuilder);
        ArrayList<Book> list = new ArrayList<>();
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            for (SearchHit hit : response.getHits()) {
                list.add(JSON.parseObject(hit.getSourceAsString(), Book.class));
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void deleteBookOfES(Integer bId) throws IOException {
        DeleteRequest request=new DeleteRequest(indexName).id(String.valueOf(bId));
        client.delete(request,RequestOptions.DEFAULT);
    }

    @Override
    public void updateBookOfES(Book book) throws Exception {
        String id= String.valueOf(book.getBId());
        IndexRequest request = new IndexRequest(indexName).
                id(id).
                source(new ObjectMapper().writeValueAsString(book), XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
    }
    @Override
    public void addBookOfES(Book book) throws IOException {
        IndexRequest request=new IndexRequest(indexName).id(String.valueOf(book.getBId()))
                .source(new ObjectMapper().writeValueAsString(book),XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
    }


}
