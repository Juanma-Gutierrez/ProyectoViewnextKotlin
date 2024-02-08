package com.viewnext.proyectoviewnext.utils

import co.infinum.retromock.Retromock
import com.viewnext.proyectoviewnext.constants.Constants
import com.viewnext.proyectoviewnext.data.api.retromock.ResourceBodyFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Class that provides instances of Retrofit and Retromock for network communication.
 */
class Services {
    /**
     * Singleton object providing an instance of Retrofit for API calls.
     */
    object RetrofitInstance {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    /**
     * Singleton object providing an instance of Retromock for API mocking during testing.
     */
    object RetromockInstance {
        val retromock: Retromock = Retromock.Builder()
         .retrofit(RetrofitInstance.retrofit)
         .defaultBodyFactory(ResourceBodyFactory())
         .build()

    }
}