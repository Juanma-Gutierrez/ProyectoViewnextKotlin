package com.viewnext.proyectoviewnext.ui.fragments

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
import com.viewnext.proyectoviewnext.viewmodels.FilterViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.ceil

/**
 * Fragment for applying filters to the list of invoices.
 */
class FilterFragment : Fragment() {
    private lateinit var binding: FragmentFilterBinding
    private lateinit var filterViewModel: FilterViewModel
    private var filterToApply: Filter = Filter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * Called when the fragment is creating its user interface.
     *
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     * saved state as given here.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        filterViewModel = ViewModelProvider(this)[FilterViewModel::class.java]
        return binding.root
    }

    /**
     * Called when the fragment's activity has been created and this fragment's view hierarchy
     * instantiated.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     * this is the state.
     */
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
        sbAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                changeValueOnSelectedSeekBar(sbAmount.progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        btApply.setOnClickListener {
            setFilters()
            findNavController().navigate(R.id.action_filterFragment_to_invoicesFragment)
        }
        btRemoveFilters.setOnClickListener {
            resetFilters()
        }
    }

    /**
     * Displays a DatePickerDialog and sets the selected date to the specified MaterialButton.
     *
     * @param bt The MaterialButton to which the selected date will be set.
     * @return A [Calendar] object representing the selected date.
     */
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

    /**
     * Converts a [Date] object to a formatted string representation.
     *
     * @param date The date to be converted.
     * @return A string representing the formatted date.
     */
    private fun dateToString(date: Date): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    /**
     * Updates the selected amount in the filter and updates the corresponding TextView.
     *
     * @param progress The progress value from the SeekBar.
     */
    private fun changeValueOnSelectedSeekBar(progress: Int) {
        filterToApply.selectedAmount = progress
        binding.filterFrTvSelectedRangeAmount.text = getSelectedAmount(progress.toString())
    }

    /**
     * Loads filters from the [FilterViewModel] and updates the UI accordingly.
     */
    private fun loadFilters() {
        binding.filterFrBtButtonFrom.text = filterViewModel.getDateFrom(this.requireContext())
        binding.filterFrBtButtonTo.text = filterViewModel.getDateTo(this.requireContext())
        val maxAmountProgressBar = ceil(filterViewModel.getMaxAmountInList()).toInt()
        binding.filterFrSbSeekBarAmount.max = maxAmountProgressBar
        binding.filterFrSbSeekBarAmount.progress = filterViewModel.getSelectedAmount()
        binding.filterFrTvAmountMax.text = "$maxAmountProgressBar €"
        binding.filterFrTvSelectedRangeAmount.text =
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

    /**
     * Gets the selected amount in the specified format.
     *
     * @param maxAmount The maximum amount value.
     * @return A formatted string representing the selected amount range.
     */
    private fun getSelectedAmount(maxAmount: String): String {
        return ("0 € - $maxAmount €")
    }

    /**
     * Applies the current filters to the [FilterViewModel].
     */
    private fun setFilters() {
        filterToApply.dateFrom = stringToDate(binding.filterFrBtButtonFrom.text)
        filterToApply.dateTo = stringToDate(binding.filterFrBtButtonTo.text)
        filterToApply.selectedAmount = binding.filterFrSbSeekBarAmount.progress
        filterToApply.statusPaid = binding.filterFrCbPaid.isChecked
        filterToApply.statusCancelled = binding.filterFrCbCancelled.isChecked
        filterToApply.statusFixedFee = binding.filterFrCbFixedFee.isChecked
        filterToApply.statusPendingPayment = binding.filterFrCbPendingPayment.isChecked
        filterToApply.statusPaymentPlan = binding.filterFrCbPaymentPlan.isChecked
        filterViewModel.setFilters(filterToApply)
    }

    /**
     * Converts a string date representation to a [Date] object.
     *
     * @param dateString The string representation of the date.
     * @return A [Date] object representing the parsed date.
     */
    private fun stringToDate(dateString: CharSequence): Date? {
        if (dateString.toString().equals(getString(R.string.title_buttonDayMonthYear))) {
            return null
        }
        var date = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            date = dateFormat.parse(dateString.toString())!!
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return date
    }

    /**
     * Resets all filters to their default values.
     */
    private fun resetFilters() {
        binding.filterFrBtButtonFrom.text = getString(R.string.title_buttonDayMonthYear)
        binding.filterFrBtButtonTo.text = getString(R.string.title_buttonDayMonthYear)
        val maxAmountProgressBar = ceil(filterViewModel.getMaxAmountInList()).toInt()
        binding.filterFrSbSeekBarAmount.max = maxAmountProgressBar
        binding.filterFrSbSeekBarAmount.progress = maxAmountProgressBar
        binding.filterFrTvAmountMax.text = "$maxAmountProgressBar €"
        binding.filterFrTvSelectedRangeAmount.text =
            getSelectedAmount(
                maxAmountProgressBar.toString()
            )
        binding.filterFrCbPaid.isChecked = false
        binding.filterFrCbCancelled.isChecked = false
        binding.filterFrCbFixedFee.isChecked = false
        binding.filterFrCbPendingPayment.isChecked = false
        binding.filterFrCbPaymentPlan.isChecked = false
    }
}