package com.example.mynavigation.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mynavigation.model.data.Movie
import com.example.mynavigation.model.data.MovieDao
import com.example.mynavigation.model.data.MovieDatabase
import com.example.mynavigation.model.network.FavMovie
import com.example.mynavigation.model.network.RetrofitService
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MovieDetailViewModel(context: Context) : ViewModel(), CoroutineScope {

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job
    private var movieDao: MovieDao = MovieDatabase.getDatabase(context).getDao()
    private var id: Int = 0


    private val _liveData = MutableLiveData<Movie>()
    val liveData: LiveData<Movie>
        get() = _liveData

    private val _check = MutableLiveData<Boolean>()
    val check: LiveData<Boolean>
        get() = _check

    private val _markFavoriteState = MutableLiveData<Boolean>()
    val markFavouritesState: LiveData<Boolean>
        get() = _markFavoriteState

    fun setArgs(id: Int) {
        this.id = id
    }

    fun getMovie() {
        launch {
            val movie = withContext(Dispatchers.IO) {
                try {
                    val response =
                        RetrofitService.getMovieApi().getMovieByIdC(id, apiKey = "")
                    if (response.isSuccessful) {
                        response.body()!!
                    } else {
                        movieDao.getMovieById(id)
                    }
                } catch (e: Exception) {
                    Log.d("error", e.toString())
                    movieDao.getMovieById(id)
                }
            }
            _liveData.value = movie
        }
    }

    fun checkFavorite(session: String, id: Int) {
        launch {
            val response =
                RetrofitService.getMovieApi().checkFavoriteMovie(session_id = session, id = id)
            if (response.isSuccessful)
                _check.value = response.body()!!.favorite
        }

    }

    fun markFavorite(movieId: Int, sessionId: String) {
        launch {
            val markMovie = FavMovie(media_id = movieId, favourite = true, media_type = "movie")
            val response = RetrofitService.getMovieApi().markFavorite(
                session_id = sessionId,

                favMovie = markMovie
            )
            _markFavoriteState.value = response.isSuccessful
        }
    }

    fun deleteFavorites(movieId: Int, sessionId: String) {
        launch {
            val favMovie = FavMovie(media_id = movieId, favourite = false)
            RetrofitService.getMovieApi().markFavorite(
                session_id = sessionId,
                favMovie = favMovie
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}