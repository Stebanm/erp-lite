package com.nimlabs.erp_lite.domain.product;

import com.nimlabs.erp_lite.domain.common.AggregateRoot;
import com.nimlabs.erp_lite.domain.product.events.ProductCreated;
import com.nimlabs.erp_lite.domain.product.events.ProductDeactivated;
import com.nimlabs.erp_lite.domain.product.events.ProductUpdated;
import com.nimlabs.erp_lite.domain.product.events.StockChanged;
import com.nimlabs.erp_lite.domain.shared.AuditInfo;
import com.nimlabs.erp_lite.domain.shared.Money;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Aggregate Root de Product.
 * Gestiona la información del producto incluyendo precios, stock y categoría.
 */
@Getter
public class Product extends AggregateRoot<ProductId> {

    private SKU sku;
    private ProductName name;
    private String description;
    private Money price;
    private Stock stock;
    private CategoryReference category;
    private ProductImage image;
    private boolean active;
    private AuditInfo auditInfo;

    Product(
            ProductId id,
            SKU sku,
            ProductName name,
            String description,
            Money price,
            Stock stock,
            CategoryReference category,
            ProductImage image,
            boolean active,
            AuditInfo auditInfo
    ) {
        super(id);

        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.image = image;
        this.active = active;
        this.auditInfo = auditInfo;
    }

    /**
     * Crea un nuevo Product.
     *
     * @param sku         el SKU del producto (único e inmutable)
     * @param name        el nombre del producto
     * @param description la descripción del producto (opcional)
     * @param price       el precio del producto (debe ser > 0)
     * @param stock       el stock inicial
     * @param category    la referencia de la categoría
     * @param image       la imagen del producto (opcional)
     * @param createdBy   el usuario que creó el producto
     * @return una nueva instancia de Product
     */
    public static Product create(
            SKU sku,
            ProductName name,
            String description,
            Money price,
            Stock stock,
            CategoryReference category,
            ProductImage image,
            String createdBy
    ) {
        validatePrice(price);

        ProductId id = ProductId.generate();
        Instant now = Instant.now();
        AuditInfo auditInfo = AuditInfo.create(createdBy, now);

        Product product = new Product(
                id,
                sku,
                name,
                description,
                price,
                stock,
                category,
                image,
                true,
                auditInfo
        );

        product.registerEvent(
                new ProductCreated(
                        id,
                        sku,
                        name,
                        price,
                        now
                )
        );

        return product;
    }

    /**
     * Actualiza la información del producto.
     *
     * @param name        el nuevo nombre del producto
     * @param description la nueva descripción
     * @param price       el nuevo precio
     * @param category    la nueva categoría
     * @param image       la nueva imagen
     */
    public void update(
            ProductName name,
            String description,
            Money price,
            CategoryReference category,
            ProductImage image
    ) {
        validatePrice(price);

        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.image = image;
        this.auditInfo = this.auditInfo.updateTimestamp();

        registerEvent(new ProductUpdated(this.id, Instant.now()));
    }

    /**
     * Incrementa el stock en la cantidad especificada.
     *
     * @param quantity la cantidad a agregar
     * @param reason   el motivo del incremento
     */
    public void incrementStock(int quantity, String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Reason for stock increment cannot be null or blank");
        }

        Integer oldStock = this.stock.value();
        this.stock = this.stock.increment(quantity);
        this.auditInfo = this.auditInfo.updateTimestamp();

        registerEvent(
                new StockChanged(
                        id,
                        oldStock,
                        this.stock.value(),
                        reason,
                        Instant.now()
                )
        );
    }

    /**
     * Decrementa el stock en la cantidad especificada.
     *
     * @param quantity la cantidad a restar
     * @param reason   el motivo del decremento
     * @throws IllegalArgumentException si el stock es insuficiente
     */
    public void decrementStock(int quantity, String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Reason for stock decrement cannot be null or blank");
        }

        Integer oldStock = this.stock.value();
        this.stock = this.stock.decrement(quantity);
        this.auditInfo = this.auditInfo.updateTimestamp();

        registerEvent(
                new StockChanged(
                        id,
                        oldStock,
                        this.stock.value(),
                        reason,
                        Instant.now()
                )
        );
    }

    /**
     * Cambia el precio del producto.
     *
     * @param newPrice el nuevo precio
     */
    public void changePrice(Money newPrice) {
        validatePrice(newPrice);

        this.price = newPrice;
        this.auditInfo = this.auditInfo.updateTimestamp();

        registerEvent(new ProductUpdated(this.id, Instant.now()));
    }

    /**
     * Desactiva el producto.
     * Los productos desactivados no pueden ser pedidos.
     */
    public void deactivate() {
        if (!this.active) {
            throw new IllegalStateException("Product is already deactivated");
        }

        this.active = false;
        this.auditInfo = this.auditInfo.updateTimestamp();

        registerEvent(new ProductDeactivated(this.id, Instant.now()));
    }

    /**
     * Activa el producto
     */
    public void activate() {
        if (this.active) {
            throw new IllegalStateException("Product is already active");
        }

        this.active = true;
        this.auditInfo = this.auditInfo.updateTimestamp();

        registerEvent(new ProductUpdated(this.id, Instant.now()));
    }

    /**
     * Verifica si el producto tiene la cantidad requerida en stock.
     *
     * @param requiredQuantity la cantidad requerida
     * @return true si el stock es suficiente
     */
    public boolean hasAvailableStock(int requiredQuantity) {
        return this.active && this.stock.hasAvailable(requiredQuantity);
    }

    private static void validatePrice(Money price) {
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }

        if (price.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
    }
}
