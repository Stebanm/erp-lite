package com.nimlabs.erp_lite.domain.order;

import com.nimlabs.erp_lite.domain.shared.CustomerId;

public record Customer(CustomerId customerId, String customerName) {

    public Customer {
        if (customerId == null) throw new IllegalArgumentException("CustomerId cannot be null");
        if (customerName == null || customerName.isBlank())
            throw new IllegalArgumentException("CustomerName cannot be null or blank");
    }

    public static Customer of(CustomerId customerId, String customerName) {
        return new Customer(customerId, customerName);
    }
}
