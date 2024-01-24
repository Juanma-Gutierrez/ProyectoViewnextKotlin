package com.viewnext.proyectoviewnext.ui.filter

import java.util.Date

object FilterService {
    private var maxAmountInList: Float = 0.0f
    private var dateFrom: Date? = null
    private var dateTo: Date? = null
    private var selectedAmount: Int = Integer.MAX_VALUE
    private var statePaid: Boolean = false
    private var stateCancelled: Boolean = false
    private var stateFixedFee: Boolean = false
    private var statePendingPayment: Boolean = false
    private var statePaymentPlan: Boolean = false

    // Date from
    fun setDateFrom(date: Date?) {
        this.dateFrom = date
    }

    fun getDateFrom(): Date? {
        return this.dateFrom
    }

    // Date to
    fun setDateTo(date: Date?) {
        this.dateTo = date
    }

    fun getDateTo(): Date? {
        return this.dateTo
    }

    // Max amount
    fun setMaxAmountInList(amount: Float) {
        this.maxAmountInList = amount
    }

    fun getMaxAmountInList(): Float {
        return maxAmountInList
    }

    // Selected amount
    fun setSelectedAmount(amount: Int) {
        this.selectedAmount = amount
    }

    fun getSelectedAmount(): Int {
        return this.selectedAmount
    }

    // Status
    fun setStatusPaid(state: Boolean) {
        this.statePaid = state
    }

    fun getStatusPaid(): Boolean {
        return this.statePaid
    }

    fun setStatusCancelled(state: Boolean) {
        this.stateCancelled = state
    }

    fun getStatusCancelled(): Boolean {
        return this.stateCancelled
    }

    fun setStatusFixedFee(state: Boolean) {
        this.stateFixedFee = state
    }

    fun getStatusFixedFee(): Boolean {
        return this.stateFixedFee
    }

    fun setStatusPendingPayment(state: Boolean) {
        this.statePendingPayment = state
    }

    fun getStatusPendingPayment(): Boolean {
        return this.statePendingPayment
    }

    fun setStatusPaymentPlan(state: Boolean) {
        this.statePaymentPlan = state
    }

    fun getStatusPaymentPlan(): Boolean {
        return this.statePaymentPlan
    }

    override fun toString(): String {
        return "\nDateFrom: $dateFrom\n" +
                "DateTo: $dateTo\n" +
                "MaxAmountInList: $maxAmountInList\n" +
                "SelectedAmount: $selectedAmount\n" +
                "Paid: $statePaid\n" +
                "Cancelled: $stateCancelled\n" +
                "FixedFee: $stateFixedFee\n" +
                "PendingPayment: $statePendingPayment\n" +
                "PaymentPlan: $statePaymentPlan\n"
    }
}


