package com.viewnext.proyectoviewnext.ui.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.viewnext.proyectoviewnext.R
import com.viewnext.proyectoviewnext.data.api.SelectorDataLoading
import com.viewnext.proyectoviewnext.databinding.FragmentInvoicesBinding
import com.viewnext.proyectoviewnext.utils.Services
import com.viewnext.proyectoviewnext.adapters.InvoiceAdapter
import com.viewnext.proyectoviewnext.viewmodels.InvoicesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass that displays a list of invoices.
 */
class InvoicesFragment : Fragment() {
    private lateinit var binding: FragmentInvoicesBinding
    private lateinit var adapter: InvoiceAdapter
    private lateinit var invoicesViewModel: InvoicesViewModel

    /**
     * Creates the view for the InvoicesFragment, inflating the layout and initializing the necessary components.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The root view of the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentInvoicesBinding.inflate(inflater, container, false)
        invoicesViewModel = ViewModelProvider(this)[InvoicesViewModel::class.java]
        invoicesViewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showProgressBar() else hideProgressBar()
        }
        return binding.root
    }

    /**
     * Called when the view has been created, setting up UI components, click listeners, and observing data changes.
     *
     * @param view The root view of the fragment.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ivBack = binding.invoicesFrTbToolbarInvoices.mainToolbarIvBackIcon
        val svc = Services()
        ivBack.setOnClickListener {
            svc.showSnackBar(getString(R.string.not_available), view)
        }
        val swDataLoading = binding.invoicesFrTbToolbarInvoices.mainToolbarSwLoadFromApi
        val selector = SelectorDataLoading
        swDataLoading.isChecked = selector.loadFromAPI
        swDataLoading.setOnCheckedChangeListener { buttonView, isChecked ->
            invoicesViewModel.setloadDataFromApi(swDataLoading.isChecked)
            invoicesViewModel.resetMaxAmountInList()
            if (swDataLoading.isChecked) {
                loadDataFromNewSource("Activada carga de datos desde API", view, svc)
            } else {
                loadDataFromNewSource("Actiavda carga de datos desde Retromock", view, svc)
            }
            selector.loadFromAPI = swDataLoading.isChecked
            loadDataInRV()
        }
        val ivFilter = binding.invoicesFrTbToolbarInvoices.mainToolbarIvFilter
        ivFilter.setOnClickListener {
            findNavController().navigate(R.id.action_invoicesFragment_to_filterFragment)
        }
        loadDataInRV()
        invoicesViewModel.invoicesList.observe(viewLifecycleOwner) { newList ->
            adapter.updateList(newList)
        }
    }

    /**
     * Shows a snack bar with the given message and displays a progress bar.
     *
     * @param message The message to be displayed in the snack bar.
     * @param view The view to which the snack bar is attached.
     * @param svc An instance of the Services class for utility functions.
     */
    private fun loadDataFromNewSource(message: String, view: View, svc: Services) {
        svc.showSnackBar(message, view)
        showProgressBar()
    }

    /**
     * Loads data into the RecyclerView by calling the [InvoicesViewModel.searchInvoices] method,
     * initializing the adapter, and setting it on the RecyclerView.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadDataInRV() {
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
    }

    /**
     * Shows the progress bar and hides the RecyclerView.
     */
    private fun showProgressBar() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.invoicesFrLlIsLoadingContainer.visibility = View.VISIBLE
            binding.invoicesFrRvRecyclerInvoices.visibility = View.GONE
        }
    }

    /**
     * Hides the progress bar and shows the RecyclerView.
     */
    private fun hideProgressBar() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.invoicesFrLlIsLoadingContainer.visibility = View.GONE
            binding.invoicesFrRvRecyclerInvoices.visibility = View.VISIBLE
        }
    }
}