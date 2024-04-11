package com.desafioitau.api.transferencia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${webclient.baseurl}")
    private String baseUrl;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder().baseUrl(baseUrl);
    }

}
