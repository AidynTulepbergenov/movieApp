package com.example.mynavigation.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mynavigation.model.data.Movie
import com.example.mynavigation.model.data.MovieDao
import com.example.mynavigation.model.data.MovieDatabase
import com.example.mynavigation.model.network.RetrofitService
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MovieDetailViewModel(context: Context): ViewModel(), CoroutineScope {

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job
    private var movieDao: MovieDao = MovieDatabase.getDatabase(context).getDao()
    private var id: Int = 0



    private val _liveData = MutableLiveData<Movie>()
    val liveData: LiveData<Movie>
        get() = _liveData

    fun setArgs(id: Int){
        this.id = id
    }

    fun getMovie() {
        launch {
            val movie = withContext(Dispatchers.IO) {
                try {
                    val response =
                        RetrofitService.getMovieApi().getMovieByIdC(id)
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

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}