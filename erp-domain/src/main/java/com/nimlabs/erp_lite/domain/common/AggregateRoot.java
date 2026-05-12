package com.nimlabs.erp_lite.domain.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase base para todos los Aggregate Roots.
 * Los Aggregate Roots son entidades que sirven como punto de entrada a un agregado.
 * Son responsables de mantener la consistencia del agregado y publicar eventos de dominio.
 *
 * @param <ID> el tipo del identificador del Aggregate Root
 */
public abstract class AggregateRoot<ID> extends Entity<ID> {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected AggregateRoot(ID id) {
        super(id);
    }

    /**
     * Registra un evento de dominio para ser publicado.
     *
     * @param event el evento de dominio a registrar
     */
    protected void registerEvent(DomainEvent event) {
        if (event != null) {
            domainEvents.add(event);
        }
    }

    /**
     * Retorna todos los eventos de dominio y limpia la lista interna.
     * Este método debe ser llamado por la capa de infraestructura después de persistir el agregado.
     *
     * @return una lista no modificable de eventos de dominio
     */
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
}
