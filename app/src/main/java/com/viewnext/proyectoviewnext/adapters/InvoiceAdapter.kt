package com.viewnext.proyectoviewnext.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.viewnext.proyectoviewnext.R
import com.viewnext.proyectoviewnext.data.models.Invoice
import com.viewnext.proyectoviewnext.utils.Services

class InvoiceAdapter(
    private var invoices: List<Invoice>,
    private val context: Context
) : RecyclerView.Adapter<InvoiceViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): InvoiceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return InvoiceViewHolder(layoutInflater.inflate(R.layout.invoice_item, parent, false))
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        val item = invoices[position]
        holder.bind(item)
        holder.binding.invoiceItemIvForward.setOnClickListener {
            val svc = Services()
            val title = getString(context, R.string.warning_title)
            val message = getString(context, R.string.warning_message)
            val btCloseText = getString(context, R.string.bt_close)
            svc.showAlertDialog(context, title, message, btCloseText)
        }
    }

    override fun getItemCount(): Int {
        return invoices.size
    }

    fun updateList(newList: List<Invoice>) {
        invoices = newList
        notifyDataSetChanged()
    }
}