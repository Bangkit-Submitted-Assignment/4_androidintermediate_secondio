package com.dicoding.myintermediateapplication.view.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.myintermediateapplication.data.UserRepository
import com.dicoding.myintermediateapplication.data.response.RegisterResponse
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: UserRepository) : ViewModel() {

    // Function to perform registration
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            val response: RegisterResponse = repository.register(name, email, password)
            handleRegisterResponse(response)
        }
    }

    private fun handleRegisterResponse(response: RegisterResponse) {
        if (response.error == false) {
            // Registration successful
            Log.d("SignupViewModel", "Registration successful: ${response.message}")
        } else {
            // Registration failed
            Log.e("SignupViewModel", "Registration failed: ${response.message}")
        }
    }
}