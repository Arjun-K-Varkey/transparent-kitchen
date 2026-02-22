package com.kitchen.nutrition;

import com.kitchen.common.KitchenEvent;
import com.kitchen.common.SagaBus;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
public class NutritionValidator {
    private final SagaBus sagaBus;

    public NutritionValidator(SagaBus sagaBus) {
        this.sagaBus = sagaBus;
    }

    @PostConstruct
    public void startListening() {
        sagaBus.getEvents()
            .ofType(KitchenEvent.OrderPlaced.class)
            .subscribe(this::verifyIntegrity);
    }

    private void verifyIntegrity(KitchenEvent.OrderPlaced event) {
        // Business Logic: WHO Standard Verification
        if (event.protein() > 5) {
            sagaBus.emit(new KitchenEvent.NutritionVerified(event.orderId(), "HASH-Varkey-2026"));
        } else {
            sagaBus.emit(new KitchenEvent.OrderFailed(event.orderId(), "Integrity Failure: Low Protein"));
        }
    }
}