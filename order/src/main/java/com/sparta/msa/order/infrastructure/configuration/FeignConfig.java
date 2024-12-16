package com.sparta.msa.order.infrastructure.configuration;

import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;

@Configuration
public class FeignConfig {

    @Bean
    public SpringEncoder feignEncoder() {
        HttpMessageConverter<?> jacksonConverter = new MappingJackson2HttpMessageConverter();
        return new SpringEncoder(() -> new HttpMessageConverters(jacksonConverter));
    }

    @Bean
    public SpringMvcContract feignContract() {
        return new SpringMvcContract();
    }
}
