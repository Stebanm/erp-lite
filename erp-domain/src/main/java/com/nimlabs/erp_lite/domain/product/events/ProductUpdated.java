package com.nimlabs.erp_lite.domain.product.events;

import com.nimlabs.erp_lite.domain.common.DomainEvent;
import com.nimlabs.erp_lite.domain.product.ProductId;

import java.time.Instant;

public record ProductUpdated(
    ProductId productId,
    Instant timestamp
) implements DomainEvent {}
