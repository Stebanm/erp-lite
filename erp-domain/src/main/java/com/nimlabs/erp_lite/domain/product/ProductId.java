package com.nimlabs.erp_lite.domain.product;

import java.util.UUID;

/**
 * Identificador único para el agregado Product.
 *
 * @param value el valor UUID
 */
public record ProductId(UUID value) {

    public ProductId {
        if (value == null) throw new IllegalArgumentException("ProductId cannot be null");
    }

    /**
     * Crea una instancia de ProductId a partir de un valor UUID.
     *
     * @param value el valor UUID
     * @return una nueva instancia de ProductId
     */
    public static ProductId of(UUID value) {

        return new ProductId(value);
    }

    /**
     * Genera un nuevo ProductId aleatorio.
     *
     * @return una nueva instancia de ProductId con un UUID aleatorio
     */
    public static ProductId generate() {

        return new ProductId(UUID.randomUUID());
    }
}
