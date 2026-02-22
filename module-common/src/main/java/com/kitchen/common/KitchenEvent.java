package com.kitchen.common;

/**
 * The Kitchen Saga Events.
 * Updated to use Long to match the database BigInt IDs (1, 2, 3).
 */
public sealed interface KitchenEvent permits 
    KitchenEvent.OrderPlaced, 
    KitchenEvent.NutritionVerified, 
    KitchenEvent.OrderFailed, 
    KitchenEvent.OrderCompleted {

    // CHANGE: UUID -> Long
    Long orderId();

    record OrderPlaced(Long orderId, int calories, int protein, int fiber) implements KitchenEvent {}
    record NutritionVerified(Long orderId, String integrityHash) implements KitchenEvent {}
    record OrderFailed(Long orderId, String reason) implements KitchenEvent {}
    record OrderCompleted(Long orderId) implements KitchenEvent {}
}