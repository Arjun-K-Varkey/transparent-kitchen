package com.kitchen.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitchen.common.SagaBus;
import com.kitchen.common.KitchenEvent;
import com.kitchen.order.Order;
import com.kitchen.order.OrderRepository;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import io.r2dbc.postgresql.codec.Json;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final SagaBus sagaBus;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper; // Added for JSON serialization

    public OrderController(SagaBus sagaBus, OrderRepository orderRepository, ObjectMapper objectMapper) {
        this.sagaBus = sagaBus;
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public Mono<OrderResponse> placeOrder(@RequestBody OrderRequest request) {
        // Convert the request record into a JSON string
        String requestJson;
        try {
            requestJson = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            // Fallback to empty JSON if serialization fails
            requestJson = "{}";
        }

        // 1. Create the order with the request data in the metadata column
        Order newOrder = new Order(
            null, 
            request.price(), 
            "PENDING", 
            request.protein(), 
            request.fiber(),
            request.calories(), 
            Json.of(requestJson) // Metadata now contains the full request
        );

        // 2. Save to DB and trigger Saga
        return orderRepository.save(newOrder)
            .doOnSuccess(savedOrder -> {
                sagaBus.emit(new KitchenEvent.OrderPlaced(
                    savedOrder.id(),
                    request.calories(), 
                    request.protein(), 
                    request.fiber()
                ));
            })
            .map(saved -> new OrderResponse(saved.id(), "Order initiated with metadata stored."));
    }
}

record OrderRequest(Double price, Integer calories, Integer protein, Integer fiber) {}
record OrderResponse(Long orderId, String message) {}