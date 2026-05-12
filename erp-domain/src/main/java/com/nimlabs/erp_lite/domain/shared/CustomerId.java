package com.nimlabs.erp_lite.domain.shared;

public record CustomerId(Long value) {

    public CustomerId {
        if (value == null) throw new IllegalArgumentException("CustomerId cannot be null");
        if (value <= 0) throw new IllegalArgumentException("CustomerId must be greater than 0, got: " + value);
    }

    public static CustomerId of(Long value) {
        return new CustomerId(value);
    }
}
