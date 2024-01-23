package com.viewnext.proyectoviewnext.ui.invoices

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.infinum.retromock.Retromock
import com.viewnext.proyectoviewnext.constants.Constants
import com.viewnext.proyectoviewnext.data.api.InvoicesResult
import com.viewnext.proyectoviewnext.data.api.InvoicesService
import com.viewnext.proyectoviewnext.data.models.Invoice
import com.viewnext.proyectoviewnext.ui.filter.FilterService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class InvoicesViewModel : ViewModel() {
    private var statusList: MutableList<String> = mutableListOf()
    private val _invoicesList: MutableLiveData<List<Invoice>> = MutableLiveData()
    val invoicesList: LiveData<List<Invoice>>
        get() = _invoicesList
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean>
        get() = _loadingState
    private var loadDataFromApi = false

    suspend fun searchInvoices() {
        CoroutineScope(Dispatchers.IO).launch {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
            val retromock = Retromock.Builder().retrofit(retrofit).build()
            val service = retromock.create(InvoicesService::class.java)

            try {
                if (loadDataFromApi) {
                    loadApiData(service) // Api data loading
                } else {
                    loadRetromockData(service) // Retromock data loading
                }
            } catch (e: Exception) {
                Log.e("Error", "Error loading data")
            }
        }
    }

    private suspend fun loadApiData(service: InvoicesService) {
        val response = service.getInvoices()
        if (response.isSuccessful) {
            loadDataInRV(response)
        } else {
            Log.e("Error", "Error in API data loading")
        }
    }

    private suspend fun loadRetromockData(service: InvoicesService) {
        val mockResponse = service.getInvoicesMock()
        if (mockResponse.isSuccessful) {
            loadDataInRV(mockResponse)
        } else {
            Log.e("Error", "Error in retromock data loading")
        }
    }


    private suspend fun loadDataInRV(response: Response<InvoicesResult>) {
        hideProgressBar()
        createArrayWithStatusSelected()
        findMaxAmount(response)
        val newInvoicesList = mapInvoicesList(response)
        _invoicesList.postValue(newInvoicesList)
    }

    fun findMaxAmount(response: Response<InvoicesResult>) {
        val filterSvc = FilterService
        val maxAmount = response.body()?.invoices!!.maxBy { it.amount }.amount.toFloat()
        filterSvc.setMaxAmountInList(maxAmount)
    }

    private fun mapInvoicesList(response: Response<InvoicesResult>): List<Invoice> {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val filteredList = response.body()?.invoices?.filter { invoice ->
            val invoiceToCheck = Invoice(
                LocalDate.parse(invoice.date, formatter), invoice.status, invoice.amount.toFloat()
            )
            invoiceInFilter(invoiceToCheck)
        }
        return filteredList?.map { invoice ->
            Invoice(
                LocalDate.parse(invoice.date, formatter),
                invoice.status,
                invoice.amount.toFloat()
            )
        }!!
    }

    private fun invoiceInFilter(invoice: Invoice): Boolean {
        var valid = true
        val filterSvc = FilterService
        //TODO Check fechas


        // Check amount
        if (invoice.amount > filterSvc.getFilterSelectedAmount()) {
            valid = false
        }
        // Check status, if all filters are false, skip this check
        if (!(!filterSvc.getFilterPaid() and
                    !filterSvc.getFilterCancelled() and
                    !filterSvc.getFilterFixedFee() and
                    !filterSvc.getFilterPendingPayment() and
                    !filterSvc.getFilterPaymentPlan())
        ) {
            if (!statusList.contains(invoice.status)) {
                valid = false
            }
        }
        return valid
    }

    private fun createArrayWithStatusSelected() {
        val filterSvc = FilterService
        if (filterSvc.getFilterPaid()) {
            statusList.add("Pagada")
        }
        if (filterSvc.getFilterCancelled()) {
            statusList.add("Anulada")
        }
        if (filterSvc.getFilterFixedFee()) {
            statusList.add("Cuota fija")
        }
        if (filterSvc.getFilterPendingPayment()) {
            statusList.add("Pendiente de pago")
        }
        if (filterSvc.getFilterPaymentPlan()) {
            statusList.add("Plan de pago")
        }
    }

    private suspend fun hideProgressBar() {
        withContext(Dispatchers.Main) {
            _loadingState.value = false
        }
    }

    fun setloadDataFromApi(status: Boolean) {
        this.loadDataFromApi = status
    }
}