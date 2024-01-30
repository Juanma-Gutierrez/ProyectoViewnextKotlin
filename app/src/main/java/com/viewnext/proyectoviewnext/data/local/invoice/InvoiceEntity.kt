package com.viewnext.proyectoviewnext.data.local.invoice

import androidx.room.Entity

@Entity(tableName = "invoice", primaryKeys = ["date", "amount"])
data class InvoiceEntity(
    val date: String,
    val status: String,
    val amount: Double
)
