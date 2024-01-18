package com.viewnext.proyectoviewnext.ui.invoices

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import co.infinum.retromock.Retromock
import com.viewnext.proyectoviewnext.R
import com.viewnext.proyectoviewnext.data.api.InvoicesResult
import com.viewnext.proyectoviewnext.data.api.InvoicesService
import com.viewnext.proyectoviewnext.data.models.Invoice
import com.viewnext.proyectoviewnext.databinding.FragmentInvoicesBinding
import com.viewnext.proyectoviewnext.services.Services
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


class InvoicesFragment : Fragment() {
    private lateinit var binding: FragmentInvoicesBinding
    private lateinit var adapter: InvoiceAdapter
    private var statusList: MutableList<String> = mutableListOf()

    private val _invoicesList: MutableLiveData<List<Invoice>> = MutableLiveData()
    private val invoicesList: LiveData<List<Invoice>>
        get() = _invoicesList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentInvoicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ivFilter = binding.invoicesFrTbToolbarInvoices.mainToolbarIvFilter
        ivFilter.setOnClickListener {
            findNavController().navigate(R.id.action_invoicesFragment_to_filterFragment)
        }
        val ivBack = binding.invoicesFrTbToolbarInvoices.mainToolbarIvBackIcon
        ivBack.setOnClickListener {
            val svc = Services()
            svc.showSnackBar(getString(R.string.not_available), view)
        }
        CoroutineScope(Dispatchers.Main).launch {
            initRecyclerView()
        }
        invoicesList.observe(viewLifecycleOwner) { updatedList ->
            updatedList?.let {
                adapter.updateList(it)
            }
        }
    }

    private suspend fun initRecyclerView() {
        CoroutineScope(Dispatchers.Main).launch {
            showProgressBar()
            try {
                searchInvoices()
                adapter = InvoiceAdapter(
                    invoicesList.value ?: emptyList(), requireContext()
                )
                binding.invoicesFrRvRecyclerInvoices.adapter = adapter
            } catch (e: Exception) {
                Log.e("tester", "${e.message}")
            }
        }
    }

    private suspend fun searchInvoices() {
        CoroutineScope(Dispatchers.IO).launch {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://viewnextandroid.wiremockapi.cloud/")
                .addConverterFactory(GsonConverterFactory.create()).build()
            val retromock = Retromock.Builder().retrofit(retrofit).build()
            val service = retromock.create(InvoicesService::class.java)

            try {
                // Seleccionar modo de carga de datos
                // loadApiData(service) // Uso de API
                loadRetromockData(service) // Uso de Retromock
            } catch (e: Exception) {
                Log.e("Error", "Error loading data")
            }
        }
    }

    private suspend fun loadRetromockData(service: InvoicesService) {
        val mockResponse = service.getInvoicesMock()
        if (mockResponse.isSuccessful) {
            hideProgressBar()
            loadStatus()
            val newInvoicesList = mapInvoicesList(mockResponse)
            _invoicesList.postValue(newInvoicesList)
        }
    }

    private suspend fun loadApiData(service: InvoicesService) {
        val response = service.getInvoices()
        if (response.isSuccessful) {
            hideProgressBar()
            loadStatus()
            val newInvoicesList = mapInvoicesList(response)
            _invoicesList.postValue(newInvoicesList)
        } else {
            Log.e("Error", "Error in API data loading")
        }
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
        // Check amount
        if (invoice.amount < filterSvc.getFilterMinAmount() || invoice.amount > filterSvc.getFilterMaxAmount()) {
            valid = false
        }
        // Check status
        if (!statusList.contains(invoice.status)) {
            valid = false
        }
        return valid
    }

    private fun loadStatus() {
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

    private suspend fun showProgressBar() {
        withContext(Dispatchers.Main) {
            binding.invoicesFrLlIsLoadingContainer.visibility = View.VISIBLE
            binding.invoicesFrRvRecyclerInvoices.visibility = View.GONE
        }
    }

    private suspend fun hideProgressBar() {
        withContext(Dispatchers.Main) {
            binding.invoicesFrLlIsLoadingContainer.visibility = View.GONE
            binding.invoicesFrRvRecyclerInvoices.visibility = View.VISIBLE
        }
    }
}