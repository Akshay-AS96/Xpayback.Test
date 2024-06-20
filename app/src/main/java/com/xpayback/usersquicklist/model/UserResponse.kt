package com.xpayback.usersquicklist.model

data class UserResponse(
    val users: List<User>, // List of user objects
    val total: Int, // Total number of users available
    val skip: Int, // Number of users skipped (for pagination)
    val limit: Int // Number of users fetched per page
)