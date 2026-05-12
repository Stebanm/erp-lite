package com.nimlabs.erp_lite.domain.shared;

/**
 * Cantidad de artículos en el pedido. Debe ser mayor que 0.
 *
 * @param value el valor de la cantidad (debe ser > 0)
 */
public record Quantity(Integer value) {

    public Quantity {
        if (value == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }

        if (value <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0, got: " + value);
        }
    }

    /**
     * Crea una instancia de Quantity a partir de un valor entero.
     *
     * @param value el valor de la cantidad
     * @return una nueva instancia de Quantity
     */
    public static Quantity of(int value) {
        return new Quantity(value);
    }
}
