package com.example.mynavigation.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynavigation.domain.common.UseCaseResponse
import com.example.mynavigation.domain.model.ApiError
import com.example.mynavigation.domain.model.responses.User
import com.example.mynavigation.domain.usecases.GetAccountDetailUseCase
import com.example.mynavigation.domain.usecases.LogoutUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class ProfileViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val getAccountDetailUseCase: GetAccountDetailUseCase
) : ViewModel(), CoroutineScope {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun deleteSession(sessionId: String) {
        logoutUseCase.invoke(viewModelScope, sessionId, object : UseCaseResponse<Any>{
            override fun onSuccess(result: Any) {

            }

            override fun onError(apiError: ApiError?) {

            }

        } )

    }

    fun setProfileDetails(sessionId: String) {
        getAccountDetailUseCase.invoke(viewModelScope, sessionId, object : UseCaseResponse<User> {
            override fun onSuccess(result: User) {
                _user.value = result
            }

            override fun onError(apiError: ApiError?) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}