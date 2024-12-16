package com.sparta.msa.delivery.infrastructure.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DeliveryApplicationQueryDslConfig {
    @Bean
    JPAQueryFactory jpaQueryFactory(EntityManager em){

        return new JPAQueryFactory(em);
    }

}
