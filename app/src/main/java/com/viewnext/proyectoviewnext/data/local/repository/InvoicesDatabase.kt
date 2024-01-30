package com.viewnext.proyectoviewnext.data.local.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.viewnext.proyectoviewnext.data.local.invoice.InvoiceDao
import com.viewnext.proyectoviewnext.data.local.invoice.InvoiceEntity

@Database(entities = [InvoiceEntity::class], version = 1)
abstract class InvoicesDatabase : RoomDatabase() {
    abstract fun invoiceDao(): InvoiceDao
}