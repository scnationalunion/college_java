package com.hua.college.hotel.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsConfig {

    @Value("${spring.elasticsearch.rest.ip}")
    private String ip;

    @Value("${spring.elasticsearch.rest.port}")
    private int port;

    @Value("${spring.elasticsearch.rest.scheme}")
    private String scheme;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        //RestClient.builder()可接收一个或多个HttpHost
        return new RestHighLevelClient(RestClient.builder(
                new HttpHost(ip, port, scheme)
        ));
    }
}
