package com.nimlabs.erp_lite.domain.order;

import java.util.Set;

/**
 * Estado del pedido con transiciones válidas.
 * Estados válidos: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
 * DELIVERED y CANCELLED son estados finales.
 *
 * @param value el valor del estado
 */
public record OrderStatus(String value) {

    public static final String PENDING = "PENDING";
    public static final String CONFIRMED = "CONFIRMED";
    public static final String SHIPPED = "SHIPPED";
    public static final String DELIVERED = "DELIVERED";
    public static final String CANCELLED = "CANCELLED";

    private static final Set<String> VALID_STATUSES = Set.of(
            PENDING,
            CONFIRMED,
            SHIPPED,
            DELIVERED,
            CANCELLED
    );

    private static final Set<String> FINAL_STATES = Set.of(DELIVERED, CANCELLED);

    public OrderStatus {
        if (value == null || !VALID_STATUSES.contains(value)) {
            throw new IllegalArgumentException(
                "Invalid OrderStatus: " + value + ". Must be one of: " + VALID_STATUSES
            );
        }
    }

    /**
     * Crea una instancia de OrderStatus a partir de un valor String.
     *
     * @param value el valor del estado
     * @return una nueva instancia de OrderStatus
     */
    public static OrderStatus of(String value) {
        return new OrderStatus(value);
    }

    public static OrderStatus pending() {
        return new OrderStatus(PENDING);
    }

    public static OrderStatus confirmed() {
        return new OrderStatus(CONFIRMED);
    }

    public static OrderStatus shipped() {
        return new OrderStatus(SHIPPED);
    }

    public static OrderStatus delivered() {
        return new OrderStatus(DELIVERED);
    }

    public static OrderStatus cancelled() {
        return new OrderStatus(CANCELLED);
    }

    /**
     * Valida si este estado puede transicionar al siguiente estado.
     * Transiciones válidas:
     * - PENDING -> CONFIRMED | CANCELLED
     * - CONFIRMED -> SHIPPED | CANCELLED
     * - SHIPPED -> DELIVERED
     * - DELIVERED -> (sin transiciones, estado final)
     * - CANCELLED -> (sin transiciones, estado final)
     *
     * @param nextStatus el estado destino
     * @return true si la transición es válida
     */
    public boolean canTransitionTo(OrderStatus nextStatus) {
        return switch (this.value) {
            case PENDING -> CONFIRMED.equals(nextStatus.value) || CANCELLED.equals(nextStatus.value);
            case CONFIRMED -> SHIPPED.equals(nextStatus.value) || CANCELLED.equals(nextStatus.value);
            case SHIPPED -> DELIVERED.equals(nextStatus.value);
            case DELIVERED -> false; // No transitions allowed from DELIVERED
            case CANCELLED -> false; // No transitions allowed from CANCELLED
            default -> false;
        };
    }

    public boolean isPending() {
        return PENDING.equals(this.value);
    }

    public boolean isConfirmed() {
        return CONFIRMED.equals(this.value);
    }

    public boolean isShipped() {
        return SHIPPED.equals(this.value);
    }

    public boolean isDelivered() {
        return DELIVERED.equals(this.value);
    }

    public boolean isCancelled() {
        return CANCELLED.equals(this.value);
    }

    public boolean isFinalState() {
        return FINAL_STATES.contains(this.value);
    }
}
