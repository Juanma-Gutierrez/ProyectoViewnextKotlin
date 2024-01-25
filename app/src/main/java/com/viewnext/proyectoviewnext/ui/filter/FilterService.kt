package com.viewnext.proyectoviewnext.ui.filter

import com.viewnext.proyectoviewnext.data.models.Filter
import java.util.Date

object FilterService {
    private var maxAmountInList: Float = 0.0f
    private var filter: Filter = Filter()

    // Date
    fun getDateFrom(): Date? {
        return this.filter.dateFrom
    }

    fun setDateFrom(date: Date?) {
        this.filter.dateFrom = date
    }

    fun getDateTo(): Date? {
        return this.filter.dateTo
    }

    fun setDateTo(date: Date?) {
        this.filter.dateTo = date
    }

    // Amount
    fun getMaxAmountInList(): Float {
        return maxAmountInList
    }

    fun setMaxAmountInList(amount: Float) {
        this.maxAmountInList = amount
    }

    fun getSelectedAmount(): Int {
        return this.filter.selectedAmount
    }

    fun setSelectedAmount(progress: Int) {
        this.filter.selectedAmount = progress
    }

    // Status
    fun getStatusPaid(): Boolean {
        return this.filter.statusPaid
    }

    fun setStatusPaid(state: Boolean) {
        this.filter.statusPaid = state
    }

    fun getStatusCancelled(): Boolean {
        return this.filter.statusCancelled
    }

    fun setStatusCancelled(state: Boolean) {
        this.filter.statusCancelled = state
    }

    fun getStatusFixedFee(): Boolean {
        return this.filter.statusFixedFee
    }

    fun setStatusFixedFee(state: Boolean) {
        this.filter.statusFixedFee = state
    }

    fun getStatusPendingPayment(): Boolean {
        return this.filter.statusPendingPayment
    }

    fun setStatusPendingPayment(state: Boolean) {
        this.filter.statusPendingPayment = state
    }

    fun getStatusPaymentPlan(): Boolean {
        return this.filter.statusPaymentPlan
    }

    fun setStatusPaymentPlan(state: Boolean) {
        this.filter.statusPaymentPlan = state
    }

    fun reset() {
        this.filter.reset()
    }

    override fun toString(): String {
        return this.filter.toString()
    }
}


