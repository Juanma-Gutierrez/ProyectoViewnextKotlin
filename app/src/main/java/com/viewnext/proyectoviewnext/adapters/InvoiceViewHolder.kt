package com.viewnext.proyectoviewnext.adapters

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.viewnext.proyectoviewnext.data.models.Invoice
import com.viewnext.proyectoviewnext.databinding.InvoiceItemBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.format.DateTimeFormatter

/**
 * ViewHolder for displaying individual invoice items in a RecyclerView.
 *
 * @property view The view representing an item in the RecyclerView.
 */
class InvoiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = InvoiceItemBinding.bind(view)

    /**
     * Binds invoice data to the view.
     *
     * @param invoice The invoice data to be displayed.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(invoice: Invoice) {
        binding.invoiceItemTvDate.text =
            invoice.date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        binding.invoiceItemTvStatus.text = invoice.status
        binding.invoiceItemTvAmount.text = formatAmount(invoice.amount)
        binding.invoiceItemIvForward.setOnClickListener {

        }
    }

    /**
     * Formats the invoice amount with the specified format.
     *
     * @param amount The amount to be formatted.
     * @return The formatted amount as a String.
     */
    private fun formatAmount(amount: Float): String {
        val decimalFormat = DecimalFormat("#,##0.00 €")
        val symbols = DecimalFormatSymbols()
        symbols.decimalSeparator = ','
        decimalFormat.decimalFormatSymbols = symbols
        val formattedAmount = "${decimalFormat.format(amount)}"
        return formattedAmount
    }
}