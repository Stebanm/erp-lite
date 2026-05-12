package com.nimlabs.erp_lite.domain.order;

import com.nimlabs.erp_lite.domain.common.AggregateRoot;
import com.nimlabs.erp_lite.domain.order.events.OrderCancelled;
import com.nimlabs.erp_lite.domain.order.events.OrderConfirmed;
import com.nimlabs.erp_lite.domain.order.events.OrderCreated;
import com.nimlabs.erp_lite.domain.order.events.OrderDelivered;
import com.nimlabs.erp_lite.domain.order.events.OrderShipped;
import com.nimlabs.erp_lite.domain.shared.AuditInfo;
import com.nimlabs.erp_lite.domain.shared.Money;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends AggregateRoot<OrderId> {

    private OrderId id;
    private OrderNumber orderNumber;
    private Customer customer;
    private OrderStatus status;
    private List<OrderItem> items;
    private Money totalAmount;
    private AuditInfo auditInfo;

    Order(OrderId id, OrderNumber orderNumber, Customer customer,
          OrderStatus status, List<OrderItem> items, Money totalAmount,
          AuditInfo auditInfo) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.customer = customer;
        this.status = status;
        this.items = items;
        this.totalAmount = totalAmount;
        this.auditInfo = auditInfo;
    }

    public static Order create(OrderNumber orderNumber, Customer customer,
                               List<OrderItem> items, String createdBy) {
        Objects.requireNonNull(orderNumber, "OrderNumber cannot be null");
        Objects.requireNonNull(customer, "Customer cannot be null");
        Objects.requireNonNull(createdBy, "CreatedBy cannot be null");

        List<OrderItem> itemsCopy = new ArrayList<>(
            Objects.requireNonNull(items, "Items cannot be null"));

        if (itemsCopy.isEmpty())
            throw new IllegalArgumentException("Order must have at least one item");

        OrderId id = OrderId.generate();
        Instant now = Instant.now();
        Order order = new Order(id, orderNumber, customer, OrderStatus.pending(),
            itemsCopy, null, AuditInfo.create(createdBy, now));
        order.calculateTotal();

        order.registerEvent(new OrderCreated(
            id, customer.customerId(), customer.customerName(), order.totalAmount, now));
        return order;
    }

    public void confirm() {
        validateTransition(OrderStatus.confirmed());
        this.status = OrderStatus.confirmed();
        this.auditInfo = auditInfo.updateTimestamp();
        registerEvent(new OrderConfirmed(id, Instant.now()));
    }

    public void ship() {
        validateTransition(OrderStatus.shipped());
        this.status = OrderStatus.shipped();
        this.auditInfo = auditInfo.updateTimestamp();
        registerEvent(new OrderShipped(id, Instant.now()));
    }

    public void deliver() {
        validateTransition(OrderStatus.delivered());
        this.status = OrderStatus.delivered();
        this.auditInfo = auditInfo.updateTimestamp();
        registerEvent(new OrderDelivered(id, Instant.now()));
    }

    public void cancel(String reason) {
        Objects.requireNonNull(reason, "Cancellation reason cannot be null");
        if (reason.isBlank()) throw new IllegalArgumentException("Cancellation reason cannot be blank");
        validateTransition(OrderStatus.cancelled());
        this.status = OrderStatus.cancelled();
        this.auditInfo = auditInfo.updateTimestamp();
        registerEvent(new OrderCancelled(id, reason, Instant.now()));
    }

    public void addItem(OrderItem item) {
        Objects.requireNonNull(item, "Item cannot be null");
        validateCurrencyConsistency(item.getUnitPrice());
        this.items.add(item);
        calculateTotal();
        this.auditInfo = auditInfo.updateTimestamp();
    }

    public void removeItem(OrderItem item) {
        Objects.requireNonNull(item, "Item cannot be null");
        boolean removed = this.items.remove(item);
        if (!removed)
            throw new IllegalArgumentException("Item not found in order");
        validateItems();
        calculateTotal();
        this.auditInfo = auditInfo.updateTimestamp();
    }

    public void calculateTotal() {
        if (items == null || items.isEmpty()) {
            throw new IllegalStateException("Cannot calculate total with no items");
        }
        Currency currency = items.get(0).getUnitPrice().currency();
        Money total = Money.of(0, currency);
        for (OrderItem item : items) {
            total = total.add(item.getSubtotal());
        }
        this.totalAmount = total;
    }

    private void validateItems() {
        if (items == null || items.isEmpty())
            throw new IllegalStateException("Order must have at least one item");
    }

    private void validateTransition(OrderStatus nextStatus) {
        if (!status.canTransitionTo(nextStatus))
            throw new IllegalStateException(
                "Cannot transition from " + status.value() + " to " + nextStatus.value());
    }

    private void validateCurrencyConsistency(Money money) {
        if (totalAmount != null && !totalAmount.currency().equals(money.currency()))
            throw new IllegalArgumentException(
                "Currency mismatch: order uses " + totalAmount.currency() + " but item uses " + money.currency());
    }
}
