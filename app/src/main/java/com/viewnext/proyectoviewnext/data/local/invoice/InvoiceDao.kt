package com.viewnext.proyectoviewnext.data.local.invoice

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Data Access Object (DAO) for interacting with invoice data in the local database.
 */
@Dao
interface InvoiceDao {
    /**
     * Inserts a list of invoices into the database.
     *
     * @param listInvoice The list of invoices to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createInvoiceList(listInvoice: List<InvoiceEntity>)

    /**
     * Retrieves all invoices from the database.
     *
     * @return A list of all invoices stored in the database.
     */
    @Query("SELECT * from invoice")
    suspend fun getAllInvoices(): List<InvoiceEntity>

    /**
     * Deletes all invoices from the database.
     */
    @Query("DELETE from invoice")
    suspend fun deleteAllInvoices()
}