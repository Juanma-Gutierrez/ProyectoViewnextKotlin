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

    fun getMinAmount(): Float {
        return filterSvc.getFilterMinAmount()
    }

    fun getMaxAmount(): Float {
        return filterSvc.getFilterMinAmount()
    }

    fun getSelectedAmount():Float{
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
        println(filterSvc)
        filterSvc.setFilterDateFrom(filter.dateFrom)
        filterSvc.setFilterDateTo(filter.dateTo)
        filterSvc.setFilterMinAmount(filter.minAmount)
        filterSvc.setFilterMaxAmount(filter.maxAmount)
        filterSvc.setFilterSelectedAmount(filter.selectedAmount.toFloat())
        filterSvc.setFilterPaid(filter.statusPaid)
        filterSvc.setFilterCancelled(filter.statusCancelled)
        filterSvc.setFilterFixedFee(filter.statusFixedFee)
        filterSvc.setFilterPendingPayment(filter.statusPendingPayment)
        filterSvc.setFilterPaymentPlan(filter.statusPaymentPlan)
        println(filterSvc)
    }

    fun resetFilters() {
        filterSvc.setFilterDateFrom(null)
        filterSvc.setFilterDateTo(null)
        filterSvc.setFilterMinAmount(0f)
        filterSvc.setFilterMaxAmount(findMaxAmountProgressBar())
        filterSvc.setFilterSelectedAmount(findMaxAmountProgressBar())
        filterSvc.setFilterPaid(true)
        filterSvc.setFilterCancelled(true)
        filterSvc.setFilterFixedFee(true)
        filterSvc.setFilterPendingPayment(true)
        filterSvc.setFilterPaymentPlan(true)
    }

    private fun checkDate(date: String, context: Context): String {
        if (date == "null") {
            return context.getString(R.string.title_buttonDayMonthYear)
        }
        return date
    }

    fun findMaxAmountProgressBar(): Float {
        val filterSvc = FilterService
        val maxProgressBar = (filterSvc.getMaxAmountInList() + Constants.FRACTION_OF_AMOUNT)
        var fraction = Math.ceil((maxProgressBar / Constants.FRACTION_OF_AMOUNT).toDouble())
        fraction *= Constants.FRACTION_OF_AMOUNT
        return fraction.toFloat()
    }
}