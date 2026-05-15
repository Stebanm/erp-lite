package com.nimlabs.erp_lite.domain.product;

/**
 * Cantidad de stock del producto. No puede ser negativa.
 *
 * @param value el valor del stock (debe ser >= 0)
 */
public record Stock(Integer value) {

    public Stock {
        if (value == null) {
            throw new IllegalArgumentException("Stock cannot be null");
        }
        if (value < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
    }

    /**
     * Crea una instancia de Stock a partir de un valor entero.
     *
     * @param value el valor del stock
     * @return una nueva instancia de Stock
     */
    public static Stock of(int value) {
        return new Stock(value);
    }

    /**
     * Crea una instancia de Stock con valor cero.
     *
     * @return una nueva instancia de Stock con valor 0
     */
    public static Stock zero() {
        return new Stock(0);
    }

    /**
     * Incrementa el stock en la cantidad especificada.
     *
     * @param quantity la cantidad a agregar
     * @return una nueva instancia de Stock con el valor incrementado
     */
    public Stock increment(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Increment quantity cannot be negative");
        }

        return new Stock(this.value + quantity);
    }

    /**
     * Decrementa el stock en la cantidad especificada.
     *
     * @param quantity la cantidad a restar
     * @return una nueva instancia de Stock con el valor decrementado
     * @throws IllegalArgumentException si el resultado fuera negativo
     */
    public Stock decrement(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Decrement quantity cannot be negative");
        }

        if (this.value - quantity < 0) {
            throw new IllegalArgumentException(
                    "Cannot decrement stock below zero. Current: " + this.value + ", requested: " + quantity);
        }

        return new Stock(this.value - quantity);
    }

    /**
     * Verifica si el stock tiene la cantidad requerida disponible.
     *
     * @param required la cantidad requerida
     * @return true si el stock disponible es mayor o igual a la cantidad requerida
     */
    public boolean hasAvailable(int required) {
        return this.value >= required;
    }
}
