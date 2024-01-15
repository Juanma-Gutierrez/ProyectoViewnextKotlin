package com.viewnext.proyectoviewnext.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.viewnext.proyectoviewnext.R
import com.viewnext.proyectoviewnext.services.Services

class InvoicesFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_invoices, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botón filtro para que abra el fragment Filter
        val ivFilter = view.findViewById<ImageView>(R.id.mainToolbar_iv_filter)
        ivFilter.setOnClickListener {
            findNavController().navigate(R.id.action_invoicesFragment_to_filterFragment)
        }
        // Botón regreso
        val ivBack = view.findViewById<ImageView>(R.id.mainToolbar_iv_backIcon)
        ivBack.setOnClickListener {
            val svc = Services()
            svc.showSnackBar(getString(R.string.not_available), view)
        }

        /*
                // Botón navegar para que muestre fragment Warning
                val navigateImageView = view.findViewById<MaterialButton>(R.id.fragmentMain_bt_navigate)
                navigateImageView.setOnClickListener {
                    findNavController().navigate(R.id.action_mainFragment_to_warningFragment)
                }

         */
    }
}