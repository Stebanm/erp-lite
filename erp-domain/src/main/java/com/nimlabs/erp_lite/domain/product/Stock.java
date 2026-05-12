package com.nimlabs.erp_lite.domain.product;

public record Stock(int value) {

    public Stock {
        if (value < 0)
            throw new IllegalArgumentException("Stock cannot be negative, got: " + value);
    }

    public static Stock of(int value) {
        return new Stock(value);
    }

    public static Stock zero() {
        return new Stock(0);
    }

    public Stock increment(int quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException("Increment quantity must be positive, got: " + quantity);
        return new Stock(this.value + quantity);
    }

    public Stock decrement(int quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException("Decrement quantity must be positive, got: " + quantity);
        if (this.value - quantity < 0)
            throw new IllegalArgumentException(
                "Insufficient stock: available=" + this.value + ", requested=" + quantity);
        return new Stock(this.value - quantity);
    }

    public boolean hasAvailable(int required) {
        return this.value >= required;
    }
}
