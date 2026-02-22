package com.kitchen.order;

import com.kitchen.common.KitchenEvent;
import com.kitchen.common.SagaBus;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
public class OrderSagaHandler {
    private final SagaBus sagaBus;
    private final OrderRepository repository;

    public OrderSagaHandler(SagaBus sagaBus, OrderRepository repository) {
        this.sagaBus = sagaBus;
        this.repository = repository;
    }

    @PostConstruct
    public void listen() {
        sagaBus.getEvents().subscribe(event -> {
            // Java 25 Pattern Matching for Switch
            switch (event) {
                case KitchenEvent.NutritionVerified verified -> handleVerified(verified);
                case KitchenEvent.OrderFailed failed -> handleFailed(failed);
                default -> {} 
            }
        });
    }

    private void handleVerified(KitchenEvent.NutritionVerified event) {
        repository.updateStatus("verified",event.orderId())
        .doOnError(e -> System.err.println("Failed to update order: " + e.getMessage()))
        .doOnSuccess(rowsAffected -> System.out.println("Order " + event.orderId() + " marked as COMPLETED. Rows: " + rowsAffected))
        .subscribe();
    }

    private void handleFailed(KitchenEvent.OrderFailed event) {
        repository.updateStatus("rejected",event.orderId())
        .doOnError(e -> System.err.println("Failed to update order: " + e.getMessage()))
        .doOnSuccess(rowsAffected -> System.out.println("Order " + event.orderId() + " marked as REJECTED. Rows: " + rowsAffected))
        .subscribe();
    }
}