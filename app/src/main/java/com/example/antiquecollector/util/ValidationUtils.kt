package com.example.antiquecollector.util

import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utility class for form validation.
 */
@Singleton
class ValidationUtils @Inject constructor() {
    
    /**
     * Validates that a text field is not empty.
     *
     * @param text The text to validate
     * @return True if the text is not empty, false otherwise
     */
    fun isNotEmpty(text: String?): Boolean {
        return !text.isNullOrBlank()
    }
    
    /**
     * Validates that a text field has at least the specified minimum length.
     *
     * @param text The text to validate
     * @param minLength The minimum required length
     * @return True if the text meets the minimum length, false otherwise
     */
    fun hasMinLength(text: String?, minLength: Int): Boolean {
        return !text.isNullOrBlank() && text.length >= minLength
    }
    
    /**
     * Validates that a text field does not exceed the specified maximum length.
     *
     * @param text The text to validate
     * @param maxLength The maximum allowed length
     * @return True if the text does not exceed the maximum length, false otherwise
     */
    fun hasMaxLength(text: String?, maxLength: Int): Boolean {
        return text.isNullOrBlank() || text.length <= maxLength
    }
    
    /**
     * Validates that a text field is a valid email address.
     *
     * @param text The text to validate
     * @return True if the text is a valid email address, false otherwise
     */
    fun isValidEmail(text: String?): Boolean {
        return !text.isNullOrBlank() && Patterns.EMAIL_ADDRESS.matcher(text).matches()
    }
    
    /**
     * Validates that a text field contains only numeric characters.
     *
     * @param text The text to validate
     * @return True if the text contains only numeric characters, false otherwise
     */
    fun isNumeric(text: String?): Boolean {
        return !text.isNullOrBlank() && text.all { it.isDigit() }
    }
    
    /**
     * Validates that a text field is a valid currency value.
     *
     * @param text The text to validate
     * @return True if the text is a valid currency value, false otherwise
     */
    fun isValidCurrency(text: String?): Boolean {
        if (text.isNullOrBlank()) return false
        
        // Remove currency symbols and whitespace
        val cleanText = text.replace(Regex("[^0-9.,]"), "")
        
        // Check if it's a valid decimal number
        return try {
            cleanText.toDouble() >= 0
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Validates that a text field is a valid date in the format "MM/dd/yyyy".
     *
     * @param text The text to validate
     * @return True if the text is a valid date, false otherwise
     */
    fun isValidDate(text: String?): Boolean {
        if (text.isNullOrBlank()) return false
        
        return try {
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            dateFormat.isLenient = false
            dateFormat.parse(text)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Validates that a text field matches a specific pattern.
     *
     * @param text The text to validate
     * @param pattern The pattern to match
     * @return True if the text matches the pattern, false otherwise
     */
    fun matchesPattern(text: String?, pattern: Pattern): Boolean {
        return !text.isNullOrBlank() && pattern.matcher(text).matches()
    }
    
    /**
     * Validates that a text field is a valid URL.
     *
     * @param text The text to validate
     * @return True if the text is a valid URL, false otherwise
     */
    fun isValidUrl(text: String?): Boolean {
        return !text.isNullOrBlank() && Patterns.WEB_URL.matcher(text).matches()
    }
}