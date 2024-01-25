package com.viewnext.proyectoviewnext.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.viewnext.proyectoviewnext.R
import com.viewnext.proyectoviewnext.data.models.Filter
import com.viewnext.proyectoviewnext.utils.FilterService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FilterViewModel(application: Application) : AndroidViewModel(application) {
    private var filterSvc = FilterService

    fun getFilter(): String {
        return "$filterSvc"
    }

    fun getDateFrom(context: Context): String {
        return dateToString(FilterService.getDateFrom(), context)
    }

    fun getDateTo(context: Context): String {
        return dateToString(FilterService.getDateTo(), context)
    }

    private fun dateToString(date: Date?, context:Context): String {
        if (date == null) {
            return context.getString(R.string.title_buttonDayMonthYear)
        }
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun getMaxAmountInList(): Float {
        return FilterService.getMaxAmountInList()
    }

    fun getSelectedAmount(): Int {
        return FilterService.getSelectedAmount()
    }

    fun getFilterPaid(): Boolean {
        return FilterService.getStatusPaid()
    }

    fun getFilterCancelled(): Boolean {
        return FilterService.getStatusCancelled()
    }

    fun getFilterFixedFee(): Boolean {
        return FilterService.getStatusFixedFee()
    }

    fun getFilterPendingPayment(): Boolean {
        return FilterService.getStatusPendingPayment()
    }

    fun getFilterPaymentPlan(): Boolean {
        return FilterService.getStatusPaymentPlan()
    }

    fun setFilters(filter: Filter) {
        FilterService.setDateFrom(filter.dateFrom)
        FilterService.setDateTo(filter.dateTo)
        FilterService.setSelectedAmount(filter.selectedAmount)
        FilterService.setStatusPaid(filter.statusPaid)
        FilterService.setStatusCancelled(filter.statusCancelled)
        FilterService.setStatusFixedFee(filter.statusFixedFee)
        FilterService.setStatusPendingPayment(filter.statusPendingPayment)
        FilterService.setStatusPaymentPlan(filter.statusPaymentPlan)
    }
}