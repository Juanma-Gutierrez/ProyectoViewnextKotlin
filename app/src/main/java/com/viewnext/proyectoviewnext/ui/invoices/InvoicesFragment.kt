package com.viewnext.proyectoviewnext.ui.invoices

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.viewnext.proyectoviewnext.R
import com.viewnext.proyectoviewnext.databinding.FragmentInvoicesBinding
import com.viewnext.proyectoviewnext.services.Services
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class InvoicesFragment : Fragment() {
    private lateinit var binding: FragmentInvoicesBinding
    private lateinit var adapter: InvoiceAdapter
    private lateinit var invoicesViewModel: InvoicesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentInvoicesBinding.inflate(inflater, container, false)
        invoicesViewModel = ViewModelProvider(this)[InvoicesViewModel::class.java]
        invoicesViewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                showProgressBar()
            } else {
                hideProgressBar()
            }
        }
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
            try {
                invoicesViewModel.searchInvoices()
                adapter = InvoiceAdapter(
                    invoicesViewModel.invoicesList.value ?: emptyList(), requireContext()
                )
                binding.invoicesFrRvRecyclerInvoices.adapter = adapter
            } catch (e: Exception) {
                Log.e("tester", "${e.message}")
            }
        }

        invoicesViewModel.invoicesList.observe(viewLifecycleOwner) { updatedList ->
            updatedList?.let {
                adapter.updateList(it)
            }
        }
    }

    private fun showProgressBar() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.invoicesFrLlIsLoadingContainer.visibility = View.VISIBLE
            binding.invoicesFrRvRecyclerInvoices.visibility = View.GONE
        }
    }

    private fun hideProgressBar() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.invoicesFrLlIsLoadingContainer.visibility = View.GONE
            binding.invoicesFrRvRecyclerInvoices.visibility = View.VISIBLE
        }
    }
}