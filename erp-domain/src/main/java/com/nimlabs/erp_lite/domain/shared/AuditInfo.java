package com.nimlabs.erp_lite.domain.shared;

import java.time.Instant;

public record AuditInfo(String createdBy, Instant createdAt, Instant updatedAt) {

    public AuditInfo {
        if (createdBy == null || createdBy.isBlank())
            throw new IllegalArgumentException("CreatedBy cannot be null or blank");
        if (createdAt == null) throw new IllegalArgumentException("CreatedAt cannot be null");
        if (updatedAt == null) throw new IllegalArgumentException("UpdatedAt cannot be null");
    }

    public static AuditInfo create(String createdBy, Instant timestamp) {
        return new AuditInfo(createdBy, timestamp, timestamp);
    }

    public AuditInfo updateTimestamp() {
        return new AuditInfo(createdBy, createdAt, Instant.now());
    }
}
