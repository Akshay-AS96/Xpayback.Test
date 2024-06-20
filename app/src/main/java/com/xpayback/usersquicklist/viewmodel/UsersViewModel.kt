package com.xpayback.usersquicklist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xpayback.usersquicklist.model.User
import com.xpayback.usersquicklist.repository.UsersRepository
import com.xpayback.usersquicklist.utils.Constants.Companion.PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsersViewModel(private val repository: UsersRepository) : ViewModel() {
    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> get() = _userList

    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> get() = _userData

    val errorMessage = MutableLiveData<String>()
    var loadingState = MutableLiveData<Boolean>()

    private var currentPage = 0 // Current page for pagination
    private var isLoading = false // Flag to track ongoing loading operations
    private var isLastPage = false // Flag to indicate if the last page has been reached

    // Loads the initial list of users
    fun loadUsers() {
        fetchUsers(skip = currentPage)
    }

    // Loads more users for pagination
    fun loadMoreUsers() {
        if (!isLoading && !isLastPage) {
            currentPage += PAGE_SIZE
            fetchUsers(skip = currentPage)
        }
    }

    // Fetches users from the repository (network)
    private fun fetchUsers(limit: Int = PAGE_SIZE, skip: Int = 0) {
        viewModelScope.launch {
            isLoading = true
            loadingState.value = isLoading // Indicate loading started

            try {
                // Fetch users on a background thread (Dispatchers.IO)
                val retrievedProducts = withContext(Dispatchers.IO) {
                    repository.getUsers(limit, skip)
                }

                retrievedProducts.let {
                    _userList.postValue(it.users) // Update the user list LiveData

                    // Check if the last page has been reached
                    if ((retrievedProducts.users.lastOrNull()?.id
                            ?: 0) == retrievedProducts.total
                    ) {
                        isLastPage = true
                        Log.d("API_LAST_PAGE", "Called")
                    }

                    // Log successful API response
                    Log.d("API_SUCCESS", "Users: $it")
                    Log.d("API_LOG", "Total users:: ${it.total}")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}")
                errorMessage.postValue(e.message)
            }

            isLoading = false
            loadingState.value = isLoading // Indicate loading finished
        }
    }

    // Fetches details of a single user
    fun userDetails(id: Int) {
        viewModelScope.launch {
            try {
                // Fetch user details on a background thread
                val retrievedProducts = withContext(Dispatchers.IO) {
                    repository.getUser(id)
                }

                retrievedProducts.let {
                    _userData.postValue(it) // Update the user details LiveData
                    Log.d("API_SUCCESS", "UserData: $it")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}")
                errorMessage.postValue(e.message)
            }
        }
    }

    // Returns the current loading state
    fun isLoading() = isLoading

    // Returns whether the last page has been reached
    fun isLastPage() = isLastPage
}