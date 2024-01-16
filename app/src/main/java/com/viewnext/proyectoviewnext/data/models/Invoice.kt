package com.viewnext.proyectoviewnext.data.models

import java.util.Date

data class Invoice(
    val date: Date,
    val status: String,
    val amount: Float
)
