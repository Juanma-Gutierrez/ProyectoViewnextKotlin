package com.viewnext.proyectoviewnext.data.local.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.viewnext.proyectoviewnext.data.local.invoice.InvoiceDao
import com.viewnext.proyectoviewnext.data.local.invoice.InvoiceEntity

/**
 * Database class representing the database for invoices.
 *
 * @property invoiceDao The Data Access Object (DAO) for accessing invoice data.
 */
@Database(entities = [InvoiceEntity::class], version = 1)
abstract class InvoicesDatabase : RoomDatabase() {
    abstract fun invoiceDao(): InvoiceDao
}