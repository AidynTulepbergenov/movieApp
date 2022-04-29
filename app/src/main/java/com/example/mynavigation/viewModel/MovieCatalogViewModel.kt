package com.example.mynavigation.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mynavigation.model.data.Event
import com.example.mynavigation.model.data.Movie
import com.example.mynavigation.model.data.MovieDao
import com.example.mynavigation.model.data.MovieDatabase
import com.example.mynavigation.model.network.RetrofitService
import com.example.mynavigation.view.adapters.MovieAdapter
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext


class MovieCatalogViewModel(context: Context): ViewModel(), CoroutineScope {

    private val _movieList = MutableLiveData<State>()
    val movieList: LiveData<State>
        get() = _movieList

    private val _openDetail = MutableLiveData<Event<Int>>()
    val openDetail: LiveData<Event<Int>>
        get() = _openDetail

    private var movieDao: MovieDao = MovieDatabase.getDatabase(context).getDao()
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    fun getMovies(){
        launch {
            _movieList.value = State.ShowLoading
            val list = withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitService.getMovieApi().getMovieListCoroutine()
                    if (response.isSuccessful) {
                        val result = response.body()!!.movies
                        if(!result.isNullOrEmpty()) {
                            movieDao.addMovieList(response.body()!!.movies)
                        }
                        result
                    } else {
                        movieDao.readAllData()
                    }
                } catch (e: Exception) {
                    movieDao.readAllData()
                }
            }
            _movieList.value = State.HideLoading
            _movieList.value = State.Result(list)
        }
    }

    val recyclerViewItemClickListener = object: MovieAdapter.RecyclerViewItemClick {
        override fun itemClick(item: Movie) {
            item.post_id?.let {
                _openDetail.value = Event(it)
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

