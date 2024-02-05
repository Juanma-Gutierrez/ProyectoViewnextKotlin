package com.viewnext.proyectoviewnext.data.api

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockBehavior
import co.infinum.retromock.meta.MockCircular
import co.infinum.retromock.meta.MockResponse
import co.infinum.retromock.meta.MockResponses
import retrofit2.Response
import retrofit2.http.GET

/**
 * Retrofit service interface for fetching invoices from the API.
 */
interface InvoicesService {
    /**
     * Fetches a list of invoices from the API.
     *
     * @return A [Response] containing the result of the API call.
     */
    @GET("facturas")
    suspend fun getInvoices(): Response<InvoicesResult>

    /**
     * Mock implementation of fetching invoices for testing purposes.
     *
     * @return A [Response] containing mock data for testing.
     */
    @Mock
    @MockResponses(
        MockResponse(body = "mock_invoices_long.json"),
        MockResponse(body = "mock_invoices_original.json"),
    )
    @MockCircular
    @MockBehavior(durationDeviation = 10, durationMillis = 20)
    @GET("/")
    suspend fun getInvoicesMock(): Response<InvoicesResult>
}

