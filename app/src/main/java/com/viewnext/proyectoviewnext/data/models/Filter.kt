package com.viewnext.proyectoviewnext.data.models

import java.util.Date

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
    override fun toString(): String {
        return "\nDateFrom:$dateFrom | DateTo:$dateTo\n" +
                "SelectedAmount: $selectedAmount\n" +
                "Paid:$statusPaid | Cancelled:$statusCancelled | FFee:$statusFixedFee | PPayment:$statusPendingPayment | PPlan: $statusPaymentPlan\n"
    }
}