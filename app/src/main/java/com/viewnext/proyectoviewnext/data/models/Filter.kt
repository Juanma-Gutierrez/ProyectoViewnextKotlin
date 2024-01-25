package com.viewnext.proyectoviewnext.data.models

import java.util.Date

/**
 * Data class representing a filter for querying invoices.
 */
class Filter(
    var dateFrom: Date? = null,
    var dateTo: Date? = null,
    var selectedAmount: Int = Int.MAX_VALUE,
    var statusPaid: Boolean = false,
    var statusCancelled: Boolean = false,
    var statusFixedFee: Boolean = false,
    var statusPendingPayment: Boolean = false,
    var statusPaymentPlan: Boolean = false
) {
    /**
     * Resets all filter parameters to their default values.
     */
    fun reset() {
        dateFrom = null
        dateTo = null
        selectedAmount = Int.MAX_VALUE
        statusPaid = false
        statusCancelled = false
        statusFixedFee = false
        statusPendingPayment = false
        statusPaymentPlan = false
    }

    /**
     * Returns a string representation of the filter parameters.
     *
     * @return A string representation of the filter.
     */
    override fun toString(): String {
        return "\nDateFrom:$dateFrom | DateTo:$dateTo\n" +
                "SelectedAmount: $selectedAmount\n" +
                "Paid:$statusPaid | Cancelled:$statusCancelled | FFee:$statusFixedFee | PPayment:$statusPendingPayment | PPlan: $statusPaymentPlan\n"
    }
}