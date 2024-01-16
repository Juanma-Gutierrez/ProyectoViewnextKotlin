package com.viewnext.proyectoviewnext.ui.invoices

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Botón filtro para que abra el fragment Filter
        val ivFilter = view.findViewById<ImageView>(R.id.mainToolbar_iv_filter)
        ivFilter.setOnClickListener {
            findNavController().navigate(R.id.action_invoicesFragment_to_filterFragment)
        }
        // Botón regreso
        val ivBack = view.findViewById<ImageView>(R.id.mainToolbar_iv_backIcon)
        ivBack.setOnClickListener {
            val svc = Services()
            svc.showSnackBar(getString(R.string.not_available), view)
        }

        CoroutineScope(Dispatchers.Main).launch {
            initRecyclerView()
        }
        /*
                // Botón navegar para que muestre fragment Warning
                val navigateImageView = view.findViewById<MaterialButton>(R.id.fragmentMain_bt_navigate)
                navigateImageView.setOnClickListener {
                    findNavController().navigate(R.id.action_mainFragment_to_warningFragment)
                }

         */
    }

    private suspend fun initRecyclerView() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                searchInvoices()
                Log.d("tester", "InvoicesList ya capturado: " + invoicesList.toString())
                invoicesList.add(Invoice(Date("2022/03/21"), "Pendiente", 30.25f))
                invoicesList.add(Invoice(Date("2023/12/10"), "Pagado", 14.10f))
                invoicesList.add(Invoice(Date("2024/04/05"), "Pendiente", 5.75f))
                adapter = InvoiceAdapter(invoicesList)
                binding.recyclerView.adapter = adapter
            } catch (e: Exception) {
                Log.e("tester", "Error al obtener los datos: ${e.message}")
            }
        }
    }


    private suspend fun searchInvoices() {
        CoroutineScope(Dispatchers.IO).launch {
            val call: Response<InvoicesResult> = getRetrofit()
                .create(InvoicesService::class.java)
                .getInvoices()
            if (call.isSuccessful) {
                invoicesList = call.body()!!.invoices.map { invoiceResult ->
                    Invoice(
                        Date(invoiceResult.date),
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