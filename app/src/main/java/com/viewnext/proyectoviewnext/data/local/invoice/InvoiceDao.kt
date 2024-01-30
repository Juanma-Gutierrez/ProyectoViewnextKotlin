package com.viewnext.proyectoviewnext.data.local.invoice

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface InvoiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createInvoiceList(listInvoice: List<InvoiceEntity>)

    @Query("SELECT * from invoice")
    suspend fun getAllInvoices(): List<InvoiceEntity>
}