package com.viewnext.proyectoviewnext.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.viewnext.proyectoviewnext.R
import com.viewnext.proyectoviewnext.databinding.FragmentFilterBinding
import com.viewnext.proyectoviewnext.utils.FilterService
import com.viewnext.proyectoviewnext.utils.dateToString
import com.viewnext.proyectoviewnext.utils.getDayFromStringDate
import com.viewnext.proyectoviewnext.utils.getMonthFromStringDate
import com.viewnext.proyectoviewnext.utils.getYearFromStringDate
import com.viewnext.proyectoviewnext.utils.showDatePickerDialog
import com.viewnext.proyectoviewnext.utils.stringToDate
import com.viewnext.proyectoviewnext.viewmodels.FilterViewModel
import java.util.Calendar
import java.util.Date
import kotlin.math.ceil

/**
 * Fragment for applying filters to the list of invoices.
 */
class FilterFragment : Fragment() {
    private lateinit var binding: FragmentFilterBinding
    private lateinit var filterViewModel: FilterViewModel

    /**
     * Called when the fragment is creating its user interface.
     *
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     * saved state as given here.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
        val filterSvc = FilterService

        loadFilters()
        ivClose.setOnClickListener {
            findNavController().navigateUp()
        }
        btDateFrom.setOnClickListener {
            showDatePickerDialog(
                this.requireContext(),
                stringToDate(binding.filterFrBtButtonFrom.text, this.context!!),
                null,
                calculateMax(binding.filterFrBtButtonTo.text.toString())
            ) { calendar ->
                binding.filterFrBtButtonFrom.text =
                    dateToString(calendar.time, this.requireContext())
            }
        }
        btDateTo.setOnClickListener {
            showDatePickerDialog(
                this.requireContext(),
                stringToDate(binding.filterFrBtButtonFrom.text, this.context!!),
                calculateMin(binding.filterFrBtButtonFrom.text.toString()),
                calculateMax(null)
            ) { calendar ->
                binding.filterFrBtButtonTo.text =
                    dateToString(calendar.time, this.requireContext())
            }
        }

        sbAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                println(filterSvc.filterToApply)
                changeValueOnSelectedSeekBar(sbAmount.progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        btApply.setOnClickListener {
            setFilters()
            findNavController().navigateUp()
        }
        btRemoveFilters.setOnClickListener {
            resetFilters()
        }
    }

    private fun calculateMin(minDate: String?): Date? {
        val calendar = Calendar.getInstance()
        if (minDate != getString(R.string.title_buttonDayMonthYear)) {
            val year = getYearFromStringDate(minDate!!)
            val month = getMonthFromStringDate(minDate)
            val day = getDayFromStringDate(minDate)
            calendar.set(year, month, day)
        } else {
            return null
        }
        return calendar.time
    }

    private fun calculateMax(maxDate: String?): Date? {
        val calendar = Calendar.getInstance()
        println(maxDate)
        if (maxDate != null && maxDate != getString(R.string.title_buttonDayMonthYear)) {
            println("Entra en el if")
            val year = getYearFromStringDate(maxDate)
            val month = getMonthFromStringDate(maxDate)
            val day = getDayFromStringDate(maxDate)
            calendar.set(year, month, day)
        } else {
            return calendar.time
        }
        return calendar.time
    }

    /**
     * Updates the selected amount in the filter and updates the corresponding TextView.
     *
     * @param progress The progress value from the SeekBar.
     */
    private fun changeValueOnSelectedSeekBar(progress: Int) {
        val filterSvc = FilterService
        filterSvc.filterToApply.selectedAmount = progress
        binding.filterFrTvSelectedRangeAmount.text = getSelectedAmount(progress)
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
        binding.filterFrTvAmountMax.text = convertAmountToIntMoney(maxAmountProgressBar)
        binding.filterFrTvSelectedRangeAmount.text = getSelectedAmount(
            if (filterViewModel.getSelectedAmount() == Integer.MAX_VALUE) {
                maxAmountProgressBar
            } else {
                filterViewModel.getSelectedAmount()
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
    private fun getSelectedAmount(maxAmount: Int): String {
        val min = convertAmountToIntMoney(0)
        val max = convertAmountToIntMoney(maxAmount)
        return ("$min - $max")
    }

    /**
     * Applies the current filters to the [FilterViewModel].
     */
    private fun setFilters() {
        val filterSvc = FilterService
        filterSvc.filterToApply.dateFrom =
            stringToDate(binding.filterFrBtButtonFrom.text, this.requireContext())
        filterSvc.filterToApply.dateTo =
            stringToDate(binding.filterFrBtButtonTo.text, this.requireContext())
        filterSvc.filterToApply.selectedAmount = binding.filterFrSbSeekBarAmount.progress
        filterSvc.filterToApply.statusPaid = binding.filterFrCbPaid.isChecked
        filterSvc.filterToApply.statusCancelled = binding.filterFrCbCancelled.isChecked
        filterSvc.filterToApply.statusFixedFee = binding.filterFrCbFixedFee.isChecked
        filterSvc.filterToApply.statusPendingPayment = binding.filterFrCbPendingPayment.isChecked
        filterSvc.filterToApply.statusPaymentPlan = binding.filterFrCbPaymentPlan.isChecked
        filterViewModel.setFilters(filterSvc.filterToApply)
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
        binding.filterFrTvAmountMax.text = convertAmountToIntMoney(maxAmountProgressBar)
        binding.filterFrTvSelectedRangeAmount.text = getSelectedAmount(maxAmountProgressBar)
        binding.filterFrCbPaid.isChecked = false
        binding.filterFrCbCancelled.isChecked = false
        binding.filterFrCbFixedFee.isChecked = false
        binding.filterFrCbPendingPayment.isChecked = false
        binding.filterFrCbPaymentPlan.isChecked = false
    }

    private fun convertAmountToIntMoney(num: Int) = "$num €"
}
