package com.viewnext.proyectoviewnext.data.api

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockBehavior
import co.infinum.retromock.meta.MockResponse
import retrofit2.Response
import retrofit2.http.GET

interface InvoicesService {
    @GET("facturas")
    suspend fun getInvoices(): Response<InvoicesResult>

    @Mock
    @MockBehavior(durationDeviation = 100, durationMillis = 500)
    @MockResponse(
        body = "{\n" +
                "    \"numFacturas\": 8,\n" +
                "    \"facturas\": [\n" +
                "        {\n" +
                "            \"descEstado\": \"Anulada\",\n" +
                "            \"importeOrdenacion\": 100.56,\n" +
                "            \"fecha\": \"07/02/2019\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"descEstado\": \"Cancelada\",\n" +
                "            \"importeOrdenacion\": 25.14,\n" +
                "            \"fecha\": \"05/02/2019\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"descEstado\": \"Pendiente de pago\",\n" +
                "            \"importeOrdenacion\": 22.69,\n" +
                "            \"fecha\": \"08/01/2019\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"descEstado\": \"Plan de pago\",\n" +
                "            \"importeOrdenacion\": 12.84,\n" +
                "            \"fecha\": \"07/12/2018\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"descEstado\": \"Cuota fija\",\n" +
                "            \"importeOrdenacion\": 35.16,\n" +
                "            \"fecha\": \"16/11/2018\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"descEstado\": \"Pagada\",\n" +
                "            \"importeOrdenacion\": 18.27,\n" +
                "            \"fecha\": \"05/10/2018\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"descEstado\": \"Pendiente de pago\",\n" +
                "            \"importeOrdenacion\": 61.17,\n" +
                "            \"fecha\": \"05/09/2018\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"descEstado\": \"Pagada\",\n" +
                "            \"importeOrdenacion\": 37.18,\n" +
                "            \"fecha\": \"07/08/2018\"\n" +
                "        }\n" +
                "    ]\n" +
                "}"
    )
    @GET("facturas.json")
    suspend fun getInvoicesMock(): Response<InvoicesResult>
}

