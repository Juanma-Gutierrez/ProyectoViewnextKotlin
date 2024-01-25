package com.viewnext.proyectoviewnext.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

/**
 * A [DialogFragment] that displays a [DatePickerDialog] for selecting a date.
 *
 * @property listener A lambda function that takes three parameters: day, month, and year.
 * It is invoked when a date is set.
 */
class DatePickerFragment(
    val listener: (day: Int, month: Int, year: Int) -> Unit
) : DialogFragment(), DatePickerDialog.OnDateSetListener {
    /**
     * Invoked when a date is set in the [DatePickerDialog]. Calls the listener with the selected
     * day, month, and year.
     */
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener(dayOfMonth, month, year)
    }

    /**
     * Creates and returns a new instance of [DatePickerDialog] with the current date as the default.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)
        return DatePickerDialog(activity as Context, this, year, month, day)
    }
}