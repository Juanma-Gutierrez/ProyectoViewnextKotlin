package com.viewnext.proyectoviewnext.utils

import com.viewnext.proyectoviewnext.data.models.Filter
import java.util.Date

/**
 * Singleton object to manage and store filter-related data.
 */
object FilterService {
    private var maxAmountInList: Float = 0.0f
    private var filter: Filter = Filter()

    // Date
    fun getDateFrom(): Date? {
        return filter.dateFrom
    }

    fun setDateFrom(date: Date?) {
        filter.dateFrom = date
    }

    fun getDateTo(): Date? {
        return filter.dateTo
    }

    fun setDateTo(date: Date?) {
        filter.dateTo = date
    }

    // Amount
    fun getMaxAmountInList(): Float {
        return maxAmountInList
    }

    fun setMaxAmountInList(amount: Float) {
        maxAmountInList = amount
    }

    fun getSelectedAmount(): Int {
        return filter.selectedAmount
    }

    fun setSelectedAmount(progress: Int) {
        filter.selectedAmount = progress
    }

    // Status
    fun getStatusPaid(): Boolean {
        return filter.statusPaid
    }

    fun setStatusPaid(state: Boolean) {
        filter.statusPaid = state
    }

    fun getStatusCancelled(): Boolean {
        return filter.statusCancelled
    }

    fun setStatusCancelled(state: Boolean) {
        filter.statusCancelled = state
    }

    fun getStatusFixedFee(): Boolean {
        return filter.statusFixedFee
    }

    fun setStatusFixedFee(state: Boolean) {
        filter.statusFixedFee = state
    }

    fun getStatusPendingPayment(): Boolean {
        return filter.statusPendingPayment
    }

    fun setStatusPendingPayment(state: Boolean) {
        filter.statusPendingPayment = state
    }

    fun getStatusPaymentPlan(): Boolean {
        return filter.statusPaymentPlan
    }

    fun setStatusPaymentPlan(state: Boolean) {
        filter.statusPaymentPlan = state
    }

    /**
     * Returns a string representation of the current filter.
     */
    override fun toString(): String {
        return filter.toString()
    }
}


