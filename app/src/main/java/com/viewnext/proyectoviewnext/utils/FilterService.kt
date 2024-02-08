package com.viewnext.proyectoviewnext.utils

import com.viewnext.proyectoviewnext.data.models.Filter
import java.util.Date

/**
 * Singleton object to manage and store filter-related data.
 */
object FilterService {
    private var filter: Filter = Filter()
    var statusList: ArrayList<String> = ArrayList()
    var filterToApply: Filter = Filter()

    /**
     * Retrieves the start date for filtering.
     *
     * @return The start date for filtering.
     */
    fun getDateFrom(): Date? {
        return filter.dateFrom
    }

    /**
     * Sets the start date for filtering.
     *
     * @param date The start date for filtering.
     */
    fun setDateFrom(date: Date?) {
        filter.dateFrom = date
    }

    /**
     * Retrieves the end date for filtering.
     *
     * @return The end date for filtering.
     */
    fun getDateTo(): Date? {
        return filter.dateTo
    }

    /**
     * Sets the end date for filtering.
     *
     * @param date The end date for filtering.
     */
    fun setDateTo(date: Date?) {
        filter.dateTo = date
    }

    /**
     * Retrieves the maximum amount in the list for filtering.
     *
     * @return The maximum amount in the list for filtering.
     */
    fun getMaxAmountInList(): Float {
        return filter.maxAmountInList
    }

    /**
     * Sets the maximum amount in the list for filtering.
     *
     * @param amount The maximum amount in the list for filtering.
     */
    fun setMaxAmountInList(amount: Float) {
        filter.maxAmountInList = amount
    }

    /**
     * Retrieves the selected amount for filtering.
     *
     * @return The selected amount for filtering.
     */
    fun getSelectedAmount(): Int {
        return filter.selectedAmount
    }

    /**
     * Sets the selected amount for filtering.
     *
     * @param progress The selected amount for filtering.
     */
    fun setSelectedAmount(progress: Int) {
        filter.selectedAmount = progress
    }

    /**
     * Retrieves the status for filtering paid invoices.
     *
     * @return True if filtering paid invoices is enabled, false otherwise.
     */
    fun getStatusPaid(): Boolean {
        return filter.statusPaid
    }

    /**
     * Sets the status for filtering paid invoices.
     *
     * @param state True to enable filtering paid invoices, false otherwise.
     */
    fun setStatusPaid(state: Boolean) {
        filter.statusPaid = state
    }

    /**
     * Retrieves the status for filtering cancelled invoices.
     *
     * @return True if filtering cancelled invoices is enabled, false otherwise.
     */
    fun getStatusCancelled(): Boolean {
        return filter.statusCancelled
    }

    /**
     * Sets the status for filtering cancelled invoices.
     *
     * @param state True to enable filtering cancelled invoices, false otherwise.
     */
    fun setStatusCancelled(state: Boolean) {
        filter.statusCancelled = state
    }

    /**
     * Retrieves the status for filtering invoices with fixed fee.
     *
     * @return True if filtering invoices with fixed fee is enabled, false otherwise.
     */
    fun getStatusFixedFee(): Boolean {
        return filter.statusFixedFee
    }

    /**
     * Sets the status for filtering invoices with fixed fee.
     *
     * @param state True to enable filtering invoices with fixed fee, false otherwise.
     */
    fun setStatusFixedFee(state: Boolean) {
        filter.statusFixedFee = state
    }

    /**
     * Retrieves the status for filtering invoices pending payment.
     *
     * @return True if filtering invoices pending payment is enabled, false otherwise.
     */
    fun getStatusPendingPayment(): Boolean {
        return filter.statusPendingPayment
    }

    /**
     * Sets the status for filtering invoices pending payment.
     *
     * @param state True to enable filtering invoices pending payment, false otherwise.
     */
    fun setStatusPendingPayment(state: Boolean) {
        filter.statusPendingPayment = state
    }

    /**
     * Retrieves the status for filtering invoices under payment plan.
     *
     * @return True if filtering invoices under payment plan is enabled, false otherwise.
     */
    fun getStatusPaymentPlan(): Boolean {
        return filter.statusPaymentPlan
    }

    /**
     * Sets the status for filtering invoices under payment plan.
     *
     * @param state True to enable filtering invoices under payment plan, false otherwise.
     */
    fun setStatusPaymentPlan(state: Boolean) {
        filter.statusPaymentPlan = state
    }

    /**
     * Returns a string representation of the current filter.
     *
     * @return A string representation of the current filter.
     */
    override fun toString(): String {
        return filter.toString()
    }
}


