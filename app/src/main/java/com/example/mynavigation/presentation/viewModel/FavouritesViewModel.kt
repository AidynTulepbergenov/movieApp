package com.example.mynavigation.presentation.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynavigation.domain.model.Event
import com.example.mynavigation.domain.model.data.Movie
import com.example.mynavigation.data.network.RetrofitService
import com.example.mynavigation.domain.common.UseCaseResponse
import com.example.mynavigation.domain.model.ApiError
import com.example.mynavigation.domain.model.responses.AccountStates
import com.example.mynavigation.domain.model.responses.FavMovie
import com.example.mynavigation.domain.usecases.GetFavMovieListUseCase
import com.example.mynavigation.domain.usecases.MarkFavoriteUseCase
import com.example.mynavigation.presentation.view.adapters.MovieAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FavouritesViewModel(
    var app: Application,
    private val markFavoriteUseCase: MarkFavoriteUseCase,
    private val getFavMovieListUseCase: GetFavMovieListUseCase
) : ViewModel(), CoroutineScope {

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    companion object {
        var sessionID: String = ""
    }

    private var _favList = MutableLiveData<State>()
    val favList: LiveData<State>
        get() = _favList

    private var _isFavourite = MutableLiveData<Event<Boolean>>()
    val isFavourite: LiveData<Event<Boolean>>
        get() = _isFavourite

    private val _markFavorite = MutableLiveData<Event<Int>>()
    val markFavourite: LiveData<Event<Int>>
        get() = _markFavorite

    private val _openDetail = MutableLiveData<Event<Int>>()
    val openDetail: LiveData<Event<Int>>
        get() = _openDetail

    fun setArgs(sessionId: String) {
        sessionID = sessionId
    }

    val recyclerViewItemClickListener = object : MovieAdapter.RecyclerViewItemClick {
        override fun itemClick(item: Movie) {
            item.post_id?.let {
                _openDetail.value = Event(it)
            }
        }
    }

    fun getFavList() {
        _favList.value = State.ShowLoading
        getFavMovieListUseCase.invoke(
            viewModelScope,
            sessionID,
            object : UseCaseResponse<List<Movie>> {
                override fun onSuccess(result: List<Movie>) {
                    _favList.value = State.Result(result)
                }

                override fun onError(apiError: ApiError?) {
                    TODO("Not yet implemented")
                }
            })
        _favList.value = State.HideLoading
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