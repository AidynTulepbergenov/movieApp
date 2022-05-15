package com.example.mynavigation.viewModel

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mynavigation.model.data.User
import com.example.mynavigation.model.network.RetrofitService
import com.example.mynavigation.model.network.Session
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.jar.Manifest
import kotlin.coroutines.CoroutineContext

class ProfileViewModel : ViewModel(), CoroutineScope {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun deleteSession(sessionId: String) {
        launch {
            val session = Session(sessionId)
            RetrofitService.getMovieApi().deleteSession(session = session)
        }
    }

    fun setProfileDetails(sessionId: String) {
        launch {
            val response = RetrofitService.getMovieApi().getAccountDetails(session_id = sessionId)
            if (response.isSuccessful){
                _user.value = response.body()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}