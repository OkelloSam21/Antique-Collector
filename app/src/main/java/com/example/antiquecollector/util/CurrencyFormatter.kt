package com.example.antiquecollector.util

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utility class for formatting currency values.
 */
@Singleton
class CurrencyFormatter @Inject constructor() {
    
    /**
     * Formats a value as currency using the default locale.
     *
     * @param value The value to format
     * @return The formatted currency string
     */
    fun formatCurrency(value: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        return format.format(value)
    }
    
    /**
     * Formats a value as currency using the specified currency code.
     *
     * @param value The value to format
     * @param currencyCode The ISO 4217 currency code (e.g., "USD", "EUR")
     * @return The formatted currency string
     */
    fun formatCurrency(value: Double, currencyCode: String): String {
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        format.currency = Currency.getInstance(currencyCode)
        return format.format(value)
    }
    
    /**
     * Formats a value as currency using the specified locale and currency code.
     *
     * @param value The value to format
     * @param locale The locale to use for formatting
     * @param currencyCode The ISO 4217 currency code (e.g., "USD", "EUR")
     * @return The formatted currency string
     */
    fun formatCurrency(value: Double, locale: Locale, currencyCode: String): String {
        val format = NumberFormat.getCurrencyInstance(locale)
        format.currency = Currency.getInstance(currencyCode)
        return format.format(value)
    }
    
    /**
     * Parses a currency string to a Double value.
     *
     * @param currencyString The currency string to parse
     * @return The parsed Double value, or null if parsing failed
     */
    fun parseCurrency(currencyString: String): Double? {
        return try {
            val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
            format.parse(currencyString)?.toDouble()
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Gets the currency symbol for the specified currency code.
     *
     * @param currencyCode The ISO 4217 currency code (e.g., "USD", "EUR")
     * @return The currency symbol
     */
    fun getCurrencySymbol(currencyCode: String): String {
        return Currency.getInstance(currencyCode).symbol
    }
    
    /**
     * Gets a list of available currency codes.
     *
     * @return A list of available currency codes
     */
    fun getAvailableCurrencyCodes(): List<String> {
        return Currency.getAvailableCurrencies().map { it.currencyCode }.sorted()
    }
    
    /**
     * Gets the display name for the specified currency code.
     *
     * @param currencyCode The ISO 4217 currency code (e.g., "USD", "EUR")
     * @return The display name for the currency
     */
    fun getCurrencyDisplayName(currencyCode: String): String {
        return Currency.getInstance(currencyCode).displayName
    }
}