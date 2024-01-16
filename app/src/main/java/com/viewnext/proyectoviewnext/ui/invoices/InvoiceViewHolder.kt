package com.viewnext.proyectoviewnext.ui.invoices

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.viewnext.proyectoviewnext.data.models.Invoice
import com.viewnext.proyectoviewnext.databinding.InvoiceItemBinding
import java.time.format.DateTimeFormatter

class InvoiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = InvoiceItemBinding.bind(view)

    fun bind(invoice: Invoice) {
        binding.invoiceItemTvDate.text = invoice.date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        binding.invoiceItemTvStatus.text = invoice.status
        binding.invoiceItemTvAmount.text = invoice.amount.toString()
    }
}