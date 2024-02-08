package com.viewnext.proyectoviewnext.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.viewnext.proyectoviewnext.data.models.Filter
import com.viewnext.proyectoviewnext.utils.FilterService
import com.viewnext.proyectoviewnext.utils.dateToString

/**
 * ViewModel class responsible for managing filter-related data and interactions.
 *
 * @param application The application context.
 */
class FilterViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * Retrieves the start date of the filter as a formatted string.
     *
     * @param context The context used for string formatting.
     * @return A formatted string representing the start date of the filter.
     */
    fun getDateFrom(context: Context): String {
        return dateToString(FilterService.getDateFrom(), context)
    }

    /**
     * Retrieves the end date of the filter as a formatted string.
     *
     * @param context The context used for string formatting.
     * @return A formatted string representing the end date of the filter.
     */
    fun getDateTo(context: Context): String {
        return dateToString(FilterService.getDateTo(), context)
    }

    /**
     * Retrieves the maximum amount in the list to be filtered.
     *
     * @return The maximum amount in the list.
     */
    fun getMaxAmountInList(): Float {
        return FilterService.getMaxAmountInList()
    }

    /**
     * Retrieves the selected amount for filtering.
     *
     * @return The selected amount.
     */
    fun getSelectedAmount(): Int {
        return FilterService.getSelectedAmount()
    }

    /**
     * Retrieves the status filter for paid invoices.
     *
     * @return True if the filter for paid invoices is enabled, false otherwise.
     */
    fun getFilterPaid(): Boolean {
        return FilterService.getStatusPaid()
    }

    /**
     * Retrieves the status filter for cancelled invoices.
     *
     * @return True if the filter for cancelled invoices is enabled, false otherwise.
     */
    fun getFilterCancelled(): Boolean {
        return FilterService.getStatusCancelled()
    }

    /**
     * Retrieves the status filter for invoices with fixed fee.
     *
     * @return True if the filter for invoices with fixed fee is enabled, false otherwise.
     */
    fun getFilterFixedFee(): Boolean {
        return FilterService.getStatusFixedFee()
    }

    /**
     * Retrieves the status filter for invoices pending payment.
     *
     * @return True if the filter for invoices pending payment is enabled, false otherwise.
     */
    fun getFilterPendingPayment(): Boolean {
        return FilterService.getStatusPendingPayment()
    }

    /**
     * Retrieves the status filter for invoices with payment plan.
     *
     * @return True if the filter for invoices with payment plan is enabled, false otherwise.
     */
    fun getFilterPaymentPlan(): Boolean {
        return FilterService.getStatusPaymentPlan()
    }

    /**
     * Sets the filters based on the provided filter object.
     *
     * @param filter The filter object containing the filter parameters.
     */
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