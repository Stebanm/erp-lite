package com.nimlabs.erp_lite.domain.order.events;

import com.nimlabs.erp_lite.domain.common.DomainEvent;
import com.nimlabs.erp_lite.domain.order.OrderId;

import java.time.Instant;

public record OrderConfirmed(
    OrderId orderId,
    Instant timestamp
) implements DomainEvent {}
