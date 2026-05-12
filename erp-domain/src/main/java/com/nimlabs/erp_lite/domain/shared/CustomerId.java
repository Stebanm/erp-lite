package com.nimlabs.erp_lite.domain.shared;

/**
 * Referencia al sistema externo de clientes (JSONPlaceholder).
 * Representa un identificador único para un cliente.
 *
 * @param value el valor del ID del cliente (debe ser > 0)
 */
public record CustomerId(Long value) {

    public CustomerId {
        if (value == null) {
            throw new IllegalArgumentException("CustomerId cannot be null");
        }

        if (value <= 0) {
            throw new IllegalArgumentException("CustomerId must be greater than 0, got: " + value);
        }
    }

    /**
     * Crea un CustomerId a partir de un valor Long.
     *
     * @param value el valor del ID del cliente
     * @return una nueva instancia de CustomerId
     */
    public static CustomerId of(Long value) {

        return new CustomerId(value);
    }
}
