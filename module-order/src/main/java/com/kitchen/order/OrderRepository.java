package com.kitchen.order;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {

    @Modifying
    @Query("UPDATE orders SET order_status = :status WHERE id = :id")
    Mono<Integer> updateStatus(String status,Long id);
}