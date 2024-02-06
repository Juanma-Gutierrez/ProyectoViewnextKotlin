package com.viewnext.proyectoviewnext.utils

import co.infinum.retromock.Retromock
import com.viewnext.proyectoviewnext.constants.Constants
import com.viewnext.proyectoviewnext.data.api.retromock.ResourceBodyFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Services {
    object RetrofitInstance {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    object RetromockInstance {
        val retromock: Retromock = Retromock.Builder()
         .retrofit(RetrofitInstance.retrofit)
         .defaultBodyFactory(ResourceBodyFactory())
         .build()

    }
}