package com.viewnext.proyectoviewnext.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.infinum.retromock.Retromock
import com.viewnext.proyectoviewnext.constants.Constants
import com.viewnext.proyectoviewnext.data.api.InvoiceResult
import com.viewnext.proyectoviewnext.data.api.InvoicesResult
import com.viewnext.proyectoviewnext.data.api.InvoicesService
import com.viewnext.proyectoviewnext.data.api.SelectorDataLoading
import com.viewnext.proyectoviewnext.data.models.Invoice
import com.viewnext.proyectoviewnext.utils.FilterService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

class InvoicesViewModel : ViewModel() {
    private var statusList: MutableList<String> = mutableListOf()
    private val _invoicesList: MutableLiveData<List<Invoice>> = MutableLiveData()
    val invoicesList: LiveData<List<Invoice>>
        get() = _invoicesList
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean>
        get() = _loadingState
    val selectorDL = SelectorDataLoading

    suspend fun searchInvoices() {
        CoroutineScope(Dispatchers.IO).launch {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
            val retromock = Retromock.Builder().retrofit(retrofit).build()
            val service = retromock.create(InvoicesService::class.java)

            try {
                if (selectorDL.loadFromAPI) {
                    loadApiData(service) // Api data loading
                } else {
                    loadRetromockData(service) // Retromock data loading
                }
                // Control of selectedMaxValue if the list changes
                val filterSvc = FilterService
                if (filterSvc.getSelectedAmount() > filterSvc.getMaxAmountInList())
                    filterSvc.setSelectedAmount(ceil(filterSvc.getMaxAmountInList()).toInt())
            } catch (e: Exception) {
                Log.e("Error", "Error loading data")
            }
        }
    }

    private suspend fun loadApiData(service: InvoicesService) {
        val response = service.getInvoices()
        if (response.isSuccessful) {
            println("Carga de datos de API")
            loadDataInRV(response)
        } else {
            Log.e("Error", "Error in API data loading")
        }
    }


    private suspend fun loadRetromockData(service: InvoicesService) {
        val mockResponse = service.getInvoicesMock()
        if (mockResponse.isSuccessful) {
            println("Carga de datos de Retromock")
            loadDataInRV(mockResponse)
        } else {
            Log.e("Error", "Error in retromock data loading")
        }
    }

    fun resetMaxAmountInList() {
        val filterSvc = FilterService
        filterSvc.setMaxAmountInList(0f)
    }

    private suspend fun loadDataInRV(response: Response<InvoicesResult>) {
        hideProgressBar()
        createArrayWithStatusSelected()
        findMaxAmount(response.body()?.invoices)
        val newInvoicesList = mapInvoicesList(response)
        _invoicesList.postValue(newInvoicesList)
    }

    fun findMaxAmount(invoices: List<InvoiceResult>?) {
        val filterSvc = FilterService
        val maxAmount = invoices?.maxBy { it.amount }?.amount?.toFloat()
        filterSvc.setMaxAmountInList(maxAmount!!)
        println(filterSvc)
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
        if (invoice.amount > filterSvc.getSelectedAmount()) {
            valid = false
        }
        // Check status, if all filters are false, skip this check
        if (!(!filterSvc.getStatusPaid() and
                    !filterSvc.getStatusCancelled() and
                    !filterSvc.getStatusFixedFee() and
                    !filterSvc.getStatusPendingPayment() and
                    !filterSvc.getStatusPaymentPlan())
        ) {
            if (!statusList.contains(invoice.status)) {
                valid = false
            }
        }
        return valid
    }

    private fun createArrayWithStatusSelected() {
        val filterSvc = FilterService
        if (filterSvc.getStatusPaid()) statusList.add("Pagada")
        if (filterSvc.getStatusCancelled()) statusList.add("Anulada")
        if (filterSvc.getStatusFixedFee()) statusList.add("Cuota fija")
        if (filterSvc.getStatusPendingPayment()) statusList.add("Pendiente de pago")
        if (filterSvc.getStatusPaymentPlan()) statusList.add("Plan de pago")
    }

    private suspend fun hideProgressBar() {
        withContext(Dispatchers.Main) {
            _loadingState.value = false
        }
    }

    fun setloadDataFromApi(status: Boolean) {
        selectorDL.loadFromAPI = status
    }
}