package com.nimlabs.erp_lite.domain.product;

/**
 * Referencia al Catalog en MongoDB.
 * Ejemplo: cat-electronics, cat-books
 *
 * @param categoryId el identificador de la categoría
 */
public record CategoryReference(String categoryId) {

    public CategoryReference {
        if (categoryId == null || categoryId.isBlank())
            throw new IllegalArgumentException("CategoryId cannot be null or blank");
    }

    /**
     * Crea una instancia de CategoryReference a partir de un valor String.
     *
     * @param categoryId el identificador de la categoría
     * @return una nueva instancia de CategoryReference
     */
    public static CategoryReference of(String categoryId) {

        return new CategoryReference(categoryId);
    }
}
