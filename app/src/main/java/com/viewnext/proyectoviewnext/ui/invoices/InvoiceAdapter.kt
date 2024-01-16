package com.viewnext.proyectoviewnext.ui.invoices

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.viewnext.proyectoviewnext.R
import com.viewnext.proyectoviewnext.data.models.Invoice
import kotlinx.coroutines.withContext

class InvoiceAdapter(
    private val invoices: List<Invoice>,
    private val navController: NavController
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
            navController.navigate(R.id.action_invoicesFragment_to_warningFragment)
        }
    }

    override fun getItemCount(): Int {
        return invoices.size
    }
}