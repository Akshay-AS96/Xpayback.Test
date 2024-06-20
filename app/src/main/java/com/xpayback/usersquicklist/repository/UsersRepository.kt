package com.xpayback.usersquicklist.repository

import com.xpayback.usersquicklist.model.User
import com.xpayback.usersquicklist.model.UserResponse
import com.xpayback.usersquicklist.network.service.ApiService
import retrofit2.Call


class UsersRepository(private val userService: ApiService) {

    // Fetches a list of users from the API
    suspend fun getUsers(limit: Int, skip: Int): UserResponse {
        return userService.getUsers(limit, skip)
    }

    // Fetches details of a single user from the API
    suspend fun getUser(id: Int): User {
        return userService.getUser(id)
    }
}