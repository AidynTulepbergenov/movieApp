package com.example.mynavigation.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynavigation.domain.common.UseCaseResponse
import com.example.mynavigation.domain.model.ApiError
import com.example.mynavigation.domain.model.responses.Comment
import com.example.mynavigation.domain.usecases.GetMovieReviewUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class MovieReviewViewModel(private val getMovieReviewUseCase: GetMovieReviewUseCase) : ViewModel(),
    CoroutineScope {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job
    private var id: Int = 0

    private var _reviews = MutableLiveData<State>()
    val reviews: LiveData<State>
        get() = _reviews

    fun setArgs(id: Int) {
        this.id = id
    }

    fun getReviews() {
        _reviews.value = State.ShowLoading
        getMovieReviewUseCase.invoke(viewModelScope, id, object : UseCaseResponse<List<Comment>> {
            override fun onSuccess(result: List<Comment>) {
                _reviews.value = State.Result(result)
            }

            override fun onError(apiError: ApiError?) {
                _reviews.value = State.Error
            }
        })
        _reviews.value = State.HideLoading
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        object Error : State()
        data class Result(val list: List<Comment>?) : State()
    }
}