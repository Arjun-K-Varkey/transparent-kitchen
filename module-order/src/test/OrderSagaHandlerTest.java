package com.kitchen.order;

import com.kitchen.common.KitchenEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OrderSagaHandlerTest {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private SagaHandler sagaHandler;

    @Test
    void testHandleFailed_ShouldUpdateStatusToRejected() {
        // 1. Create and Save a dummy order
        // Ensure price/nutrition fields are set if your DB constraints require them
        Order testOrder = new Order();
        testOrder.setOrderStatus("PENDING");
        testOrder.setTotalPrice(10.0); 
        
        Order savedOrder = repository.save(testOrder).block(); 
        Long orderId = savedOrder.getId();

        // 2. Trigger the failure event manually
        KitchenEvent.OrderFailed failEvent = new KitchenEvent.OrderFailed(orderId, "Out of ingredients");
        sagaHandler.handleFailed(failEvent);

        // 3. Verify the status was changed in the DB
        // We use .expectNoEvent to give the background .subscribe() thread a moment to finish
        StepVerifier.create(repository.findById(orderId).delayElement(Duration.ofMillis(200)))
                .assertNext(order -> {
                    assertEquals("REJECTED", order.getOrderStatus(), "Status should have been updated to REJECTED");
                })
                .verifyComplete();
    }
}