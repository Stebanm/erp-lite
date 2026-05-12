package com.nimlabs.erp_lite.domain.shared;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

/**
 * Valor monetario inmutable con moneda.
 * Representa dinero con monto y código de moneda.
 *
 * @param amount   el monto monetario (debe ser >= 0)
 * @param currency el código de moneda
 */
public record Money(BigDecimal amount, Currency currency) {

    public Money {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }

        if (currency == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative, got: " + amount);
        }

        // Escalar a 2 decimales para consistencia
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Crea una instancia de Money a partir de BigDecimal y Currency.
     *
     * @param amount   el monto monetario
     * @param currency la moneda
     * @return una nueva instancia de Money
     */
    public static Money of(BigDecimal amount, Currency currency) {

        return new Money(amount, currency);
    }

    /**
     * Crea una instancia de Money a partir de double y Currency.
     *
     * @param amount   el monto monetario
     * @param currency la moneda
     * @return una nueva instancia de Money
     */
    public static Money of(double amount, Currency currency) {

        return new Money(BigDecimal.valueOf(amount), currency);
    }

    /**
     * Suma otro valor de Money a este.
     * Ambos objetos Money deben tener la misma moneda.
     *
     * @param other el Money a sumar
     * @return una nueva instancia de Money con la suma
     * @throws IllegalArgumentException si las monedas no coinciden
     */
    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    /**
     * Resta otro valor de Money a este.
     * Ambos objetos Money deben tener la misma moneda.
     *
     * @param other el Money a restar
     * @return una nueva instancia de Money con la diferencia
     * @throws IllegalArgumentException si las monedas no coinciden o el resultado es negativo
     */
    public Money subtract(Money other) {
        validateSameCurrency(other);
        BigDecimal result = this.amount.subtract(other.amount);

        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Subtraction would result in negative amount");
        }

        return new Money(result, this.currency);
    }

    /**
     * Multiplica este Money por un multiplicador entero.
     *
     * @param multiplier el multiplicador
     * @return una nueva instancia de Money con el producto
     */
    public Money multiply(int multiplier) {
        if (multiplier < 0) {
            throw new IllegalArgumentException("Multiplier cannot be negative");
        }

        return new Money(this.amount.multiply(BigDecimal.valueOf(multiplier)), this.currency);
    }

    /**
     * Multiplica este Money por una Quantity.
     *
     * @param quantity la cantidad por la que multiplicar
     * @return una nueva instancia de Money con el producto
     */
    public Money multiply(Quantity quantity) {

        return multiply(quantity.value());
    }

    /**
     * Valida que dos instancias de Money tengan la misma moneda.
     * Este método es utilizado internamente antes de realizar operaciones aritméticas.
     *
     * @param other el Money a comparar
     * @throws IllegalArgumentException si las monedas no coinciden
     */
    private void validateSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    "Cannot operate on Money with different currencies: " +
                            this.currency +
                            " vs "
                            + other.currency
            );
        }
    }

    @Override
    public String toString() {
        return amount + " " + currency.getCurrencyCode();
    }
}
