package com.example.mynavigation.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynavigation.domain.common.UseCaseResponse
import com.example.mynavigation.domain.model.ApiError
import com.example.mynavigation.domain.model.Event
import com.example.mynavigation.domain.model.data.Movie
import com.example.mynavigation.domain.model.responses.AccountStates
import com.example.mynavigation.domain.usecases.GetMovieListUseCase
import com.example.mynavigation.domain.usecases.MarkFavoriteUseCase
import com.example.mynavigation.presentation.view.adapters.MovieAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


class MovieCatalogViewModel(
    private val getMovieListUseCase: GetMovieListUseCase,
    private val markFavoriteUseCase: MarkFavoriteUseCase
) : ViewModel(),
    CoroutineScope {
    private var _isFavourite = MutableLiveData<Event<Boolean>>()
    val isFavourite: LiveData<Event<Boolean>>
        get() = _isFavourite

    private var _openDetail = MutableLiveData<Event<Int>>()
    val openDetail: LiveData<Event<Int>>
        get() = _openDetail

    private var _markFavorite = MutableLiveData<Event<Int>>()
    val markFavourite: LiveData<Event<Int>>
        get() = _markFavorite

    private var _movieList = MutableLiveData<State>()
    val movieList: LiveData<State>
        get() = _movieList

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    companion object {
        var sessionID: String = ""
    }

    fun setArgs(sessionId: String) {
        sessionID = sessionId
    }

    fun getMovies() {
        _movieList.value = State.ShowLoading
        getMovieListUseCase.invoke(
            viewModelScope,
            null,
            object : UseCaseResponse<List<Movie>> {
                override fun onSuccess(result: List<Movie>) {
                    _movieList.value = State.Result(result)
                }

                override fun onError(apiError: ApiError?) {
                    //todo error handler
                }
            })
        _movieList.value = State.HideLoading
    }

    fun markFavorite(movieId: Int) {
        markFavoriteUseCase.invoke(
            viewModelScope,
            movieId,
            object : UseCaseResponse<AccountStates> {
                override fun onSuccess(result: AccountStates) {
                    if (result.favorite)
                        _isFavourite.value = Event(true)
                    else
                        _isFavourite.value = Event(false)
                }

                override fun onError(apiError: ApiError?) {
                    //todo
                }
            })
    }

    val recyclerViewItemClickListener = object : MovieAdapter.RecyclerViewItemClick {
        override fun itemClick(item: Movie) {
            item.post_id?.let {
                _openDetail.value = Event(it)
            }
        }
    }

    val recyclerViewFavItemClick = object : MovieAdapter.RecyclerViewFavItemClick {
        override fun itemClick(item: Movie) {
            item.post_id?.let {
                _markFavorite.value = Event(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class Result(val list: List<Movie>?) : State()
    }
}

