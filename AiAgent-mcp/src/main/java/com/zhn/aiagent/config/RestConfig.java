package com.zhn.aiagent.config;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * JDK 默认的 {@link java.net.HttpURLConnection} 在遇到 smartbox 等站的 301 时，
 * 有时无法正确跟随重定向，会把 HTML 错误页当作响应体返回。
 * 使用 HttpClient 5 作为请求工厂可稳定跟随重定向。
 */
@Configuration
public class RestConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        var connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnTotal(64)
                .setMaxConnPerRoute(32)
                .build();

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofSeconds(10))
                .setConnectTimeout(Timeout.ofSeconds(10))
                .setResponseTimeout(Timeout.ofSeconds(30))
                .build();

        HttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .disableCookieManagement()
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return builder
                .requestFactory(() -> factory)
                .defaultHeader(
                        HttpHeaders.USER_AGENT,
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36")
                .build();
    }
}
