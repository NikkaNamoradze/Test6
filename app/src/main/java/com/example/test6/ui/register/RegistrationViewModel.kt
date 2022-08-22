package com.example.test6.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test6.model.registration.RegistrationResponse
import com.example.test6.model.registration.RegistrationSend
import com.example.test6.network.RetrofitClient
import com.example.test6.util.ResponseHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {
    private val _registerState =
        MutableStateFlow<ResponseHandler<RegistrationResponse>>(ResponseHandler.Loading(true))
    val registerState = _registerState.asStateFlow()

    fun register(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            when {
                !email.contains('@') -> {
                    _registerState.emit(ResponseHandler.Failure("email must contain @"))
                }
                password.length <= 7 -> {
                    _registerState.emit(ResponseHandler.Failure("password must contain 7 characters"))
                }
                confirmPassword != password ->{
                    _registerState.emit(ResponseHandler.Failure("match passwords"))
                }
                else -> {
                    registerResponse(email, password).collect {
                        _registerState.value = it
                    }
                }
            }
        }
    }

    private fun registerResponse(email: String, password: String) = flow {

        val response = RetrofitClient.authService.register(RegistrationSend(email, password))

        try {
            when {
                response.isSuccessful -> {
                    val body = response.body()!!
                    emit(ResponseHandler.Success(body))
                }
                else -> {
                    val error = response.errorBody()
                    emit(ResponseHandler.Failure(errorMessage = error?.string() ?: "error"))
                }
            }
        } catch (e: Exception) {
            emit(ResponseHandler.Failure(e.message.toString()))
        }
    }
}