package com.example.mynavigation.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynavigation.domain.common.UseCaseResponse
import com.example.mynavigation.domain.model.ApiError
import com.example.mynavigation.domain.model.responses.LoginApprove
import com.example.mynavigation.domain.model.responses.Session
import com.example.mynavigation.domain.usecases.LoginUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel(), CoroutineScope {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    private val _sessionId = MutableLiveData<State>()
    val sessionId: LiveData<State>
        get() = _sessionId

    fun login(data: LoginApprove) {
        _sessionId.value = State.ShowLoading

        loginUseCase.invoke(viewModelScope, data, object : UseCaseResponse<Session> {
            override fun onSuccess(result: Session) {
                _sessionId.value = State.Result(result.session_id)
            }

            override fun onError(apiError: ApiError?) {
                _sessionId.value = State.StatusFailed
            }
        })

        _sessionId.value = State.HideLoading
    }

    sealed class State {
        object StatusFailed : State()
        object ShowLoading : State()
        object HideLoading : State()
        data class Result(val sessionId: String) : State()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}