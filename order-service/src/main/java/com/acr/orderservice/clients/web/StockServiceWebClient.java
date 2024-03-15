package com.acr.orderservice.clients.web;


import com.acr.basedomains.dto.OrderEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockServiceWebClient {

    private static final String BASE_URL = "http://localhost:8081";

    private WebClient webClient;

    public StockServiceWebClient() {
        this.webClient = WebClient.builder().baseUrl(BASE_URL).build();
    }

    private static void sleepExecution(OrderEvent i) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public List<OrderEvent> getAllOrderEvents_sync() {

        return webClient
                .get()
                .uri("/stock/getAllOrderEvents")
                .retrieve()
                .bodyToFlux(OrderEvent.class).collectList()
                .block();
    }

    public List<OrderEvent> getAllOrderEvents_sync_wDelay() {

        List<OrderEvent> block = webClient
                .get()
                .uri("/stock/getAllOrderEvents")
                .retrieve()
                .bodyToFlux(OrderEvent.class).collectList()
                .block();

        if (block != null) {
            return block.stream()
                    .peek(StockServiceWebClient::sleepExecution)
                    .peek(orderEvent -> System.out.println(orderEvent.getOrder()))
                    .collect(Collectors.toList());
        }

        return List.of();

    }

    public Flux<OrderEvent> getAllOrderEvents_async() {

        return webClient
                .get()
                .uri("/stock/getAllOrderEvents")
                .retrieve()
                .bodyToFlux(OrderEvent.class);
    }

    public Flux<OrderEvent> getAllOrderEvents_async_wDelay() {

        List<OrderEvent> block = webClient
                .get()
                .uri("/stock/getAllOrderEvents")
                .retrieve()
                .bodyToFlux(OrderEvent.class).collectList()
                .block();

        assert block != null;

        return Flux.range(0, block.size())
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(System.out::println)
                .map(block::get);
    }
}
