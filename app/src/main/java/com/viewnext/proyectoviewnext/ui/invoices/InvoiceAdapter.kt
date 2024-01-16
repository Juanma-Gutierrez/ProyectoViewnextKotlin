package com.viewnext.proyectoviewnext.ui.invoices

import android.app.AlertDialog
import android.content.Context
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
    private val navController: NavController,
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
            showAlertDialog(context)
        }
    }

    override fun getItemCount(): Int {
        return invoices.size
    }


    private fun showAlertDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Información")
        builder.setMessage("Esta funcionalidad aún no está disponible")
        builder.setPositiveButton("Cerrar") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}