package com.nimlabs.erp_lite.domain.product;

public record CategoryReference(String categoryId) {

    public CategoryReference {
        if (categoryId == null || categoryId.isBlank())
            throw new IllegalArgumentException("CategoryId cannot be null or blank");
    }

    public static CategoryReference of(String categoryId) {
        return new CategoryReference(categoryId);
    }
}
