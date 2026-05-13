package com.nimlabs.erp_lite.domain.order.events;

import com.nimlabs.erp_lite.domain.common.DomainEvent;
import com.nimlabs.erp_lite.domain.order.OrderId;

import java.time.Instant;

/**
 * Emitido cuando el pedido es cancelado.
 * Si estaba CONFIRMED, el stock debe ser liberado.
 *
 * @param orderId   el identificador del pedido
 * @param reason    el motivo de la cancelación
 * @param timestamp la marca de tiempo del evento
 */
public record OrderCancelled(
    OrderId orderId,
    String reason,
    Instant timestamp
) implements DomainEvent {}
