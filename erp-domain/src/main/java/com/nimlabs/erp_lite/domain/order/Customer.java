package com.nimlabs.erp_lite.domain.order;

import com.nimlabs.erp_lite.domain.shared.CustomerId;

/**
 * Referencia del cliente con información básica.
 * Combina el ID y el nombre del cliente para el contexto del pedido.
 *
 * @param customerId   el identificador del cliente
 * @param customerName el nombre del cliente
 */
public record Customer(
        CustomerId customerId,
        String customerName
) {

    public Customer {
        if (customerId == null) {
            throw new IllegalArgumentException("CustomerId cannot be null");
        }

        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("CustomerName cannot be null or blank");
        }
    }

    /**
     * Crea una instancia de Customer a partir de un CustomerId y un nombre.
     *
     * @param customerId   el identificador del cliente
     * @param customerName el nombre del cliente
     * @return una nueva instancia de Customer
     */
    public static Customer of(CustomerId customerId, String customerName) {
        return new Customer(customerId, customerName);
    }
}
