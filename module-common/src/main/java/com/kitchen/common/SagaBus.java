package com.kitchen.common;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class SagaBus {
    // Multicast sink allows multiple modules (Order, Nutrition) to listen simultaneously
    private final Sinks.Many<KitchenEvent> bus = Sinks.many().multicast().onBackpressureBuffer();

    public void emit(KitchenEvent event) {
        bus.tryEmitNext(event);
    }

    public Flux<KitchenEvent> getEvents() {
        return bus.asFlux();
    }
}