package com.viewnext.proyectoviewnext.data.models

import java.util.Date

class Filter(
    var dateFrom: Date? = null,
    var dateTo: Date? = null,
    var minAmount: Float = 0f,
    var maxAmount: Float = 0f,
    var statusPaid: Boolean = true,
    var statusCancelled: Boolean = true,
    var statusFixedFee: Boolean = true,
    var statusPendingPayment: Boolean = true,
    var statusPaymentPlan: Boolean = true
)