package com.nimlabs.erp_lite.domain.order;

import java.util.UUID;

/**
 * Identificador único para el aggregate Order.
 *
 * @param value el valor UUID
 */
public record OrderId(UUID value) {

    public OrderId {
        if (value == null) throw new IllegalArgumentException("OrderId cannot be null");
    }

    /**
     * Crea una instancia de OrderId a partir de un valor UUID.
     *
     * @param value el valor UUID
     * @return una nueva instancia de OrderId
     */
    public static OrderId of(UUID value) {
        return new OrderId(value);
    }

    /**
     * Genera un nuevo OrderId aleatorio.
     *
     * @return una nueva instancia de OrderId con un UUID aleatorio
     */
    public static OrderId generate() {
        return new OrderId(UUID.randomUUID());
    }
}
