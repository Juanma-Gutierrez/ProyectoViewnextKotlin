package com.viewnext.proyectoviewnext.utils

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getColor
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.viewnext.proyectoviewnext.R
import com.viewnext.proyectoviewnext.constants.Constants
import com.viewnext.proyectoviewnext.data.models.Filter
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

@RequiresApi(Build.VERSION_CODES.O)
fun parseLocalDate(date: String): LocalDate {
    val formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)
    return LocalDate.parse(date, formatter)
}


fun getYearFromStringDate(date: String): Int {
    val calendar = createCalendar(date)
    return calendar.get(Calendar.YEAR)
}

fun getMonthFromStringDate(date: String): Int {
    val calendar = createCalendar(date)
    return calendar.get(Calendar.MONTH)
}

fun getDayFromStringDate(date: String): Int {
    val calendar = createCalendar(date)
    return calendar.get(Calendar.DAY_OF_MONTH)
}

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
 * @return A string representing the formatted date.
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
 * @return A [Date] object representing the parsed date.
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
 * @param bt The MaterialButton to which the selected date will be set.
 * @return A [Calendar] object representing the selected date.
 */
fun showDatePickerDialog(bt: MaterialButton, context: Context, filterToApply: Filter): Calendar {
    val calendar = Calendar.getInstance()
    var year = 0
    var month = 0
    var day = 0
    if (bt.text == context.getString(R.string.title_buttonDayMonthYear)) {
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
    } else {
        year = getYearFromStringDate(bt.text.toString())
        month = getMonthFromStringDate(bt.text.toString())
        day = getDayFromStringDate(bt.text.toString())
        println("Year: $year,  month: $month, day: $day")
    }
    var picker = DatePickerDialog(
        context,
        { _, _year, _month, _day ->
            calendar.set(_year, _month, _day)
            bt.text = dateToString(calendar.time, context)
        }, year, month, day
    )
    picker = calculateMin(picker, filterToApply)
    picker = calculateMax(picker, filterToApply)
    picker.show()
    return calendar
}


fun calculateMin(picker: DatePickerDialog, filterToApply: Filter): DatePickerDialog {
    val calendar = Calendar.getInstance()
    val minDate = filterToApply.dateFrom
    println("Mindate***************************\n$filterToApply")
    if (minDate != null) {
        println("Entra en minDate if: $minDate")
        calendar.set(minDate.year, minDate.month, minDate.day)
        picker.datePicker.minDate = calendar.timeInMillis
    }
    return picker
}

fun calculateMax(picker: DatePickerDialog, filterToApply: Filter): DatePickerDialog {
    val calendar = Calendar.getInstance()
    // val maxDate = filterToApply.dateTo
    // if (maxDate != null) {
    //     calendar.set(maxDate.year, maxDate.month, maxDate.day)
    // }
    println("Maxdate***************************\n$filterToApply")
    picker.datePicker.maxDate = calendar.timeInMillis
    return picker
}