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
                .baseUrl("https://raw.githubusercontent.com/Juanma-Gutierrez/ProyectoViewnextKotlin/develop/app/src/main/java/com/viewnext/proyectoviewnext/data/localJson/")
                .addConverterFactory(GsonConverterFactory.create()).build()
            val retromock = Retromock.Builder().retrofit(retrofit).build()
            val service = retromock.create(InvoicesService::class.java)

            try {
                loadApiData(service) // Uso de API
                // loadRetromockData(service) // Uso de Retromock
            } catch (e: Exception) {
                Log.e("Error", "Error loading data")
            }
        }
    }

    private suspend fun loadRetromockData(service: InvoicesService) {
        hideProgressBar()
        val mockResponse = service.getInvoicesMock()
        val newInvoicesList = mapInvoicesList(mockResponse)
        _invoicesList.postValue(newInvoicesList)
    }

    private suspend fun loadApiData(service: InvoicesService) {
        val response = service.getInvoices()
        if (response.isSuccessful) {
            hideProgressBar()
            val newInvoicesList = mapInvoicesList(response)
            _invoicesList.postValue(newInvoicesList)
        } else {
            Log.e("Error", "Error in API data loading")
        }
    }

    private fun mapInvoicesList(response: Response<InvoicesResult>): List<Invoice> {
        return response.body()?.invoices?.map { invoiceResult ->
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            Invoice(
                LocalDate.parse(invoiceResult.date, formatter),
                invoiceResult.status,
                invoiceResult.amount.toFloat()
            )
        }!!
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

    /*
    private fun getRetrofit(): Retrofit {
        // return Retrofit.Builder().baseUrl("https://viewnextandroid.mocklab.io/")
        return
    }

     */
}