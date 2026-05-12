package com.nimlabs.erp_lite.domain.shared;

import java.time.Instant;

/**
 * Información de auditoría para los agregados.
 * Registra quién creó/actualizó la entidad y cuándo.
 *
 * @param createdBy  el nombre de usuario que creó la entidad
 * @param createdAt  la marca de tiempo de cuando la entidad fue creada
 * @param updatedAt  la marca de tiempo de la última actualización de la entidad
 */
public record AuditInfo(
        String createdBy,
        Instant createdAt,
        Instant updatedAt
) {

    public AuditInfo {
        if (createdBy == null || createdBy.isBlank()) {
            throw new IllegalArgumentException("CreatedBy cannot be null or blank");
        }

        if (createdAt == null) {
            throw new IllegalArgumentException("CreatedAt cannot be null");
        }

        if (updatedAt == null) {
            throw new IllegalArgumentException("UpdatedAt cannot be null");
        }

        if (updatedAt.isBefore(createdAt)) {
            throw new IllegalArgumentException("UpdatedAt cannot be before CreatedAt");
        }
    }

    /**
     * Crea un nuevo AuditInfo con la marca de tiempo actual para la creación y actualización.
     *
     * @param createdBy el nombre de usuario que creó la entidad
     * @param timestamp la marca de tiempo para la creación y actualización
     * @return una nueva instancia de AuditInfo
     */
    public static AuditInfo create(String createdBy, Instant timestamp) {
        return new AuditInfo(createdBy, timestamp, timestamp);
    }

    /**
     * Actualiza la marca de tiempo al momento actual.
     *
     * @return una nueva instancia de AuditInfo con la marca de tiempo actualizada
     */
    public AuditInfo updateTimestamp() {
        return new AuditInfo(this.createdBy, this.createdAt, Instant.now());
    }
}
