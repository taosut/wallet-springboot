package org.blockchain.wallet.resttemplate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestConfiguration {

    @Bean
    public RestTemplate restTemplate() {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate(requestFactory);
        restTemplate.setInterceptors(Collections.singletonList(new UserAgentInterceptor()));

        return restTemplate;
    }
}
