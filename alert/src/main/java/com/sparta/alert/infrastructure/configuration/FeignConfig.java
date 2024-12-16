package com.sparta.alert.infrastructure.configuration;

import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class FeignConfig {

    @Bean
    public SpringEncoder feignEncoder() {
        return new SpringEncoder(() -> new MappingJackson2HttpMessageConverter());
    }

    @Bean
    public SpringMvcContract feignContract() {
        return new SpringMvcContract();
    }
}
