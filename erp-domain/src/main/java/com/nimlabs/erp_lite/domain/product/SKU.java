package com.nimlabs.erp_lite.domain.product;

import java.util.regex.Pattern;

/**
 * Stock Keeping Unit. Único e inmutable.
 * Patrón: [A-Z]+-\d{3} (ej. LAPTOP-001, MOUSE-042)
 *
 * @param value el valor del SKU
 */
public record SKU(String value) {

    private static final Pattern SKU_PATTERN = Pattern.compile("^[A-Z]+-\\d{3}$");

    public SKU {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("SKU cannot be null or blank");
        }

        if (!SKU_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Invalid SKU format: " + value + ". Expected pattern: CATEGORY-NNN (e.g., LAPTOP-001)");
        }
    }

    /**
     * Crea una instancia de SKU a partir de un valor String.
     *
     * @param value el valor del SKU
     * @return una nueva instancia de SKU
     */
    public static SKU of(String value) {

        return new SKU(value);
    }
}
