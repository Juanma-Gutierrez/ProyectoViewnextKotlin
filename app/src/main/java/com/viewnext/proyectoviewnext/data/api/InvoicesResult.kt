package com.viewnext.proyectoviewnext.data.api

import com.google.gson.annotations.SerializedName
import com.viewnext.proyectoviewnext.data.local.invoice.InvoiceEntity

/**
 * Data class representing the result of fetching a list of invoices from the API.
 *
 * @property numInvoices The total number of invoices in the result.
 * @property invoices The list of invoices in the result.
 */
data class InvoicesResult(
    @SerializedName("numFacturas") val numInvoices: Long,
    @SerializedName("facturas") val invoices: List<InvoiceResult>,
)

/**
 * Data class representing an individual invoice result from the API.
 */
data class InvoiceResult(
    @SerializedName("descEstado") val status: String,
    @SerializedName("importeOrdenacion") val amount: Double,
    @SerializedName("fecha") val date: String,
)
