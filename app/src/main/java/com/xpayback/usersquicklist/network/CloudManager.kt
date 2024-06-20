package com.xpayback.usersquicklist.network

import com.xpayback.usersquicklist.network.service.ApiService
import com.xpayback.usersquicklist.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface CloudManager {

    companion object {

        private val retrofit: Retrofit =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        val apiService: ApiService = retrofit.create(ApiService::class.java)
    }
}
