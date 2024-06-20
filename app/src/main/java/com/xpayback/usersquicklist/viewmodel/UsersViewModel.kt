package com.xpayback.usersquicklist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xpayback.usersquicklist.model.User
import com.xpayback.usersquicklist.model.UserResponse
import com.xpayback.usersquicklist.repository.UsersRepository
import com.xpayback.usersquicklist.utils.Constants.Companion.PAGE_SIZE
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsersViewModel(private val repository: UsersRepository) : ViewModel() {
    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> get() = _userList
    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> get() = _userData
    val errorMessage = MutableLiveData<String>()
    var loadingState = MutableLiveData<Boolean>()
    private var currentPage = 0
    private var isLoading = false
    private var isLastPage = false

    /*init {
        loadUsers()
    }*/

    fun loadUsers() {
        fetchUsers(skip = currentPage)
    }

    fun loadMoreUsers() {
        if (!isLoading && !isLastPage) {
            currentPage += PAGE_SIZE
            fetchUsers(skip = currentPage)
        }
    }

    private fun fetchUsers(limit: Int = PAGE_SIZE, skip: Int = 0) {
        viewModelScope.launch {
            isLoading = true
            loadingState.value=isLoading
            try {
                val retrievedProducts = withContext(Dispatchers.IO) {
                    repository.getUsers(limit, skip)
                }
                retrievedProducts.let {
                    _userList.postValue(it.users)
                    if ((retrievedProducts.users.lastOrNull()?.id
                            ?: 0) == retrievedProducts.total
                    ) {
                        isLastPage = true
                        Log.d("API_LAST_PAGE", "Called")
                    }
                    Log.d("API_SUCCESS", "Users: $it")
                    Log.d("API_LOG", "Total users:: ${it.total}")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}")
                errorMessage.postValue(e.message)
            }
            isLoading = false
            loadingState.value=isLoading
        }
    }

    fun userDetails(id: Int) {
        viewModelScope.launch {
            try {
                val retrievedProducts = withContext(Dispatchers.IO) {
                    repository.getUser(id)
                }
                retrievedProducts.let {
                    _userData.postValue(it)
                    Log.d("API_SUCCESS", "UserData: $it")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}")
                errorMessage.postValue(e.message)
            }
        }
    }


    fun isLoading() = isLoading

    fun isLastPage() = isLastPage
}
