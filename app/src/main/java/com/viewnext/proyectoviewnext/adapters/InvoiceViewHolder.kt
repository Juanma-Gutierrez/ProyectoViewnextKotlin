package com.viewnext.proyectoviewnext.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.viewnext.proyectoviewnext.data.models.Invoice
import com.viewnext.proyectoviewnext.databinding.InvoiceItemBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.format.DateTimeFormatter

class InvoiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = InvoiceItemBinding.bind(view)

    fun bind(invoice: Invoice) {
        binding.invoiceItemTvDate.text =
            invoice.date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        binding.invoiceItemTvStatus.text = invoice.status
        binding.invoiceItemTvAmount.text = formatAmount(invoice.amount)
        binding.invoiceItemIvForward.setOnClickListener {

        }
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