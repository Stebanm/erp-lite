package com.nimlabs.erp_lite.domain.order.events;

import com.nimlabs.erp_lite.domain.common.DomainEvent;
import com.nimlabs.erp_lite.domain.order.OrderId;
import com.nimlabs.erp_lite.domain.shared.CustomerId;
import com.nimlabs.erp_lite.domain.shared.Money;

import java.time.Instant;

public record OrderCreated(
    OrderId orderId,
    CustomerId customerId,
    String customerName,
    Money totalAmount,
    Instant timestamp
) implements DomainEvent {}
