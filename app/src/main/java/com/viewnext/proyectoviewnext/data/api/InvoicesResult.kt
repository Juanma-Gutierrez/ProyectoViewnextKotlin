package com.viewnext.proyectoviewnext.data.api

import com.google.gson.annotations.SerializedName
import com.viewnext.proyectoviewnext.data.models.Invoice

data class InvoicesResult(
    @SerializedName("numFacturas") val numInvoices: Long,
    @SerializedName("facturas") val invoices: List<InvoiceResult>,
)

data class InvoiceResult(
    @SerializedName("descEstado") val status: String,
    @SerializedName("importeOrdenacion") val amount: Double,
    @SerializedName("fecha") val date: String,
)
