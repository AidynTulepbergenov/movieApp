package com.example.mynavigation.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynavigation.domain.common.UseCaseResponse
import com.example.mynavigation.domain.model.ApiError
import com.example.mynavigation.domain.model.data.Movie
import com.example.mynavigation.domain.model.responses.AccountStates
import com.example.mynavigation.domain.usecases.GetMovieDetailUseCase
import com.example.mynavigation.domain.usecases.MarkFavoriteUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class MovieDetailViewModel(private val getMovieDetailUseCase: GetMovieDetailUseCase, private val markFavoriteUseCase: MarkFavoriteUseCase) : ViewModel(),
    CoroutineScope {

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job
    private var id: Int = 0

    private var _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean>
        get() = _isFavourite

    private var _movieData = MutableLiveData<State>()
    val movieData: LiveData<State>
        get() = _movieData

    fun setArgs(id: Int) {
        this.id = id
    }

    fun getMovie() {
        _movieData.value = State.ShowLoading
        getMovieDetailUseCase.invoke(viewModelScope, id, object : UseCaseResponse<Movie> {
            override fun onSuccess(result: Movie) {
                _movieData.value = State.Result(result)
            }

            override fun onError(apiError: ApiError?) {
                //todo
            }
        })
        _movieData.value = State.HideLoading
    }

    fun markFavorite() {
        markFavoriteUseCase.invoke(
            viewModelScope,
            id,
            object : UseCaseResponse<AccountStates> {
                override fun onSuccess(result: AccountStates) {
                   _isFavourite.value = result.favorite
                }

                override fun onError(apiError: ApiError?) {
                    //todo
                }
            })
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class Result(val movie: Movie) : State()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}