package com.dicoding.myintermediateapplication.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.myintermediateapplication.data.UserRepository
import com.dicoding.myintermediateapplication.data.pref.UserModel
import com.dicoding.myintermediateapplication.data.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    fun login(email: String, password: String, onLoginResult: (LoginResponse?) -> Unit) {
        viewModelScope.launch {
            val response = repository.login(email, password)
            onLoginResult(response)
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}