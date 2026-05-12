package com.nimlabs.erp_lite.domain.order;

import com.nimlabs.erp_lite.domain.common.Entity;
import com.nimlabs.erp_lite.domain.product.Product;
import com.nimlabs.erp_lite.domain.shared.Money;
import com.nimlabs.erp_lite.domain.shared.Quantity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItem extends Entity<OrderItemId> {

    private OrderItemId id;
    private com.nimlabs.erp_lite.domain.product.ProductId productReference;
    private String productName;
    private Quantity quantity;
    private Money unitPrice;
    private Money subtotal;

    public static OrderItem from(Product product, Quantity quantity) {
        Objects.requireNonNull(product, "Product cannot be null");
        Objects.requireNonNull(quantity, "Quantity cannot be null");
        Money unitPrice = product.getPrice();
        Money subtotal = unitPrice.multiply(quantity);
        return new OrderItem(
            OrderItemId.generate(),
            product.getId(),
            product.getName().value(),
            quantity,
            unitPrice,
            subtotal
        );
    }

    public Money calculateSubtotal() {
        return unitPrice.multiply(quantity);
    }
}
