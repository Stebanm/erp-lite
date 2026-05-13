package com.nimlabs.erp_lite.domain.order.events;

import com.nimlabs.erp_lite.domain.common.DomainEvent;
import com.nimlabs.erp_lite.domain.order.OrderId;
import com.nimlabs.erp_lite.domain.shared.CustomerId;
import com.nimlabs.erp_lite.domain.shared.Money;

import java.time.Instant;

/**
 * Emitido cuando un nuevo pedido es creado.
 *
 * @param orderId      el identificador del pedido
 * @param customerId   el identificador del cliente
 * @param customerName el nombre del cliente
 * @param totalAmount  el monto total del pedido
 * @param timestamp    la marca de tiempo del evento
 */
public record OrderCreated(
    OrderId orderId,
    CustomerId customerId,
    String customerName,
    Money totalAmount,
    Instant timestamp
) implements DomainEvent {}
