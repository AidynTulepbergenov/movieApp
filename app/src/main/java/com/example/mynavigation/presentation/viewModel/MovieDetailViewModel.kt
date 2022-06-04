package com.example.mynavigation.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynavigation.data.network.RetrofitService
import com.example.mynavigation.domain.common.UseCaseResponse
import com.example.mynavigation.domain.model.ApiError
import com.example.mynavigation.domain.model.Event
import com.example.mynavigation.domain.model.data.Movie
import com.example.mynavigation.domain.model.responses.AccountStates
import com.example.mynavigation.domain.model.responses.FavMovie
import com.example.mynavigation.domain.usecases.GetMovieDetailUseCase
import com.example.mynavigation.domain.usecases.MarkFavoriteUseCase
import com.example.mynavigation.presentation.viewModel.MovieCatalogViewModel.Companion.sessionID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MovieDetailViewModel(private val getMovieDetailUseCase: GetMovieDetailUseCase, private val markFavoriteUseCase: MarkFavoriteUseCase) : ViewModel(),
    CoroutineScope {

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job
    private var id: Int = 0

    private var _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean>
        get() = _isFavourite

    private var _movieData = MutableLiveData<Movie>()
    val movieData: LiveData<Movie>
        get() = _movieData

    fun setArgs(id: Int) {
        this.id = id
    }

    fun getMovie() {
        getMovieDetailUseCase.invoke(viewModelScope, id, object : UseCaseResponse<Movie> {
            override fun onSuccess(result: Movie) {
                _movieData.value = result
            }

            override fun onError(apiError: ApiError?) {
                //todo
            }
        })
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

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}