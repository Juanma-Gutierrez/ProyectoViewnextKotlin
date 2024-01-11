package com.viewnext.proyectoviewnext.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.viewnext.proyectoviewnext.databinding.ActivityMainBinding
import com.viewnext.proyectoviewnext.services.Service

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val service = Service()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        service.showSnackBar( "Pruebas", binding.root)

    }
}