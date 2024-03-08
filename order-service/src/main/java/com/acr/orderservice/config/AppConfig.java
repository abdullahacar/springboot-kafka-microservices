package com.acr.orderservice.config;

import com.acr.basedomains.dto.Order;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    @Qualifier("beanOrder")
    public Order beanOrder() {
        return Order.builder().name("Bean Order").build();
    }

    @Bean
    public Order firstOrder() {
        return Order.builder().name("First Order").build();
    }

    @Bean
    public Order secondOrder() {
        return Order.builder().name("Second Order").build();
    }


}
