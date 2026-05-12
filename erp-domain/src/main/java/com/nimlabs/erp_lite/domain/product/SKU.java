package com.nimlabs.erp_lite.domain.product;

import java.util.regex.Pattern;

public record SKU(String value) {

    private static final Pattern SKU_PATTERN = Pattern.compile("[A-Z]+-\\d{3}");

    public SKU {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("SKU cannot be null or blank");
        if (!SKU_PATTERN.matcher(value).matches())
            throw new IllegalArgumentException(
                "Invalid SKU format: " + value + ". Expected pattern: CATEGORY-NNN (e.g., LAPTOP-001)");
    }

    public static SKU of(String value) {
        return new SKU(value);
    }
}
