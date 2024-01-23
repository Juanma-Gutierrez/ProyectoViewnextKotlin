package com.viewnext.proyectoviewnext.ui.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
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
        val sbAmount = binding.filterFrSbSeekBarAmount
        sbAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                changeValueOnSelectedSeekBar(sbAmount.progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun changeValueOnSelectedSeekBar(progress: Int) {
        val filterSvc = FilterService
        filterSvc.setFilterSelectedAmount(progress)
        binding.filterFrTvSelectedAmount.text = getSelectedAmount(progress.toString())
    }

    private fun loadFilters() {
        binding.filterFrBtButtonFrom.text = filterViewModel.getDateFrom(this.requireContext())
        binding.filterFrBtButtonTo.text = filterViewModel.getDateTo(this.requireContext())
        val maxAmountProgressBar = filterViewModel.findMaxAmountProgressBar()
        binding.filterFrSbSeekBarAmount.max = maxAmountProgressBar.toInt()
        binding.filterFrSbSeekBarAmount.progress = filterViewModel.getSelectedAmount().toInt()
        binding.filterFrTvAmountMax.text = "${maxAmountProgressBar.toInt()} €"
        binding.filterFrTvSelectedAmount.text =
            getSelectedAmount(
                if (filterViewModel.getSelectedAmount().toInt() == Integer.MAX_VALUE) {
                    maxAmountProgressBar.toInt().toString()
                } else {
                    filterViewModel.getSelectedAmount().toString()
                }
            )
        binding.filterFrCbPaid.isChecked = filterViewModel.getFilterPaid()
        binding.filterFrCbCancelled.isChecked = filterViewModel.getFilterCancelled()
        binding.filterFrCbFixedFee.isChecked = filterViewModel.getFilterFixedFee()
        binding.filterFrCbPendingPayment.isChecked = filterViewModel.getFilterPendingPayment()
        binding.filterFrCbPaymentPlan.isChecked = filterViewModel.getFilterPaymentPlan()
    }


    private fun getSelectedAmount(maxAmount: String): String {
        return ("0 € - $maxAmount €")
    }

    private fun setFilters() {
        filterToApply.dateFrom = null
        filterToApply.dateTo = null
        filterToApply.maxAmount = binding.filterFrSbSeekBarAmount.max.toFloat()
        filterToApply.selectedAmount = binding.filterFrSbSeekBarAmount.progress
        filterToApply.statusPaid = binding.filterFrCbPaid.isChecked
        filterToApply.statusCancelled = binding.filterFrCbCancelled.isChecked
        filterToApply.statusFixedFee = binding.filterFrCbFixedFee.isChecked
        filterToApply.statusPendingPayment = binding.filterFrCbPendingPayment.isChecked
        filterToApply.statusPaymentPlan = binding.filterFrCbPaymentPlan.isChecked
        filterViewModel.setFilters(filterToApply)
    }


    private fun resetFilters() {
        filterViewModel.resetFilters()
        loadFilters()
    }
}