package com.viewnext.proyectoviewnext.ui.filter

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.viewnext.proyectoviewnext.R
import com.viewnext.proyectoviewnext.constants.Constants
import com.viewnext.proyectoviewnext.data.models.Filter

class FilterViewModel(application: Application) : AndroidViewModel(application) {
    private var filterSvc = FilterService

    fun getFilter(): String {
        return "$filterSvc"
    }

    fun getDateFrom(context: Context): String {
        return checkDate(filterSvc.getFilterDateFrom().toString(), context)
    }

    fun getDateTo(context: Context): String {
        return checkDate(filterSvc.getFilterDateFrom().toString(), context)
    }

    fun getSelectedAmount():Int{
        return filterSvc.getFilterSelectedAmount()
    }

    fun getFilterPaid(): Boolean {
        return filterSvc.getFilterPaid()
    }

    fun getFilterCancelled(): Boolean {
        return filterSvc.getFilterCancelled()
    }

    fun getFilterFixedFee(): Boolean {
        return filterSvc.getFilterFixedFee()
    }

    fun getFilterPendingPayment(): Boolean {
        return filterSvc.getFilterPendingPayment()
    }

    fun getFilterPaymentPlan(): Boolean {
        return filterSvc.getFilterPaymentPlan()
    }

    fun setFilters(filter: Filter) {
        filterSvc.setFilterDateFrom(filter.dateFrom)
        filterSvc.setFilterDateTo(filter.dateTo)
        filterSvc.setFilterMaxAmount(filter.maxAmount)
        filterSvc.setFilterSelectedAmount(filter.selectedAmount)
        filterSvc.setFilterPaid(filter.statusPaid)
        filterSvc.setFilterCancelled(filter.statusCancelled)
        filterSvc.setFilterFixedFee(filter.statusFixedFee)
        filterSvc.setFilterPendingPayment(filter.statusPendingPayment)
        filterSvc.setFilterPaymentPlan(filter.statusPaymentPlan)
    }

    fun resetFilters() {
        filterSvc.setFilterDateFrom(null)
        filterSvc.setFilterDateTo(null)
        filterSvc.setFilterMaxAmount(findMaxAmountProgressBar().toFloat())
        filterSvc.setFilterSelectedAmount(findMaxAmountProgressBar())
        filterSvc.setFilterPaid(false)
        filterSvc.setFilterCancelled(false)
        filterSvc.setFilterFixedFee(false)
        filterSvc.setFilterPendingPayment(false)
        filterSvc.setFilterPaymentPlan(false)
    }

    private fun checkDate(date: String, context: Context): String {
        if (date == "null") {
            return context.getString(R.string.title_buttonDayMonthYear)
        }
        return date
    }

    fun findMaxAmountProgressBar(): Int {
        val filterSvc = FilterService
        val maxProgressBar = (filterSvc.getMaxAmountInList() + Constants.FRACTION_OF_AMOUNT)
        var fraction= Math.ceil((maxProgressBar / Constants.FRACTION_OF_AMOUNT).toDouble())
        fraction *= Constants.FRACTION_OF_AMOUNT
        return fraction.toInt()
    }
}