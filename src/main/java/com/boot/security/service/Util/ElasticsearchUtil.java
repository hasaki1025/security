package com.boot.security.service.Util;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchUtil {


    @Value("${Elasticsearch.host}")
    String host;

    @Value("${Elasticsearch.port}")
    Integer port;

    @Bean
    RestHighLevelClient createRestClient()
    {
        RestClientBuilder builder= RestClient.builder(new HttpHost(host,port));
        return new RestHighLevelClient(builder);
    }

}
