package com.viewnext.proyectoviewnext.data.models

import java.time.LocalDate
import java.util.Date

data class Invoice(
    val date: LocalDate,
    val status: String,
    val amount: Float
)
