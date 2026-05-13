package com.nimlabs.erp_lite.domain.product;

import java.net.URI;

/**
 * URL de imagen del producto almacenada en AWS S3.
 * Valida que la URL esté bien formada.
 *
 * @param imageUrl la URL de la imagen
 */
public record ProductImage(String imageUrl) {

    public ProductImage {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("ImageUrl cannot be null or blank");
        }

        try {
            URI uri = URI.create(imageUrl);

            if (!uri.isAbsolute()) {
                throw new IllegalArgumentException("Invalid image URL (not absolute): " + imageUrl);
            }

            if (
                    uri.getScheme() == null ||
                    !(uri.getScheme().equals("http") || uri.getScheme().equals("https"))
            ) {
                throw new IllegalArgumentException("Invalid image URL (missing scheme): " + imageUrl);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid image URL: " + imageUrl);
        }
    }

    /**
     * Crea una instancia de ProductImage a partir de una URL String.
     *
     * @param imageUrl la URL de la imagen
     * @return una nueva instancia de ProductImage
     */
    public static ProductImage of(String imageUrl) {

        return new ProductImage(imageUrl);
    }

    /**
     * Retorna la URL completa.
     *
     * @return la URL de la imagen
     */
    public String getFullUrl() {

        return imageUrl;
    }

    /**
     * Extrae el nombre del archivo de la URL.
     *
     * @return el nombre del archivo o la URL completa si la extracción falla
     */
    public String getFileName() {
        int lastSlash = imageUrl.lastIndexOf('/');
        return lastSlash >= 0 ? imageUrl.substring(lastSlash + 1) : imageUrl;
    }
}
