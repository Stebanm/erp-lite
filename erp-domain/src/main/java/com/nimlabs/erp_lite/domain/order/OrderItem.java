package com.nimlabs.erp_lite.domain.order;

import com.nimlabs.erp_lite.domain.common.Entity;
import com.nimlabs.erp_lite.domain.product.Product;
import com.nimlabs.erp_lite.domain.product.ProductId;
import com.nimlabs.erp_lite.domain.shared.Money;
import com.nimlabs.erp_lite.domain.shared.Quantity;
import lombok.Getter;

/**
 * Entidad OrderItem.
 * Representa una línea de artículo en un pedido con referencia de producto, cantidad y precio.
 * El nombre del producto y el precio unitario son instantáneas tomadas en el momento de la creación del pedido.
 */
@Getter
public class OrderItem extends Entity<OrderItemId> {

    private ProductId productReference;
    private String productName;
    private Quantity quantity;
    private Money unitPrice;
    private Money subtotal;

    protected OrderItem() {
        super(null);
    }

    /**
     * Crea un OrderItem a partir de un Product y una cantidad.
     * Es una snapshot: el nombre del producto y el precio quedan fijos en el momento de la creación del pedido.
     *
     * @param product  el producto a pedir
     * @param quantity la cantidad a pedir
     * @return una nueva instancia de OrderItem
     * @throws IllegalArgumentException si el producto está inactivo o no tiene suficiente stock
     */
    public static OrderItem from(Product product, Quantity quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }

        if (!product.isActive()) {
            throw new IllegalArgumentException("Cannot create order item for inactive product: " + product.getSku().value());
        }

        if (!product.hasAvailableStock(quantity.value())) {
            throw new IllegalArgumentException(
                                    "Insufficient stock for product " +
                                    product.getSku().value() +
                                    ". Available: " +
                                    product.getStock().value() +
                                    ", requested: " +
                                    quantity.value()
            );
        }

        OrderItemId orderItemId = OrderItemId.generate();
        Money unitPrice = product.getPrice();
        Money subtotal = unitPrice.multiply(quantity);

        return new OrderItem(
            orderItemId,
            product.getId(),
            product.getName().value(),
            quantity,
            unitPrice,
            subtotal
        );
    }

    private OrderItem(
        OrderItemId id,
        ProductId productReference,
        String productName,
        Quantity quantity,
        Money unitPrice,
        Money subtotal
    ) {
        super(id);

        this.productReference = productReference;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    /**
     * Calcula el subtotal (cantidad * precioUnitario).
     * Este método existe con fines de validación.
     *
     * @return el subtotal calculado
     */
    public Money calculateSubtotal() {
        return this.unitPrice.multiply(quantity);
    }
}
