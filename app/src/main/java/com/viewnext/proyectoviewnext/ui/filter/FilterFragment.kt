package com.viewnext.proyectoviewnext.ui.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.viewnext.proyectoviewnext.R
import com.viewnext.proyectoviewnext.databinding.FragmentFilterBinding


class FilterFragment : Fragment() {
    private lateinit var binding: FragmentFilterBinding
    var filterSvc = FilterService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("-----------Entra en filtro-----------")
        println(filterSvc)
        loadFilters()
        super.onViewCreated(view, savedInstanceState)
        val ivClose = binding.filterFrTbToolbarFilter.filterToolbarIvClose
        ivClose.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_invoicesFragment)
        }
        val btApply = binding.filterFrBtButtonApply
        btApply.setOnClickListener {
            println("-----------Se pulsa Aplicar-----------")
            setFilters()
        }
        val btRemoveFilters = binding.filterFrBtButtonRemove
        btRemoveFilters.setOnClickListener {
            println("-----------Se pulsa eliminar filtros-----------")
            resetFilters()
        }
    }

    private fun loadFilters() {
        binding.filterFrTvTitleDateFrom.text = filterSvc.getFilterDateFrom().toString()
        binding.filterFrTvTitleDateTo.text = filterSvc.getFilterDateto().toString()
        binding.filterFrTvAmountMin.text = filterSvc.getFilterMinAmount().toString()
        binding.filterFrTvAmountMax.text = filterSvc.getFilterMaxAmount().toString()
        binding.filterFrCbPaid.isChecked = filterSvc.getFilterPaid()
        binding.filterFrCbCancelled.isChecked = filterSvc.getFilterCancelled()
        binding.filterFrCbFixedFee.isChecked = filterSvc.getFilterFixedFee()
        binding.filterFrCbPendingPayment.isChecked = filterSvc.getFilterPendingPayment()
        binding.filterFrCbPaymentPlan.isChecked = filterSvc.getFilterPaymentPlan()
    }

    private fun resetFilters() {
        println("-----------ResetFilters-----------")
        binding.filterFrTvTitleDateFrom.text = getString(R.string.title_filterDate)
        binding.filterFrTvTitleDateTo.text = getString(R.string.title_filterDate)
        binding.filterFrTvAmountMin.text = "0.0€"
        binding.filterFrTvAmountMax.text = "300.0€"
        binding.filterFrCbPaid.isChecked = false
        binding.filterFrCbCancelled.isChecked = false
        binding.filterFrCbFixedFee.isChecked = false
        binding.filterFrCbPendingPayment.isChecked = false
        binding.filterFrCbPaymentPlan.isChecked = false
        println(filterSvc)
    }

    private fun setFilters() {
        println("-----------SetFilters-----------")
        // TODO CAPTURAR DATOS DE FECHAS
        filterSvc.setFilterMinAmount(binding.filterFrSbSeekBarAmount.min.toFloat())
        filterSvc.setFilterMaxAmount(binding.filterFrSbSeekBarAmount.progress.toFloat())
        filterSvc.setFilterPaid(binding.filterFrCbPaid.isChecked)
        filterSvc.setFilterCancelled(binding.filterFrCbCancelled.isChecked)
        filterSvc.setFilterFixedFee(binding.filterFrCbFixedFee.isChecked)
        filterSvc.setFilterPendingPayment(binding.filterFrCbPendingPayment.isChecked)
        filterSvc.setFilterPaymentPlan(binding.filterFrCbPaymentPlan.isChecked)
        println(filterSvc)
    }
}