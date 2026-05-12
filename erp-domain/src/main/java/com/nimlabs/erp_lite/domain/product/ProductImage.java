package com.nimlabs.erp_lite.domain.product;

import java.net.URI;

public record ProductImage(String imageUrl) {

    public ProductImage {
        if (imageUrl == null || imageUrl.isBlank())
            throw new IllegalArgumentException("ImageUrl cannot be null or blank");
        try {
            URI uri = URI.create(imageUrl);
            if (uri.getScheme() == null)
                throw new IllegalArgumentException("Invalid image URL (missing scheme): " + imageUrl);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid image URL: " + imageUrl);
        }
    }

    public static ProductImage of(String imageUrl) {
        return new ProductImage(imageUrl);
    }

    public String getFullUrl() {
        return imageUrl;
    }

    public String getFileName() {
        int lastSlash = imageUrl.lastIndexOf('/');
        return lastSlash >= 0 ? imageUrl.substring(lastSlash + 1) : imageUrl;
    }
}
