package com.example.antiquecollector.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utility class for handling date operations.
 */
@Singleton
class DateUtils @Inject constructor() {
    
    /**
     * Formats a date as a display string in the format "MMM d, yyyy".
     * Example: "Jan 1, 2023"
     *
     * @param date The date to format
     * @return The formatted date string
     */
    fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }
    
    /**
     * Formats a date as a short display string in the format "MM/dd/yyyy".
     * Example: "01/01/2023"
     *
     * @param date The date to format
     * @return The formatted date string
     */
    fun formatShortDate(date: Date): String {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }
    
    /**
     * Parses a date string in the format "MM/dd/yyyy" to a Date object.
     *
     * @param dateString The date string to parse
     * @return The parsed Date object, or null if parsing failed
     */
    fun parseDate(dateString: String): Date? {
        return try {
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Gets the start of the day for the given date.
     *
     * @param date The date to get the start of the day for
     * @return The start of the day
     */
    fun getStartOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
    
    /**
     * Gets the end of the day for the given date.
     *
     * @param date The date to get the end of the day for
     * @return The end of the day
     */
    fun getEndOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }
    
    /**
     * Calculates the number of days between two dates.
     *
     * @param start The start date
     * @param end The end date
     * @return The number of days between the two dates
     */
    fun daysBetween(start: Date, end: Date): Int {
        val startDay = getStartOfDay(start)
        val endDay = getStartOfDay(end)
        val diff = endDay.time - startDay.time
        return (diff / (24 * 60 * 60 * 1000)).toInt()
    }
    
    /**
     * Returns the current date.
     *
     * @return The current date
     */
    fun getCurrentDate(): Date {
        return Date()
    }
    
    /**
     * Adds the specified number of days to the given date.
     *
     * @param date The date to add days to
     * @param days The number of days to add
     * @return The new date
     */
    fun addDays(date: Date, days: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH, days)
        return calendar.time
    }
    
    /**
     * Subtracts the specified number of days from the given date.
     *
     * @param date The date to subtract days from
     * @param days The number of days to subtract
     * @return The new date
     */
    fun subtractDays(date: Date, days: Int): Date {
        return addDays(date, -days)
    }
    
    /**
     * Adds the specified number of months to the given date.
     *
     * @param date The date to add months to
     * @param months The number of months to add
     * @return The new date
     */
    fun addMonths(date: Date, months: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MONTH, months)
        return calendar.time
    }
    
    /**
     * Gets the date for the start of the current month.
     *
     * @return The start of the current month
     */
    fun getStartOfCurrentMonth(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        return getStartOfDay(calendar.time)
    }
    
    /**
     * Gets the date for the end of the current month.
     *
     * @return The end of the current month
     */
    fun getEndOfCurrentMonth(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        return getEndOfDay(calendar.time)
    }
}