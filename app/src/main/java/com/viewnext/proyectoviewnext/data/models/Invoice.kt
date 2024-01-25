package com.viewnext.proyectoviewnext.data.models

import java.time.LocalDate

/**
 * Data class representing an invoice.
 */
data class Invoice(
    val date: LocalDate,
    val status: String,
    val amount: Float
)
