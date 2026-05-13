package com.nimlabs.erp_lite.domain.order;

import com.nimlabs.erp_lite.domain.common.AggregateRoot;
import com.nimlabs.erp_lite.domain.order.events.OrderCancelled;
import com.nimlabs.erp_lite.domain.order.events.OrderConfirmed;
import com.nimlabs.erp_lite.domain.order.events.OrderCreated;
import com.nimlabs.erp_lite.domain.order.events.OrderDelivered;
import com.nimlabs.erp_lite.domain.order.events.OrderShipped;
import com.nimlabs.erp_lite.domain.shared.AuditInfo;
import com.nimlabs.erp_lite.domain.shared.Money;
import lombok.Getter;

import java.time.Instant;
import java.util.*;

/**
 * Aggregate Root de Order.
 * Gestiona el ciclo de vida del pedido, los artículos y las transiciones de estado.
 */
@Getter
public class Order extends AggregateRoot<OrderId> {

    private OrderNumber orderNumber;
    private Customer customer;
    private OrderStatus status;
    private List<OrderItem> items;
    private Money totalAmount;
    private AuditInfo auditInfo;

    protected Order() {
        super(null);
    }

    private Order(
            OrderId id,
            OrderNumber orderNumber,
            Customer customer,
            OrderStatus status,
            List<OrderItem> items,
            Money totalAmount,
            AuditInfo auditInfo
    ) {
        super(id);

        this.orderNumber = orderNumber;
        this.customer = customer;
        this.status = status;
        this.items = new ArrayList<>(items);
        this.totalAmount = totalAmount;
        this.auditInfo = auditInfo;
    }

    /**
     * Crea un nuevo Order.
     *
     * @param orderNumber el número de pedido único
     * @param customer    la información del cliente
     * @param items       la lista de artículos del pedido (no debe estar vacía)
     * @param createdBy   el usuario que creó el pedido
     * @return una nueva instancia de Order en estado PENDING
     */
    public static Order create(
            OrderNumber orderNumber,
            Customer customer,
            List<OrderItem> items,
            String createdBy
    ) {
        if (orderNumber == null) {
            throw new IllegalArgumentException("Order number cannot be null");
        }

        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Items cannot be null");
        }

        validateItems(items);

        OrderId orderId = OrderId.generate();
        OrderStatus status = OrderStatus.pending();
        Money totalAmount = calculateTotal(items);
        Instant now = Instant.now();
        AuditInfo auditInfo = AuditInfo.create(createdBy, now);

        Order order = new Order(
                orderId,
                orderNumber,
                customer,
                status,
                items,
                totalAmount,
                auditInfo
        );

        order.registerEvent(
                new OrderCreated(
                        orderId,
                        customer.customerId(),
                        customer.customerName(),
                        totalAmount,
                        now
                )
        );

        return order;
    }

    /**
     * Confirma el pedido (PENDING -> CONFIRMED).
     * Esta transición desencadena el decremento del stock.
     */
    public void confirm() {
        OrderStatus nextStatus = OrderStatus.confirmed();
        validateTransition(nextStatus);

        this.status = nextStatus;
        this.auditInfo = this.auditInfo.updateTimestamp();

        registerEvent(new OrderConfirmed(id, Instant.now()));
    }

    /**
     * Envía el pedido (CONFIRMED -> SHIPPED).
     */
    public void ship() {
        OrderStatus nextStatus = OrderStatus.shipped();
        validateTransition(nextStatus);

        this.status = nextStatus;
        this.auditInfo = this.auditInfo.updateTimestamp();

        registerEvent(new OrderShipped(this.id, Instant.now()));
    }

    /**
     * Entrega el pedido (SHIPPED -> DELIVERED).
     * DELIVERED es un estado final.
     */
    public void deliver() {
        OrderStatus nextStatus = OrderStatus.delivered();
        validateTransition(nextStatus);

        this.status = nextStatus;
        this.auditInfo = this.auditInfo.updateTimestamp();
        registerEvent(new OrderDelivered(this.id, Instant.now()));
    }

    /**
     * Cancela el pedido.
     * Si el pedido estaba CONFIRMED, el stock debe ser liberado.
     *
     * @param reason el motivo de la cancelación
     */
    public void cancel(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Cancellation reason cannot be blank");
        }

        OrderStatus nextStatus = OrderStatus.cancelled();
        validateTransition(nextStatus);

        this.status = nextStatus;
        this.auditInfo = this.auditInfo.updateTimestamp();
        registerEvent(new OrderCancelled(this.id, reason, Instant.now()));
    }

    /**
     * Agrega un artículo al pedido.
     * Solo permitido en estado PENDING.
     *
     * @param item el artículo a agregar
     */
    public void addItem(OrderItem item) {
        if (!this.status.isPending()) {
            throw new IllegalStateException("Cannot add items to order in status: " + this.status.value());
        }

        if (item == null) {
            throw new IllegalArgumentException("Order item cannot be null");
        }

        this.items.add(item);
        this.totalAmount = calculateTotal(this.items);
        this.auditInfo = this.auditInfo.updateTimestamp();
    }

    /**
     * Elimina un artículo del pedido.
     * Solo permitido en estado PENDING.
     *
     * @param item el artículo a eliminar
     */
    public void removeItem(OrderItem item) {
        if (!this.status.isPending()) {
            throw new IllegalStateException("Cannot remove items from order in status: " + this.status.value());
        }

        if (item == null) {
            throw new IllegalArgumentException("Order item cannot be null");
        }

        if (!this.items.remove(item)) {
            throw new IllegalArgumentException("Item not found in order");
        }

        if (this.items.isEmpty()) {
            throw new IllegalStateException("Order must have at least one item");
        }

        this.totalAmount = calculateTotal(this.items);
        this.auditInfo = this.auditInfo.updateTimestamp();
    }

    /**
     * Retorna una copia no modificable de los artículos del pedido.
     *
     * @return la lista de artículos del pedido
     */
    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Calcula el monto total sumando todos los subtotales de los artículos.
     *
     * @return el monto total
     */
    private static Money calculateTotal(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalStateException("Cannot calculate total with no items");
        }

        Money total = items.get(0).getSubtotal();

        for (int i = 1; i < items.size(); i++) {
            total = total.add(items.get(i).getSubtotal());
        }

        return total;
    }

    /**
     * Valida que todos los artículos sean válidos.
     * - Todos los artículos deben tener la misma moneda
     * - El subtotal de cada artículo debe ser igual a cantidad * precioUnitario
     */
    private static void validateItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalStateException("Order must have at least one item");
        }

        Currency currency = items.get(0).getUnitPrice().currency();

        items.forEach(item -> {
            if (item.getUnitPrice() == null) {
                throw new IllegalArgumentException("Item unit price cannot be null");
            }

            if (!item.getUnitPrice().currency().equals(currency)) {
                throw new IllegalArgumentException(
                        "All items must have the same currency. Expected: " + currency +
                                ", found: " + item.getUnitPrice().currency()
                );
            }

            Money calculatedSubtotal = item.calculateSubtotal();

            if (!item.getSubtotal().equals(calculatedSubtotal)) {
                throw new IllegalArgumentException(
                        "Item subtotal mismatch for product " + item.getProductName() +
                                ". Expected: " + calculatedSubtotal + ", found: " + item.getSubtotal()
                );
            }
        });
    }

    /**
     * Valida que el estado actual pueda transicionar al estado destino.
     *
     * @param nextStatus el estado destino
     * @throws IllegalStateException si la transición es inválida
     */
    private void validateTransition(OrderStatus nextStatus) {
        if (!this.status.canTransitionTo(nextStatus))
            throw new IllegalStateException(
                "Cannot transition from " + this.status.value() + " to " + nextStatus.value());
    }
}
