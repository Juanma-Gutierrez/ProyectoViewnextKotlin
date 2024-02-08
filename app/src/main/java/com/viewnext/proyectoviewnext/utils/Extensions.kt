package com.viewnext.proyectoviewnext.utils

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getColor
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.viewnext.proyectoviewnext.R
import com.viewnext.proyectoviewnext.constants.Constants
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Displays a snackbar with the provided message.
 *
 * @param message The message to be displayed in the snackbar.
 * @param view The view where the snackbar should be displayed.
 * @param color The color resource for the background tint of the snackbar.
 * @param length The duration for which the snackbar should be displayed.
 */
fun showSnackBar(message: String, view: View, color: Int, length: Int = Snackbar.LENGTH_SHORT) {
    val snackBar =
        Snackbar.make(view, message, length).setBackgroundTint(getColor(view.context, color))
    snackBar.show()
}

/**
 * Displays an alert dialog with the provided title, message, and close button text.
 *
 * @param context The context in which the alert dialog should be displayed.
 * @param title The title of the alert dialog.
 * @param message The message to be displayed in the alert dialog.
 * @param btCloseText The text for the close button in the alert dialog.
 */
fun showAlertDialog(context: Context, title: String, message: String, btCloseText: String) {
    val builder = MaterialAlertDialogBuilder(context)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton(btCloseText) { dialog, _ -> dialog.dismiss() }
    val alertDialog = builder.create()
    alertDialog.show()
}

/**
 * Parses a string representation of a local date into a [LocalDate] object.
 *
 * @param date The string representation of the date.
 * @return The parsed [LocalDate] object.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun parseLocalDate(date: String): LocalDate {
    val formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)
    return LocalDate.parse(date, formatter)
}

/**
 * Extracts the year from a string date representation.
 *
 * @param date The string representation of the date.
 * @return The year value extracted from the date.
 */
fun getYearFromStringDate(date: String): Int {
    val calendar = createCalendar(date)
    return calendar.get(Calendar.YEAR)
}

/**
 * Extracts the month from a string date representation.
 *
 * @param date The string representation of the date.
 * @return The month value extracted from the date.
 */
fun getMonthFromStringDate(date: String): Int {
    val calendar = createCalendar(date)
    return calendar.get(Calendar.MONTH)
}

/**
 * Extracts the day from a string date representation.
 *
 * @param date The string representation of the date.
 * @return The day value extracted from the date.
 */
fun getDayFromStringDate(date: String): Int {
    val calendar = createCalendar(date)
    return calendar.get(Calendar.DAY_OF_MONTH)
}

/**
 * Creates a [Calendar] object from a string date representation.
 *
 * @param date The string representation of the date.
 * @return The [Calendar] object representing the parsed date.
 */
fun createCalendar(date: String): Calendar {
    val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
    val parsedDate = dateFormat.parse(date)
    val calendar = Calendar.getInstance()
    calendar.time = parsedDate
    return calendar
}

/**
 * Converts a [Date] object to a formatted string representation.
 *
 * @param date The date to be converted.
 * @param context The context for accessing resources.
 * @return A formatted string representing the date.
 */
fun dateToString(date: Date?, context: Context): String {
    if (date == null) return context.getString(R.string.title_buttonDayMonthYear)
    val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(date)
}


/**
 * Converts a string date representation to a [Date] object.
 *
 * @param dateString The string representation of the date.
 * @param context The context for accessing resources.
 * @return The [Date] object representing the parsed date.
 */
fun stringToDate(dateString: CharSequence, context: Context): Date? {
    if (dateString.toString() == context.getString(R.string.title_buttonDayMonthYear)) {
        return null
    }
    var date = Date()
    val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
    try {
        date = dateFormat.parse(dateString.toString())!!
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return date
}


/**
 * Displays a DatePickerDialog and sets the selected date to the specified MaterialButton.
 *
 * @param context The context in which the DatePickerDialog should be displayed.
 * @param selectedDate The initially selected date.
 * @param minDate The minimum selectable date.
 * @param maxDate The maximum selectable date.
 * @param callback A callback function to handle the selected date.
 */
fun showDatePickerDialog(
    context: Context,
    selectedDate: Date?,
    minDate: Date?,
    maxDate: Date?,
    callback: (Calendar) -> Unit
) {
    val calendar = Calendar.getInstance()
    if (selectedDate != null) {
        calendar.time = selectedDate
    }
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    var picker = DatePickerDialog(
        context,
        { _, y, m, d ->
            calendar.set(y, m, d)
            callback(calendar)
        }, year, month, day
    )
    picker = setLimits(picker, minDate, "min")
    picker = setLimits(picker, maxDate, "max")
    picker.show()
}

/**
 * Sets the limits (min or max) for the DatePickerDialog.
 *
 * @param picker The DatePickerDialog instance.
 * @param date The limit date.
 * @param limit The type of limit ("min" or "max").
 * @return The modified DatePickerDialog instance.
 */
fun setLimits(picker: DatePickerDialog, date: Date?, limit: String): DatePickerDialog {
    val calendar = Calendar.getInstance()
    if (date != null) {
        calendar.time = date
        when (limit) {
            "min" -> picker.datePicker.minDate = calendar.timeInMillis
            "max" -> picker.datePicker.maxDate = calendar.timeInMillis
        }
    }
    return picker
}