package com.acr.orderservice.controller;

import com.acr.basedomains.dto.Order;
import com.acr.basedomains.dto.OrderEvent;
import com.acr.orderservice.clients.web.StockServiceWebClient;
import com.acr.orderservice.config.AppConfig;
import com.acr.orderservice.exceptions.OrderNotFoundException;
import com.acr.orderservice.clients.feign.StockServiceFeignClient;
import com.acr.orderservice.kafka.OrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private OrderProducer orderProducer;

    @Autowired
    @Qualifier("beanOrder")
    private Order order;

    @Autowired
    private StockServiceFeignClient stockServiceFeignClient;

    @Autowired
    private StockServiceWebClient stockServiceWebClient;

    public OrderController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @PostMapping("/orders")
    public String placeOrder(@RequestBody Order order) {

        order.setOrderId(UUID.randomUUID().toString());

        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setStatus("PENDING");
        orderEvent.setMessage("order status is in pending state");
        orderEvent.setOrder(order);

        orderProducer.sendMessage(orderEvent);

        return "Order placed successfully ...";
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getProduct(@PathVariable String id) {

        if (id == null) throw new OrderNotFoundException();

        return new ResponseEntity<>(Order.builder().build(), HttpStatus.OK);
    }

    @GetMapping("orders/getOrder")
    public ResponseEntity<Order> getOrder() {


        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        Order firstOrder = context.getBean("firstOrder", Order.class);
        Order secondOrder = context.getBean("secondOrder", Order.class);

        System.out.println(firstOrder.getName());

        System.out.println(secondOrder.getName());

        System.out.println(order.getName());

        return ResponseEntity.ok(secondOrder);

    }

    @GetMapping(value = "/orders/stock-service-test")
    public ResponseEntity<String> testStockService() {
        return stockServiceFeignClient.test();
    }

    @GetMapping(value = "/orders/getAllOrderEvents-sync")
    public ResponseEntity<List<OrderEvent>> testStockServiceWithFlux_sync() {
        return ResponseEntity.ok(stockServiceWebClient.getAllOrderEvents_sync());
    }

    @GetMapping(value = "/orders/getAllOrderEvents-async")
    public ResponseEntity<Flux<OrderEvent>> testStockServiceWithFlux_async() {
        return ResponseEntity.ok(stockServiceWebClient.getAllOrderEvents_async());
    }

    @GetMapping(value = "/orders/getAllOrderEvents-sync-delayed")
    public ResponseEntity<List<OrderEvent>> testStockServiceWithFlux_sync_delayed() {
        return ResponseEntity.ok(stockServiceWebClient.getAllOrderEvents_sync_wDelay());
    }

    // async request time-out exception
    @GetMapping(value = "/orders/getAllOrderEvents-async-delayed")
    public ResponseEntity<Flux<OrderEvent>> testStockServiceWithFlux_async_delayed() {
        return ResponseEntity.ok(stockServiceWebClient.getAllOrderEvents_async_wDelay());
    }

    @GetMapping(value = "/orders/getAllOrderEvents-async-delayed-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<OrderEvent>> testStockServiceWithFlux_async_delayed_stream() {
        return ResponseEntity.ok(stockServiceWebClient.getAllOrderEvents_async_wDelay());
    }



}
