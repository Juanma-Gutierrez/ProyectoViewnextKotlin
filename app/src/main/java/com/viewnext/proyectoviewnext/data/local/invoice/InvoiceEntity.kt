package com.viewnext.proyectoviewnext.data.local.invoice

import androidx.room.Entity

/**
 * Entity class representing an invoice stored in the local database.
 *
 * @property date The date of the invoice.
 * @property status The status of the invoice.
 * @property amount The amount of the invoice.
 */
@Entity(tableName = "invoice", primaryKeys = ["date", "amount"])
data class InvoiceEntity(
    val date: String,
    val status: String,
    val amount: Double
)
