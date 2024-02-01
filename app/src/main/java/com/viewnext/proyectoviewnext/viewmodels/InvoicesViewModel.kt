package com.viewnext.proyectoviewnext.viewmodels

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import co.infinum.retromock.Retromock
import com.viewnext.proyectoviewnext.constants.Constants
import com.viewnext.proyectoviewnext.data.api.InvoiceResult
import com.viewnext.proyectoviewnext.data.api.InvoicesService
import com.viewnext.proyectoviewnext.data.api.SelectorDataLoading
import com.viewnext.proyectoviewnext.data.local.invoice.InvoiceEntity
import com.viewnext.proyectoviewnext.data.local.repository.InvoicesDatabase
import com.viewnext.proyectoviewnext.data.models.Invoice
import com.viewnext.proyectoviewnext.utils.FilterService
import com.viewnext.proyectoviewnext.utils.parseLocalDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.ZoneId
import java.util.Date
import kotlin.math.ceil

class InvoicesViewModel(application: Application) : AndroidViewModel(application) {
    private var statusList: MutableList<String> = mutableListOf()
    private val _invoicesList: MutableLiveData<List<Invoice>> = MutableLiveData()
    val invoicesList: LiveData<List<Invoice>>
        get() = _invoicesList
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean>
        get() = _loadingState
    private val selectorDL = SelectorDataLoading

    val room: InvoicesDatabase = Room
        .databaseBuilder(application.applicationContext, InvoicesDatabase::class.java, "invoices")
        .build()
    val repositoryInvoices = room.invoiceDao()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun searchInvoices() {
        repositoryInvoices.getAllInvoices()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadApiData(service: InvoicesService) {
        println("Entra en API")
        viewModelScope.launch {
            var list: List<InvoiceResult> = emptyList()
            try {
                val response = service.getInvoices()
                println("Respuesta: ${response.body()}")
                if (response.isSuccessful) {
                    println("Entra en API OK")
                    list = response.body()!!.invoices
                    saveLocalRepository(list)
                    loadDataInRV(list)
                } else {
                    println("Entra en error API")
                    loadDataInRV(loadRepositoryData())
                    Log.e("Error", "Error in API data loading")
                }
            } catch (e: Exception) {
                loadDataInRV(loadRepositoryData())
                Log.e("Error", "Error in API endpoint $e")
            }
        }
    }

    private suspend fun saveLocalRepository(invoicesList: List<InvoiceResult>) {
        println("Entra en error saveLocalRepository")
        val invoicesEntityList = invoicesList.map { invoice ->
            InvoiceEntity(
                status = invoice.status,
                amount = invoice.amount,
                date = invoice.date
            )
        }
        println("Lista actualizada: $invoicesEntityList")
        repositoryInvoices.deleteAllInvoices()
        repositoryInvoices.createInvoiceList(invoicesEntityList)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun loadRetromockData(service: InvoicesService) {
        println("Entra en RETROMOCK")
        val mockResponse = service.getInvoicesMock()
        if (mockResponse.isSuccessful) {
            println("Entra en RETROMOCK OK")
            saveLocalRepository(mockResponse.body()!!.invoices)
            loadDataInRV(mockResponse.body()!!.invoices)
        } else {
            println("Entra en error RETROMOCK")
            loadDataInRV(loadRepositoryData())
            Log.e("Error", "Error in retromock data loading")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun loadRepositoryData(): List<InvoiceResult> {
        println("Entra en LOADREPOSITORY")
        val listResult = repositoryInvoices.getAllInvoices().map { invoiceEntity ->
            InvoiceResult(
                status = invoiceEntity.status,
                amount = invoiceEntity.amount,
                date = invoiceEntity.date
            )
        }
        println(listResult)
        return listResult
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun loadDataInRV(list: List<InvoiceResult>) {
        println("Entra en LOADDATAINRV")
        findMaxAmount(list)
        hideProgressBar()
        createArrayWithStatusSelected()
        val newInvoicesList = mapInvoicesList(list)
        _invoicesList.postValue(newInvoicesList)
    }

    fun findMaxAmount(invoices: List<InvoiceResult>?) {
        val filterSvc = FilterService
        val maxAmount = invoices?.maxBy { it.amount }?.amount?.toFloat()
        filterSvc.setMaxAmountInList(maxAmount!!)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mapInvoicesList(list: List<InvoiceResult>): List<Invoice> {
        val filteredList = list.filter { invoice ->
            val invoiceToCheck = Invoice(
                parseLocalDate(invoice.date), invoice.status, invoice.amount.toFloat()
            )
            // Checks if the invoice is available with the filter applied
            invoiceInFilter(invoiceToCheck)
        }
        return filteredList.map { invoice ->
            Invoice(
                parseLocalDate(invoice.date), invoice.status, invoice.amount.toFloat()
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun invoiceInFilter(invoice: Invoice): Boolean {
        var valid = true
        val filterSvc = FilterService
        // Check dates
        var inDate = true
        val invoiceDateAsDate =
            Date.from(invoice.date.atStartOfDay(ZoneId.systemDefault()).toInstant())
        if (filterSvc.getDateFrom() != null) {
            if (invoiceDateAsDate < filterSvc.getDateFrom()) inDate = false
        }
        if (filterSvc.getDateTo() != null) {
            if (invoiceDateAsDate > filterSvc.getDateTo()) inDate = false
        }
        if (!inDate) valid = false
        // Check amount
        if (invoice.amount > filterSvc.getSelectedAmount()) valid = false
        // Check status, if all filters are false, skip this check
        if (!(!filterSvc.getStatusPaid() and !filterSvc.getStatusCancelled() and !filterSvc.getStatusFixedFee() and !filterSvc.getStatusPendingPayment() and !filterSvc.getStatusPaymentPlan())) {
            if (!statusList.contains(invoice.status)) valid = false
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