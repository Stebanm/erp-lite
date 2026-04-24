package com.nimlabs.erp_lite.persistence.mongo.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "catalogs")
public class CatalogDocument {

    @Id
    private String id;

    @Field(name = "active")
    private boolean active;

    private CatalogType catalogType;

    private Instant createAt;

    private Instant updateAt;

    private String description;

    private String name;

    private List<CatalogItem> items;
}
