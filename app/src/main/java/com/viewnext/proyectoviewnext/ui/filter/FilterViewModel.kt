package com.viewnext.proyectoviewnext.ui.filter

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.viewnext.proyectoviewnext.R
import com.viewnext.proyectoviewnext.constants.Constants
import com.viewnext.proyectoviewnext.data.models.Filter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.ceil

class FilterViewModel(application: Application) : AndroidViewModel(application) {
    private var filterSvc = FilterService

    fun getFilter(): String {
        return "$filterSvc"
    }

    fun getDateFrom(context: Context): String {
        return checkDate(dateToString(filterSvc.getDateFrom()), context)
    }

    fun getDateTo(context: Context): String {
        return checkDate(dateToString(filterSvc.getDateTo()), context)
    }

    private fun dateToString(date: Date?): String {
        if (date == null) {
            return "día/mes/año"
        }
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun getMaxAmountInList(): Float {
        println("-------------muestra el filtro en maxamount ----------------")
        println(filterSvc)
        return filterSvc.getMaxAmountInList()
    }

    fun getSelectedAmount(): Int {
        return filterSvc.getSelectedAmount()
    }

    fun getFilterPaid(): Boolean {
        return filterSvc.getStatusPaid()
    }

    fun getFilterCancelled(): Boolean {
        return filterSvc.getStatusCancelled()
    }

    fun getFilterFixedFee(): Boolean {
        return filterSvc.getStatusFixedFee()
    }

    fun getFilterPendingPayment(): Boolean {
        return filterSvc.getStatusPendingPayment()
    }

    fun getFilterPaymentPlan(): Boolean {
        return filterSvc.getStatusPaymentPlan()
    }

    fun setFilters(filter: Filter) {
        filterSvc.setDateFrom(filter.dateFrom)
        filterSvc.setDateTo(filter.dateTo)
        filterSvc.setSelectedAmount(filter.selectedAmount)
        filterSvc.setStatusPaid(filter.statusPaid)
        filterSvc.setStatusCancelled(filter.statusCancelled)
        filterSvc.setStatusFixedFee(filter.statusFixedFee)
        filterSvc.setStatusPendingPayment(filter.statusPendingPayment)
        filterSvc.setStatusPaymentPlan(filter.statusPaymentPlan)
    }
/*
    fun resetFilters() {
        filterSvc.setDateFrom(null)
        filterSvc.setDateTo(null)
        filterSvc.setSelectedAmount(ceil(filterSvc.getMaxAmountInList()).toInt())
        filterSvc.setStatusPaid(false)
        filterSvc.setStatusCancelled(false)
        filterSvc.setStatusFixedFee(false)
        filterSvc.setStatusPendingPayment(false)
        filterSvc.setStatusPaymentPlan(false)
    }
*/
    private fun checkDate(date: String, context: Context): String {
        if (date == "null") {
            return context.getString(R.string.title_buttonDayMonthYear)
        }
        return date
    }
}