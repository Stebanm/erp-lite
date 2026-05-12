package com.nimlabs.erp_lite.domain.product;

public record ProductName(String value) {

    public ProductName {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("ProductName cannot be null or blank");
        if (value.length() < 3)
            throw new IllegalArgumentException("ProductName must be at least 3 characters, got: " + value.length());
        if (value.length() > 200)
            throw new IllegalArgumentException("ProductName must be at most 200 characters, got: " + value.length());
    }

    public static ProductName of(String value) {
        return new ProductName(value);
    }
}
