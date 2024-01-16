package com.viewnext.proyectoviewnext.ui.invoices

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.viewnext.proyectoviewnext.data.models.Invoice
import com.viewnext.proyectoviewnext.databinding.InvoiceItemBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Currency

class InvoiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = InvoiceItemBinding.bind(view)

    fun bind(invoice: Invoice) {
        binding.invoiceItemTvDate.text =
            invoice.date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        binding.invoiceItemTvStatus.text = invoice.status
        binding.invoiceItemTvAmount.text = formatAmount(invoice.amount)
    }

    private fun formatAmount(amount: Float): String {
        val decimalFormat = DecimalFormat("#,##0.00 €")
        val symbols = DecimalFormatSymbols()
        symbols.decimalSeparator = ','
        decimalFormat.decimalFormatSymbols = symbols
        val formattedAmount = "${decimalFormat.format(amount)}"
        return formattedAmount
    }
}