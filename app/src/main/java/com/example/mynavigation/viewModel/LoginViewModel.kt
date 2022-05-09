package com.example.mynavigation.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mynavigation.model.network.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@SuppressLint("StaticFieldLeak")
class LoginViewModel(context: Context) : ViewModel(), CoroutineScope {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job
    private val myContext = context

    private val _sessionId = MutableLiveData<String>()
    val sessionId: LiveData<String>
        get() = _sessionId



    fun login(data: LoginApprove) {
        launch {
            val responseGet = RetrofitService.getMovieApi().getToken()
            if (responseGet.isSuccessful) {

                val loginApprove = LoginApprove(
                    username = data.username,
                    password = data.password,
                    request_token = responseGet.body()?.request_token as String
                )
                val responseApprove = RetrofitService.getMovieApi().validateToken(loginApprove = loginApprove)

                if (responseApprove.isSuccessful) {
                    val session =
                        RetrofitService.getMovieApi().createSession(token = responseApprove.body() as Token)
                    if (session.isSuccessful) {
                        _sessionId.value = session.body()?.session_id
                    }
                } else {
                    Toast.makeText(myContext, "Неверные данные", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}