package com.nimlabs.erp_lite.domain.product.events;

import com.nimlabs.erp_lite.domain.common.DomainEvent;
import com.nimlabs.erp_lite.domain.product.ProductId;

import java.time.Instant;

/**
 * Emitido cuando la información del producto es actualizada.
 * DESENCADENA la sincronización con MongoDB.
 *
 * @param productId el identificador del producto
 * @param timestamp la marca de tiempo del evento
 */
public record ProductUpdated(
    ProductId productId,
    Instant timestamp
) implements DomainEvent {}
