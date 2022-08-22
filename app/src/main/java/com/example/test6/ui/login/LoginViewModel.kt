package com.example.test6.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test6.model.login.LoginResponse
import com.example.test6.model.login.LoginSend
import com.example.test6.network.RetrofitClient
import com.example.test6.util.ResponseHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _logInState = MutableStateFlow<ResponseHandler<LoginResponse>>(ResponseHandler.Loading(true))
    val logInState = _logInState.asStateFlow()

    fun logIn(email: String, password: String){
        viewModelScope.launch {
            when {
                !email.contains('@') -> {
                    _logInState.emit(ResponseHandler.Failure("email must contain @"))

                }
                password.length < 7 -> {
                    _logInState.emit(ResponseHandler.Failure("password must contain 8 character"))
                }
                else -> {
                    loginResponse(email = email, password = password).collect {
                        _logInState.value = it
                    }
                }
            }
        }
    }

    private fun loginResponse(email: String, password: String) = flow {
        try {
            val response = RetrofitClient.authService.logIn(LoginSend(email, password))
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