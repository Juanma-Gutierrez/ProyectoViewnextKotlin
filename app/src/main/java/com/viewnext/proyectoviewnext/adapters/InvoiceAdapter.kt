package com.viewnext.proyectoviewnext.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.viewnext.proyectoviewnext.R
import com.viewnext.proyectoviewnext.data.models.Invoice
import com.viewnext.proyectoviewnext.utils.showAlertDialog

/**
 * Adapter for displaying a list of invoices in a RecyclerView.
 *
 * @property invoices The list of invoices to display.
 * @property context The context associated with the adapter.
 */
class InvoiceAdapter(
    private var invoices: List<Invoice>,
    private val context: Context
) : RecyclerView.Adapter<InvoiceViewHolder>() {

    /**
     * Called when RecyclerView needs a new [InvoiceViewHolder] of the given type to represent
     * an item.
     *
     * @param parent The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new [InvoiceViewHolder] that holds a View with the given layout resource.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): InvoiceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return InvoiceViewHolder(layoutInflater.inflate(R.layout.item_invoice, parent, false))
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [holder.itemView] to reflect the item at the given position.
     *
     * @param holder The [InvoiceViewHolder] which should be updated to represent the contents of
     * the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        val item = invoices[position]
        holder.bind(item)
        holder.binding.invoiceItemIvForward.setOnClickListener {
            val title = getString(context, R.string.warning_title)
            val message = getString(context, R.string.function_not_available)
            val btCloseText = getString(context, R.string.bt_close)
            showAlertDialog(context, title, message, btCloseText)
        }
    }

    /**
     * Gets the total number of invoices in the dataset.
     *
     * @return The total number of invoices.
     */
    override fun getItemCount(): Int {
        return invoices.size
    }

    /**
     * Updates the current list of invoices with a new list and notifies any registered observers
     * that the data set has changed.
     *
     * @param newList The new list of invoices to replace the current list.
     */
    fun updateList(newList: List<Invoice>) {
        invoices = newList
        notifyDataSetChanged()
    }
}