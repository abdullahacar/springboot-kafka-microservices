package com.acr.stockservice.controller;

import com.acr.basedomains.dto.Order;
import com.acr.basedomains.dto.OrderEvent;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/stock")
public class StockController {

    private static List<OrderEvent> orderEventsRepository = new ArrayList<>();

    static {
        IntStream.range(0, 100).forEach(value -> orderEventsRepository.add(OrderEvent.builder()
                .message(RandomStringUtils.random(1))
                .status(RandomStringUtils.random(1))
                .order(Order.builder()
                        .name(RandomStringUtils.random(1))
                        .price(value)
                        .qty(value)
                        .orderId(UUID.randomUUID().toString()).build())
                .build()));
    }

    @GetMapping("/test")
    public ResponseEntity<String> getResponse() {

        return ResponseEntity.ok("Stock Service Test");

    }

    @GetMapping("/getAllOrderEvents")
    public ResponseEntity<List<OrderEvent>> getAllOrderEvents() {

        return ResponseEntity.ok(orderEventsRepository);

    }


}
