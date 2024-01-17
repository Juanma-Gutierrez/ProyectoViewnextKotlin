package com.viewnext.proyectoviewnext.data.api

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockResponse
import retrofit2.Response
import retrofit2.http.GET

interface InvoicesService {
    //@GET("facturas")
    @GET("facturas.json")
    suspend fun getInvoices(): Response<InvoicesResult>

    @Mock
    @MockResponse(
        body = "{\n" +
                "  \"numFacturas\": 8,\n" +
                "  \"facturas\": [\n" +
                "    {\n" +
                "      \"descEstado\": \"Pendiente de pago\",\n" +
                "      \"importeOrdenacion\": 1.56,\n" +
                "      \"fecha\": \"07/02/2019\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"descEstado\": \"Pagada\",\n" +
                "      \"importeOrdenacion\": 37.18,\n" +
                "      \"fecha\": \"07/08/2018\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"
    )
    @GET("facturas.json")
    suspend fun getInvoicesMock(): Response<InvoicesResult>
}

