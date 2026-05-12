package com.nimlabs.erp_lite.domain.order;

import java.util.Set;

public record OrderStatus(String value) {

    private static final Set<String> VALID_VALUES =
        Set.of("PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED");

    public OrderStatus {
        if (value == null || !VALID_VALUES.contains(value))
            throw new IllegalArgumentException(
                "Invalid OrderStatus: " + value + ". Must be one of: " + VALID_VALUES);
    }

    public static OrderStatus of(String value) {
        return new OrderStatus(value);
    }

    public static OrderStatus pending() { return new OrderStatus("PENDING"); }
    public static OrderStatus confirmed() { return new OrderStatus("CONFIRMED"); }
    public static OrderStatus shipped() { return new OrderStatus("SHIPPED"); }
    public static OrderStatus delivered() { return new OrderStatus("DELIVERED"); }
    public static OrderStatus cancelled() { return new OrderStatus("CANCELLED"); }

    public boolean canTransitionTo(OrderStatus next) {
        return switch (this.value) {
            case "PENDING"   -> Set.of("CONFIRMED", "CANCELLED").contains(next.value);
            case "CONFIRMED" -> Set.of("SHIPPED", "CANCELLED").contains(next.value);
            case "SHIPPED"   -> "DELIVERED".equals(next.value);
            default          -> false; // DELIVERED and CANCELLED are final states
        };
    }

    public boolean isPending()    { return "PENDING".equals(value); }
    public boolean isConfirmed()  { return "CONFIRMED".equals(value); }
    public boolean isShipped()    { return "SHIPPED".equals(value); }
    public boolean isDelivered()  { return "DELIVERED".equals(value); }
    public boolean isCancelled()  { return "CANCELLED".equals(value); }
    public boolean isFinalState() { return isDelivered() || isCancelled(); }
}
