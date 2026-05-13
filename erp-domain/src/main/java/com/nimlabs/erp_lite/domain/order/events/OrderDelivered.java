package com.nimlabs.erp_lite.domain.order.events;

import com.nimlabs.erp_lite.domain.common.DomainEvent;
import com.nimlabs.erp_lite.domain.order.OrderId;

import java.time.Instant;

/**
 * Emitido cuando el pedido transiciona de SHIPPED -> DELIVERED.
 * Estado final.
 *
 * @param orderId   el identificador del pedido
 * @param timestamp la marca de tiempo del evento
 */
public record OrderDelivered(
    OrderId orderId,
    Instant timestamp
) implements DomainEvent {}
