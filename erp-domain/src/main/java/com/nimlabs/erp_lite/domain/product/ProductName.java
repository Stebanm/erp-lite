package com.nimlabs.erp_lite.domain.product;

/**
 * Nombre del producto.
 * Debe tener entre 3 y 200 caracteres.
 *
 * @param value el nombre del producto
 */
public record ProductName(String value) {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 200;

    public ProductName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ProductName cannot be null or blank");
        }

        if (value.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("ProductName must be at least 3 characters, got: " + value.length());
        }

        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("ProductName must be at most 200 characters, got: " + value.length());
        }
    }

    /**
     * Crea una instancia de ProductName a partir de un valor String.
     *
     * @param value el nombre del producto
     * @return una nueva instancia de ProductName
     */
    public static ProductName of(String value) {

        return new ProductName(value);
    }
}
