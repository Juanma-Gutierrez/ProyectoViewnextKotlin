package com.viewnext.proyectoviewnext.ui.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.viewnext.proyectoviewnext.R
import com.viewnext.proyectoviewnext.data.models.Filter
import com.viewnext.proyectoviewnext.databinding.FragmentFilterBinding


class FilterFragment : Fragment() {
    private lateinit var binding: FragmentFilterBinding
    private lateinit var filterViewModel: FilterViewModel
    private var filterToApply: Filter = Filter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        filterViewModel = ViewModelProvider(this)[FilterViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFilters()
        val ivClose = binding.filterFrTbToolbarFilter.filterToolbarIvClose
        ivClose.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_invoicesFragment)
        }
        val btApply = binding.filterFrBtButtonApply
        btApply.setOnClickListener {
            setFilters()
            findNavController().navigate(R.id.action_filterFragment_to_invoicesFragment)
        }
        val btRemoveFilters = binding.filterFrBtButtonRemove
        btRemoveFilters.setOnClickListener {
            resetFilters()
        }
    }

    private fun loadFilters() {
        println("-----------LoadFilters-----------")
        binding.filterFrBtButtonFrom.text = filterViewModel.getDateFrom(this.requireContext())
        binding.filterFrBtButtonTo.text = filterViewModel.getDateTo(this.requireContext())
        binding.filterFrTvAmountMin.text = filterViewModel.getMinAmount()
        binding.filterFrTvAmountMax.text = filterViewModel.findMaxAmountProgressBar().toString()
        binding.filterFrTvSelectedAmount.text =
            getSelectedAmount(filterViewModel.getMinAmount(), filterViewModel.findMaxAmountProgressBar().toString())
        binding.filterFrCbPaid.isChecked = filterViewModel.getFilterPaid()
        binding.filterFrCbCancelled.isChecked = filterViewModel.getFilterCancelled()
        binding.filterFrCbFixedFee.isChecked = filterViewModel.getFilterFixedFee()
        binding.filterFrCbPendingPayment.isChecked = filterViewModel.getFilterPendingPayment()
        binding.filterFrCbPaymentPlan.isChecked = filterViewModel.getFilterPaymentPlan()
        println(filterViewModel.getFilter())
    }



    private fun getSelectedAmount(minAmount: String, maxAmount: String): String {
        return ("$minAmount € - $maxAmount €")
    }

    private fun setFilters() {
        println("-----------SetFilters-----------")
        filterToApply.dateFrom = null
        filterToApply.dateTo = null
        filterToApply.minAmount = binding.filterFrSbSeekBarAmount.min.toFloat()
        filterToApply.maxAmount = binding.filterFrSbSeekBarAmount.progress.toFloat()
        filterToApply.statusPaid = binding.filterFrCbPaid.isChecked
        filterToApply.statusCancelled = binding.filterFrCbCancelled.isChecked
        filterToApply.statusFixedFee = binding.filterFrCbFixedFee.isChecked
        filterToApply.statusPendingPayment = binding.filterFrCbPendingPayment.isChecked
        filterToApply.statusPaymentPlan = binding.filterFrCbPaymentPlan.isChecked
        filterViewModel.setFilters(filterToApply)
        println(filterViewModel.getFilter())
    }


    private fun resetFilters() {
        println("-----------ResetFilters-----------")
        filterViewModel.resetFilters()
        loadFilters()
        println(filterViewModel.getFilter())
    }
}