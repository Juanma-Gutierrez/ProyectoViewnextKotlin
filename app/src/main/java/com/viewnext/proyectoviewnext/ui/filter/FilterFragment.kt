package com.viewnext.proyectoviewnext.ui.filter

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.viewnext.proyectoviewnext.R
import com.viewnext.proyectoviewnext.data.models.Filter
import com.viewnext.proyectoviewnext.databinding.FragmentFilterBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.ceil


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
    ): View {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        filterViewModel = ViewModelProvider(this)[FilterViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ivClose = binding.filterFrTbToolbarFilter.filterToolbarIvClose
        val btDateFrom = binding.filterFrBtButtonFrom
        val btDateTo = binding.filterFrBtButtonTo
        val btApply = binding.filterFrBtButtonApply
        val btRemoveFilters = binding.filterFrBtButtonRemove
        val sbAmount = binding.filterFrSbSeekBarAmount

        println("antes de hacer la carga del filtro" + filterViewModel.getFilter())
        loadFilters()
        ivClose.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_invoicesFragment)
        }
        btDateFrom.setOnClickListener {
            val date = showDatePickerDialog(btDateFrom)
            println("LO QUE HA DEVUELTO Y TENEMOS EN DATE" + date.time)
            filterToApply.dateFrom = date.time
        }
        btDateTo.setOnClickListener {
            val date = showDatePickerDialog(btDateTo)
            println("LO QUE HA DEVUELTO Y TENEMOS EN DATE" + date.time)
            filterToApply.dateTo = date.time
        }
        btApply.setOnClickListener {
            setFilters()
            findNavController().navigate(R.id.action_filterFragment_to_invoicesFragment)
        }
        btRemoveFilters.setOnClickListener {
            resetFilters()
        }
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

    private fun showDatePickerDialog(bt: MaterialButton): Calendar {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            activity as Context, { _, _year, _month, _day ->
                calendar.set(_year, _month, _day)
                bt.text = dateToString(calendar.time)
                println("DENTRO DEL DATEPICKERDIALOG" + calendar.time)
            }, year, month, day
        )
        datePickerDialog.show()
        println("ANTES DE SALIR DEL DATEPICKERDIALOG" + calendar.time)
        return calendar
    }

    private fun dateToString(date: Date): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    private fun changeValueOnSelectedSeekBar(progress: Int) {
        val filterSvc = FilterService
        filterSvc.setSelectedAmount(progress)
        binding.filterFrTvSelectedAmount.text = getSelectedAmount(progress.toString())
    }

    private fun loadFilters() {
        binding.filterFrBtButtonFrom.text = filterViewModel.getDateFrom(this.requireContext())
        binding.filterFrBtButtonTo.text = filterViewModel.getDateTo(this.requireContext())
        val maxAmountProgressBar = ceil(filterViewModel.getMaxAmountInList()).toInt()
        binding.filterFrSbSeekBarAmount.max = maxAmountProgressBar
        binding.filterFrSbSeekBarAmount.progress = filterViewModel.getSelectedAmount()
        binding.filterFrTvAmountMax.text = "${maxAmountProgressBar} €"
        binding.filterFrTvSelectedAmount.text =
            getSelectedAmount(
                if (filterViewModel.getSelectedAmount() == Integer.MAX_VALUE) {
                    maxAmountProgressBar.toString()
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
        filterToApply.dateFrom = stringToDate(binding.filterFrBtButtonFrom.text)
        filterToApply.dateTo = stringToDate(binding.filterFrBtButtonTo.text)
        // filterToApply.maxAmount = binding.filterFrSbSeekBarAmount.max.toFloat()
        filterToApply.selectedAmount = binding.filterFrSbSeekBarAmount.progress
        filterToApply.statusPaid = binding.filterFrCbPaid.isChecked
        filterToApply.statusCancelled = binding.filterFrCbCancelled.isChecked
        filterToApply.statusFixedFee = binding.filterFrCbFixedFee.isChecked
        filterToApply.statusPendingPayment = binding.filterFrCbPendingPayment.isChecked
        filterToApply.statusPaymentPlan = binding.filterFrCbPaymentPlan.isChecked
        filterViewModel.setFilters(filterToApply)
    }

    private fun stringToDate(dateString: CharSequence): Date {
        var date = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            date = dateFormat.parse(dateString.toString())!!
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return date
    }


    private fun resetFilters() {
        filterViewModel.resetFilters()
        loadFilters()
    }
}