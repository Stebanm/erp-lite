package com.nimlabs.erp_lite.domain.shared;

import java.util.regex.Pattern;

/**
 * Dirección de correo electrónico para notificaciones.
 * Valida el formato del correo usando un patrón regex básico.
 *
 * @param value la dirección de correo electrónico
 */
public record Email(String value) {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public Email {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }
    }

    /**
     * Crea un Email a partir de un valor String.
     *
     * @param value la dirección de correo electrónico
     * @return una nueva instancia de Email
     */
    public static Email of(String value) {

        return new Email(value);
    }
}
