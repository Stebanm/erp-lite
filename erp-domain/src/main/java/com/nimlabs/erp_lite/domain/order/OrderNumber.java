package com.nimlabs.erp_lite.domain.order;

import java.time.LocalDate;
import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

/**
 * Número de pedido único.
 * Patrón: ORD-YYYY-NNN (ej. ORD-2025-001)
 *
 * @param value el valor del número de pedido
 */
public record OrderNumber(String value) {

    private static final Pattern ORDER_NUMBER_PATTERN = Pattern.compile("^ORD-\\d{4}-\\d{3}$");

    public OrderNumber {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("OrderNumber cannot be null or blank");
        }

        if (!ORDER_NUMBER_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Invalid OrderNumber format: " +
                            value +
                            ". Expected: ORD-YYYY-NNN (e.g., ORD-2025-001)"
            );
        }
    }

    /**
     * Crea una instancia de OrderNumber a partir de un valor String.
     *
     * @param value el valor del número de pedido
     * @return una nueva instancia de OrderNumber
     */
    public static OrderNumber of(String value) {
        return new OrderNumber(value);
    }

    /**
     * Genera un nuevo OrderNumber con el año actual y un número de secuencia.
     * Nota: En una implementación real, la secuencia debería obtenerse de una secuencia o contador de base de datos.
     *
     * @param sequence el número de secuencia (001-999)
     * @return una nueva instancia de OrderNumber
     */
    public static OrderNumber generate(int sequence) {
        if (sequence < 1 || sequence > 999) {
            throw new IllegalArgumentException("Sequence must be between 1 and 999");
        }

        int currentYear = Year.now().getValue();
        String oderNumber = String.format("ORD-%d-%03d", currentYear, sequence);
        return new OrderNumber(oderNumber);
    }
}
