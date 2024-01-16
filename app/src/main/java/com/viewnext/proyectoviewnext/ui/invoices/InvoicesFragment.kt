package com.viewnext.proyectoviewnext.ui.invoices

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.viewnext.proyectoviewnext.R
import com.viewnext.proyectoviewnext.data.api.InvoicesResult
import com.viewnext.proyectoviewnext.data.api.InvoicesService
import com.viewnext.proyectoviewnext.data.models.Invoice
import com.viewnext.proyectoviewnext.databinding.FragmentInvoicesBinding
import com.viewnext.proyectoviewnext.services.Services
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date


class InvoicesFragment : Fragment() {
    private lateinit var binding: FragmentInvoicesBinding
    private lateinit var adapter: InvoiceAdapter

    private var invoicesList: MutableList<Invoice> = mutableListOf()
    // private lateinit var _invoicesList: MutableStateFlow<List<Invoice>>
    // private val invoicesList = _invoicesList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInvoicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ivFilter = binding.invoicesFrTbToolbarInvoices.mainToolbarIvFilter
        ivFilter.setOnClickListener {
            findNavController().navigate(R.id.action_invoicesFragment_to_filterFragment)
        }
        val ivBack = view.findViewById<ImageView>(R.id.mainToolbar_iv_backIcon)
        ivBack.setOnClickListener {
            val svc = Services()
            svc.showSnackBar(getString(R.string.not_available), view)
        }
        CoroutineScope(Dispatchers.Main).launch {
            initRecyclerView()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun initRecyclerView() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                searchInvoices()
                Log.d("tester", "InvoicesList ya capturado: " + invoicesList.toString())
                rellenaRecycler()
                adapter = InvoiceAdapter(invoicesList, findNavController(), requireContext())
                binding.invoicesFrRvRecyclerInvoices.adapter = adapter
            } catch (e: Exception) {
                Log.e("tester", "Error al obtener los datos: ${e.message}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun rellenaRecycler() {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        invoicesList.add(Invoice(LocalDate.parse("07/02/2019", formatter), "Pendiente", 30.25f))
        invoicesList.add(Invoice(LocalDate.parse("09/05/2019", formatter), "Cancelado", 10.15f))
        invoicesList.add(Invoice(LocalDate.parse("12/06/2019", formatter), "Pendiente", 8.00f))
        invoicesList.add(Invoice(LocalDate.parse("04/07/2019", formatter), "Pendiente", 4.3f))
        invoicesList.add(Invoice(LocalDate.parse("06/08/2019", formatter), "Finalizado", 21.70f))
        invoicesList.add(Invoice(LocalDate.parse("12/09/2019", formatter), "Pendiente", 30.25f))
        invoicesList.add(Invoice(LocalDate.parse("18/09/2019", formatter), "Pendiente", 14.34f))
        invoicesList.add(Invoice(LocalDate.parse("04/10/2019", formatter), "Pendiente", 19.12f))
        invoicesList.add(Invoice(LocalDate.parse("09/11/2019", formatter), "Finalizado", 21.7f))
        invoicesList.add(Invoice(LocalDate.parse("11/11/2019", formatter), "Cancelado", 7.5f))
        invoicesList.add(Invoice(LocalDate.parse("29/11/2019", formatter), "Pendiente", 6.23f))
        invoicesList.add(Invoice(LocalDate.parse("07/12/2019", formatter), "Pendiente", 5.90f))
        invoicesList.add(Invoice(LocalDate.parse("09/12/2019", formatter), "Pendiente", 23.20f))
    }


    private suspend fun searchInvoices() {
        CoroutineScope(Dispatchers.IO).launch {
            val call: Response<InvoicesResult> = getRetrofit()
                .create(InvoicesService::class.java)
                .getInvoices()
            if (call.isSuccessful) {
                invoicesList = call.body()!!.invoices.map { invoiceResult ->
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    Invoice(
                        LocalDate.parse(invoiceResult.date, formatter),
                        invoiceResult.status,
                        invoiceResult.amount.toFloat()
                    )
                }.toMutableList()
                Log.d("tester", "LLamada correcta: " + invoicesList.toString())
            } else {
                Log.d("tester", "Error en la carga de datos")
            }
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://viewnextandroid.wiremockapi.cloud/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}