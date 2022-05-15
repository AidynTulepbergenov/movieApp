package com.example.mynavigation.viewModel

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.core.content.ContextCompat
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

class ProfileViewModel(context: Context) : ViewModel(), CoroutineScope {
    private val job: Job = Job()
    private val cont = context
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    fun deleteSession(sessionId: String) {
        launch {
            val session = Session(sessionId)
            RetrofitService.getMovieApi().deleteSession(session = session)
        }
    }

    fun setProfileDetails(sessionId: String): User {
        var user = User("Def","def")
        launch {
            val response = RetrofitService.getMovieApi().getAccountDetails(session_id = sessionId)
            if (response.isSuccessful){
                user = response.body()!!
            }
        }
        return user
    }

    fun setImage() {

    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}