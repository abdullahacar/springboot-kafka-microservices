package com.acr.orderservice.clients.feign;


import com.acr.basedomains.dto.OrderEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "stock-service", url = "http://localhost:8081")
public interface StockServiceFeignClient {

    @GetMapping(value = "/stock/test")
    ResponseEntity<String> test();

    @GetMapping(value = "/stock/getAllOrderEvents")
    ResponseEntity<List<OrderEvent>> getAllOrderEvents();

}
