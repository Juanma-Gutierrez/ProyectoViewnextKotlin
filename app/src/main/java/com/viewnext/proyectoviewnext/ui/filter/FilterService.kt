package com.viewnext.proyectoviewnext.ui.filter

import java.util.Date

object FilterService {
    private var maxAmountInList: Float = 0.0f
    private var dateFrom: Date = Date()
    private var dateTo: Date = Date()
    private var minAmount: Float = 0.0f
    private var maxAmount: Float = 0.0f
    private var statePaid: Boolean = false
    private var stateCancelled: Boolean = false
    private var stateFixedFee: Boolean = false
    private var statePendingPayment: Boolean = false
    private var statePaymentPlan: Boolean = false


    fun setFilterDateFrom(date: Date) {
        this.dateFrom = date
    }

    fun getFilterDateFrom(): Date {
        return this.dateFrom
    }

    fun setFilterDateTo(date: Date) {
        this.dateTo = date
    }

    fun getFilterDateto(): Date {
        return this.dateTo
    }

    fun setFilterMinAmount(amount: Float) {
        this.minAmount = amount
    }

    fun getFilterMinAmount(): Float {
        return this.minAmount
    }

    fun setFilterMaxAmount(amount: Float) {
        this.maxAmount = amount
    }

    fun getFilterMaxAmount(): Float {
        return this.maxAmount
    }

    fun setFilterPaid(state: Boolean) {
        this.statePaid = state
    }

    fun getFilterPaid(): Boolean {
        return this.statePaid
    }

    fun setFilterCancelled(state: Boolean) {
        this.stateCancelled = state
    }

    fun getFilterCancelled(): Boolean {
        return this.stateCancelled
    }

    fun setFilterFixedFee(state: Boolean) {
        this.stateFixedFee = state
    }

    fun getFilterFixedFee(): Boolean {
        return this.stateFixedFee
    }

    fun setFilterPendingPayment(state: Boolean) {
        this.statePendingPayment = state
    }

    fun getFilterPendingPayment(): Boolean {
        return this.statePendingPayment
    }

    fun setFilterPaymentPlan(state: Boolean) {
        this.statePaymentPlan = state
    }

    fun getFilterPaymentPlan(): Boolean {
        return this.statePaymentPlan
    }

    override fun toString(): String {
        return "MaxAmountInList: $maxAmountInList\n" +
                "DateFrom: $dateFrom\n" +
                "DateTo: $dateTo\n" +
                "MinAmount: $minAmount\n" +
                "Paid: $statePaid\n" +
                "Cancelled: $stateCancelled\n" +
                "FixedFee: $stateFixedFee\n" +
                "PendingPayment: $statePendingPayment\n" +
                "PaymentPlan: $statePaymentPlan\n"
    }
}


