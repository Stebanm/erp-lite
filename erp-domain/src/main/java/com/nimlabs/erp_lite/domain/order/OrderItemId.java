package com.nimlabs.erp_lite.domain.order;

import java.util.UUID;

/**
 * Identificador único para la entidad OrderItem.
 *
 * @param value el valor UUID
 */
public record OrderItemId(UUID value) {

    public OrderItemId {
        if (value == null) throw new IllegalArgumentException("OrderItemId cannot be null");
    }

    /**
     * Crea una instancia de OrderItemId a partir de un valor UUID.
     *
     * @param value el valor UUID
     * @return una nueva instancia de OrderItemId
     */
    public static OrderItemId of(UUID value) {
        return new OrderItemId(value);
    }

    /**
     * Genera un nuevo OrderItemId aleatorio.
     *
     * @return una nueva instancia de OrderItemId con un UUID aleatorio
     */
    public static OrderItemId generate() {
        return new OrderItemId(UUID.randomUUID());
    }
}
