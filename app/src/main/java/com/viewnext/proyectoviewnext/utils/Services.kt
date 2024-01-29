package com.viewnext.proyectoviewnext.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

/**
 * Utility class containing various services such as showing snack bars and alert dialogs.
 */
class Services {

    /**
     * Displays a snackbar with the provided message.
     *
     * @param message The message to be displayed in the snackbar.
     * @param view The view where the snackbar should be displayed.
     */
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
            150,
        )
        snackBarView.layoutParams = layoutParams
        snackBar.show()
    }


    /**
     * Displays an alert dialog with the provided title, message, and close button text.
     *
     * @param context The context in which the alert dialog should be displayed.
     * @param title The title of the alert dialog.
     * @param message The message to be displayed in the alert dialog.
     * @param btCloseText The text for the close button in the alert dialog.
     */
    fun showAlertDialog(context: Context, title: String, message: String, btCloseText: String) {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(btCloseText) { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}

