package com.xpayback.usersquicklist.network.service

import com.xpayback.usersquicklist.model.User
import com.xpayback.usersquicklist.model.UserResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun getUsers(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): UserResponse

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Int): User
}