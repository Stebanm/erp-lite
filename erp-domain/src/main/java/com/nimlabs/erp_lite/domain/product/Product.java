package com.nimlabs.erp_lite.domain.product;

import com.nimlabs.erp_lite.domain.common.AggregateRoot;
import com.nimlabs.erp_lite.domain.product.events.ProductCreated;
import com.nimlabs.erp_lite.domain.product.events.ProductDeactivated;
import com.nimlabs.erp_lite.domain.product.events.ProductUpdated;
import com.nimlabs.erp_lite.domain.product.events.StockChanged;
import com.nimlabs.erp_lite.domain.shared.AuditInfo;
import com.nimlabs.erp_lite.domain.shared.Money;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends AggregateRoot<ProductId> {

    private ProductId id;
    private SKU sku;
    private ProductName name;
    private String description;
    private Money price;
    private Stock stock;
    private CategoryReference category;
    private ProductImage image;
    private boolean active;
    private AuditInfo auditInfo;

    Product(ProductId id, SKU sku, ProductName name, String description,
            Money price, Stock stock, CategoryReference category,
            ProductImage image, boolean active, AuditInfo auditInfo) {
        this.id = id;
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

    public static Product create(SKU sku, ProductName name, String description, Money price,
                                 Stock stock, CategoryReference category, ProductImage image,
                                 String createdBy) {
        Objects.requireNonNull(sku, "SKU cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(price, "Price cannot be null");
        Objects.requireNonNull(stock, "Stock cannot be null");
        Objects.requireNonNull(category, "Category cannot be null");
        Objects.requireNonNull(createdBy, "CreatedBy cannot be null");
        if (price.amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Price must be greater than 0");

        ProductId id = ProductId.generate();
        Instant now = Instant.now();
        Product product = new Product(id, sku, name, description, price, stock, category, image, true,
            AuditInfo.create(createdBy, now));
        product.registerEvent(new ProductCreated(id, sku, name, price, now));
        return product;
    }

    public void update(ProductName name, String description, Money price,
                       CategoryReference category, ProductImage image) {
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(price, "Price cannot be null");
        Objects.requireNonNull(category, "Category cannot be null");
        if (price.amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Price must be greater than 0");

        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.image = image;
        this.auditInfo = auditInfo.updateTimestamp();
        registerEvent(new ProductUpdated(id, Instant.now()));
    }

    public void incrementStock(int quantity, String reason) {
        if (quantity <= 0) throw new IllegalArgumentException("Increment quantity must be positive");
        int oldValue = this.stock.value();
        this.stock = this.stock.increment(quantity);
        this.auditInfo = auditInfo.updateTimestamp();
        registerEvent(new StockChanged(id, oldValue, this.stock.value(), reason, Instant.now()));
    }

    public void decrementStock(int quantity, String reason) {
        if (quantity <= 0) throw new IllegalArgumentException("Decrement quantity must be positive");
        int oldValue = this.stock.value();
        this.stock = this.stock.decrement(quantity);
        this.auditInfo = auditInfo.updateTimestamp();
        registerEvent(new StockChanged(id, oldValue, this.stock.value(), reason, Instant.now()));
    }

    public void changePrice(Money newPrice) {
        Objects.requireNonNull(newPrice, "Price cannot be null");
        if (newPrice.amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Price must be greater than 0");
        this.price = newPrice;
        this.auditInfo = auditInfo.updateTimestamp();
        registerEvent(new ProductUpdated(id, Instant.now()));
    }

    public void deactivate() {
        this.active = false;
        this.auditInfo = auditInfo.updateTimestamp();
        registerEvent(new ProductDeactivated(id, Instant.now()));
    }

    public void activate() {
        this.active = true;
        this.auditInfo = auditInfo.updateTimestamp();
        registerEvent(new ProductUpdated(id, Instant.now()));
    }

    public boolean hasAvailableStock(int requiredQuantity) {
        return stock.hasAvailable(requiredQuantity);
    }
}
