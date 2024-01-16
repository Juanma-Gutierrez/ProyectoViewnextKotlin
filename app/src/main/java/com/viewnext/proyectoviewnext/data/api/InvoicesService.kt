package com.viewnext.proyectoviewnext.data.api

import retrofit2.Response
import retrofit2.http.GET

interface InvoicesService {
    @GET("facturas")
    suspend fun getInvoices(): Response<InvoicesResult>
}

