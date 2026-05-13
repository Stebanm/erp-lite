package com.nimlabs.erp_lite.domain.product.events;

import com.nimlabs.erp_lite.domain.common.DomainEvent;
import com.nimlabs.erp_lite.domain.product.ProductId;
import com.nimlabs.erp_lite.domain.product.ProductName;
import com.nimlabs.erp_lite.domain.product.SKU;
import com.nimlabs.erp_lite.domain.shared.Money;

import java.time.Instant;

/**
 * Emitido cuando un nuevo producto es creado.
 * DESENCADENA la sincronización con MongoDB (CQRS).
 *
 * @param productId el identificador del producto
 * @param sku       el SKU del producto
 * @param name      el nombre del producto
 * @param price     el precio del producto
 * @param timestamp la marca de tiempo del evento
 */
public record ProductCreated(
    ProductId productId,
    SKU sku,
    ProductName name,
    Money price,
    Instant timestamp
) implements DomainEvent {}
