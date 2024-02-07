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
import com.viewnext.proyectoviewnext.data.api.retromock.ResourceBodyFactory
import com.viewnext.proyectoviewnext.data.local.invoice.InvoiceEntity
import com.viewnext.proyectoviewnext.data.local.repository.InvoicesDatabase
import com.viewnext.proyectoviewnext.data.models.Invoice
import com.viewnext.proyectoviewnext.utils.FilterService
import com.viewnext.proyectoviewnext.utils.Services
import com.viewnext.proyectoviewnext.utils.parseLocalDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.ZoneId
import java.util.Date
import kotlin.math.ceil


class InvoicesViewModel(application: Application) : AndroidViewModel(application) {
    private val _invoicesList: MutableLiveData<List<Invoice>> = MutableLiveData()
    val invoicesList: LiveData<List<Invoice>>
        get() = _invoicesList
    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean>
        get() = _loadingState
    private val selectorDL = SelectorDataLoading
    val room: InvoicesDatabase = Room.databaseBuilder(
        application.applicationContext,
        InvoicesDatabase::class.java,
        "invoices"
    ).build()
    val repositoryInvoices = room.invoiceDao()
    val filterSvc = FilterService

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun searchInvoices() {
        repositoryInvoices.getAllInvoices()
        CoroutineScope(Dispatchers.Main).launch {
            val retromock = Services.RetromockInstance.retromock
            val service = retromock.create(InvoicesService::class.java)
            try {
                if (selectorDL.loadFromAPI) {
                    loadApiData(service) // Api data loading
                } else {
                    loadRetromockData(service) // Retromock data loading
                }
                // Control of selectedMaxValue if the list changes
                if (filterSvc.getSelectedAmount() > filterSvc.getMaxAmountInList()) filterSvc.setSelectedAmount(
                    ceil(filterSvc.getMaxAmountInList()).toInt()
                )
            } catch (e: Exception) {
                Log.e("Error", "Error loading data ${e.message}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadApiData(service: InvoicesService) {
        viewModelScope.launch {
            try {
                val response = service.getInvoices()
                if (response.isSuccessful) {
                    val list = checkEmptyList(response.body()!!.invoices)
                    saveLocalRepository(list)
                } else {
                    Log.e("Error", "Error in API data loading")
                }
            } catch (e: Exception) {
                Log.e("Error", "Error in API endpoint $e")
            }
            loadDataInRV(loadRepositoryData())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun loadRetromockData(service: InvoicesService) {
        val mockResponse = service.getInvoicesMock()
        if (mockResponse.isSuccessful) {
            val list = checkEmptyList(mockResponse.body()!!.invoices)
            saveLocalRepository(list)
        } else {
            Log.e("Error", "Error in retromock data loading")
        }
        loadDataInRV(mockResponse.body()!!.invoices)
    }

    private fun checkEmptyList(invoices: List<InvoiceResult>): List<InvoiceResult> {
        if (invoices.isEmpty()) {
            return emptyList()
        }
        return invoices
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun loadRepositoryData(): List<InvoiceResult> {
        val listResult = repositoryInvoices.getAllInvoices().map { invoiceEntity ->
            InvoiceResult(
                status = invoiceEntity.status,
                amount = invoiceEntity.amount,
                date = invoiceEntity.date
            )
        }
        return listResult
    }

    private suspend fun saveLocalRepository(invoicesList: List<InvoiceResult>) {
        val invoicesEntityList = invoicesList.map { invoice ->
            InvoiceEntity(
                status = invoice.status, amount = invoice.amount, date = invoice.date
            )
        }
        repositoryInvoices.deleteAllInvoices()
        repositoryInvoices.createInvoiceList(invoicesEntityList)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadDataInRV(list: List<InvoiceResult>) {
        findMaxAmount(list)
        createArrayWithStatusSelected()
        val newInvoicesList = mapInvoicesList(list)
        _invoicesList.postValue(newInvoicesList)
        hideProgressBar()
    }

    fun findMaxAmount(invoices: List<InvoiceResult>?) {
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
        if (!inDate) return false
        // Check amount
        if (invoice.amount > filterSvc.getSelectedAmount()) return false
        // Check status, if all filters are false, skip this check
        if (filterSvc.statusList.size!=0){
            if (!filterSvc.statusList.contains(invoice.status)) return false
        }
        return valid
    }

    private fun createArrayWithStatusSelected() {
        filterSvc.statusList = ArrayList()  // Reset statusList
        if (filterSvc.getStatusPaid()) filterSvc.statusList.add(Constants.STATUS_PAID)
        if (filterSvc.getStatusCancelled()) filterSvc.statusList.add(Constants.STATUS_CANCELLED)
        if (filterSvc.getStatusFixedFee()) filterSvc.statusList.add(Constants.STATUS_FIXED_FEE)
        if (filterSvc.getStatusPendingPayment()) filterSvc.statusList.add(Constants.STATUS_PENDING_PAYMENT)
        if (filterSvc.getStatusPaymentPlan()) filterSvc.statusList.add(Constants.STATUS_PAYMENT_PLAN)
    }

    fun showProgressBar() {
        _loadingState.value = true
    }

    private fun hideProgressBar() {
        _loadingState.value = false
    }

    fun setLoadDataFromApi(status: Boolean) {
        selectorDL.loadFromAPI = status
    }
}