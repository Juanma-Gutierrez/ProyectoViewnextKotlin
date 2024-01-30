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
import androidx.room.Room
import com.viewnext.proyectoviewnext.R
import com.viewnext.proyectoviewnext.adapters.InvoiceAdapter
import com.viewnext.proyectoviewnext.data.api.SelectorDataLoading
import com.viewnext.proyectoviewnext.data.local.repository.InvoicesDatabase
import com.viewnext.proyectoviewnext.databinding.FragmentInvoicesBinding
import com.viewnext.proyectoviewnext.utils.Services
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
            // Shows or hides progressBar and hides or shows recyclerView
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
        // Load services
        val svc = Services()
        // Button back behavior
        val ivBack = binding.invoicesFrTbToolbarInvoices.mainToolbarIvBackIcon
        ivBack.setOnClickListener {
            svc.showSnackBar(
                getString(R.string.not_available), view, R.color.md_theme_light_secondary
            )
        }
        // Instance of SelectorDataLoading
        val selector = SelectorDataLoading
        // Switch API or Retromock data loading
        val swDataLoading = binding.invoicesFrTbToolbarInvoices.mainToolbarSwLoadFromApi
        swDataLoading.isChecked = selector.loadFromAPI
        swDataLoading.setOnCheckedChangeListener { buttonView, isChecked ->
            invoicesViewModel.setloadDataFromApi(swDataLoading.isChecked)
            // invoicesViewModel.resetMaxAmountInList()
            if (swDataLoading.isChecked) {
                loadDataFromNewSource(getString(R.string.load_data_from_api), view, svc)
            } else {
                loadDataFromNewSource(getString(R.string.load_data_from_retromock), view, svc)
            }
            selector.loadFromAPI = swDataLoading.isChecked
            loadDataInRV()
        }
        // Filter button behavior
        val ivFilter = binding.invoicesFrTbToolbarInvoices.mainToolbarIvFilter
        ivFilter.setOnClickListener {
            findNavController().navigate(R.id.action_invoicesFragment_to_filterFragment)
        }
        // Load data in RecyclerView
        loadDataInRV()

        invoicesViewModel.invoicesList.observe(viewLifecycleOwner) { newList ->
            // Show or hide recyclerView and warning if the invoices list is empty
            if (newList.isEmpty()) showWarningMessage() else hideWarningMessage()
            // Updates the list of invoices
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
        svc.showSnackBar(message, view, R.color.md_theme_light_primary)
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
        binding.invoicesFrInIsLoading.invoicesFrLlIsLoadingContainer.visibility = View.VISIBLE
        binding.invoicesFrRvRecyclerInvoices.visibility = View.GONE
    }

    /**
     * Hides the progress bar and shows the RecyclerView.
     */
    private fun hideProgressBar() {
        binding.invoicesFrInIsLoading.invoicesFrLlIsLoadingContainer.visibility = View.GONE
        binding.invoicesFrRvRecyclerInvoices.visibility = View.VISIBLE
    }

    /**
     * Shows the warning message and hides the RecyclerView.
     */
    private fun showWarningMessage() {
        binding.invoicesFrInWarning.invoicesFrLLWarningMessageContainer.visibility = View.VISIBLE
        binding.invoicesFrInWarning.messageWarningTvMessageWarning.text =
            getString(R.string.none_invoice_found)
        binding.invoicesFrRvRecyclerInvoices.visibility = View.GONE
    }

    /**
     * Shows the RecyclerView and hides the warning message.
     */
    private fun hideWarningMessage() {
        binding.invoicesFrInWarning.invoicesFrLLWarningMessageContainer.visibility = View.GONE
        binding.invoicesFrRvRecyclerInvoices.visibility = View.VISIBLE
    }
}
