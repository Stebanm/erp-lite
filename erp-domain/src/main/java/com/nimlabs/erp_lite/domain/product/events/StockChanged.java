package com.nimlabs.erp_lite.domain.product.events;

import com.nimlabs.erp_lite.domain.common.DomainEvent;
import com.nimlabs.erp_lite.domain.product.ProductId;

import java.time.Instant;

/**
 * Emitido cuando el stock cambia (incremento o decremento).
 * DESENCADENA la sincronización con MongoDB.
 *
 * @param productId el identificador del producto
 * @param oldStock  el valor anterior del stock
 * @param newStock  el nuevo valor del stock
 * @param reason    el motivo del cambio de stock
 * @param timestamp la marca de tiempo del evento
 */
public record StockChanged(
    ProductId productId,
    Integer oldStock,
    Integer newStock,
    String reason,
    Instant timestamp
) implements DomainEvent {}
