package com.viewnext.proyectoviewnext.data.models

import java.util.Date

class Filter(
    var dateFrom: Date? = null,
    var dateTo: Date? = null,
    var selectedAmount: Int = 0,
    var statusPaid: Boolean = false,
    var statusCancelled: Boolean = false,
    var statusFixedFee: Boolean = false,
    var statusPendingPayment: Boolean = false,
    var statusPaymentPlan: Boolean = false
)