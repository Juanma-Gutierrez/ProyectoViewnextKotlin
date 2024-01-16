package com.viewnext.proyectoviewnext.services

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.viewnext.proyectoviewnext.R

class Services {
    fun showSnackBar(message: String, view: View) {
        val snackBar = Snackbar.make(
            view,
            message,
            Snackbar.LENGTH_SHORT
        )
        val snackBarView = snackBar.view
        val layoutParams = snackBarView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(
            layoutParams.leftMargin,
            layoutParams.topMargin,
            layoutParams.rightMargin,
            250
        )
        snackBarView.layoutParams = layoutParams
        snackBar.show()
    }

    fun showAlertDialog(context: Context, title: String, message: String, btCloseText: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(btCloseText) { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}

