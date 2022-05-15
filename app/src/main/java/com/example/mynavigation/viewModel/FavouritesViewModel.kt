package com.example.mynavigation.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mynavigation.model.data.Event
import com.example.mynavigation.model.data.Movie
import com.example.mynavigation.model.network.FavMovie
import com.example.mynavigation.model.network.RetrofitService
import com.example.mynavigation.view.adapters.MovieAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FavouritesViewModel(context: Context) : ViewModel(), CoroutineScope {
    private val job: Job = Job()
    private val cont = context
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    companion object{
        var sessionID: String = ""
    }

    private var _favList = MutableLiveData<State>()
    val favList: LiveData<State>
        get() = _favList

    private val _check = MutableLiveData<Boolean>()
    val check: LiveData<Boolean>
        get() = _check

    private val _markFavorite = MutableLiveData<Event<Int>>()
    val markFavourite: LiveData<Event<Int>>
        get() = _markFavorite

    private val _openDetail = MutableLiveData<Event<Int>>()
    val openDetail: LiveData<Event<Int>>
        get() = _openDetail

    fun setArgs(sessionId: String) {
        sessionID = sessionId
    }

    fun getFavList() {
        launch {
            _favList.value = State.ShowLoading
            var list: List<Movie>? = listOf()
            val response = RetrofitService.getMovieApi().getFavList(
                session_id = sessionID,
            )
            if (response.isSuccessful) {
                list = response.body()?.movies
            } else {
                Toast.makeText(
                    cont,
                    "Требуется авторизация",
                    Toast.LENGTH_SHORT
                ).show()
            }
            _favList.value = State.HideLoading
            _favList.value = State.Result(list)
        }
    }

    val recyclerViewItemClickListener = object : MovieAdapter.RecyclerViewItemClick {
        override fun itemClick(item: Movie) {
            item.post_id?.let {
                _openDetail.value = Event(it)
            }
        }
    }

//    fun checkFavorite(id: Int) {
//        launch {
//            val response =
//                RetrofitService.getMovieApi()
//                    .checkFavoriteMovie(session_id = sessionID, id = id)
//            if (response.isSuccessful)
//                _check.value = response.body()!!.favorite
//        }
//    }

    fun markFavorite(movieId: Int) {
        launch {
            val response =
                RetrofitService.getMovieApi()
                    .checkFavoriteMovie(session_id = sessionID, id = movieId)
            if (response.body()!!.favorite) {
                val markMovie = FavMovie(media_id = movieId, favourite = false)
                RetrofitService.getMovieApi().markFavorite(
                    session_id = sessionID,
                    favMovie = markMovie
                )
                Toast.makeText(cont, "Removed from \"Favorites\"", Toast.LENGTH_SHORT).show()
            } else {
                val markMovie = FavMovie(media_id = movieId, favourite = true)
                RetrofitService.getMovieApi().markFavorite(
                    session_id = sessionID,
                    favMovie = markMovie
                )
                Toast.makeText(cont, "Added to \"Favorites\"", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    fun deleteFavorites(movieId: Int) {
//        launch {
//            val favMovie = FavMovie(media_id = movieId, favourite = false)
//            RetrofitService.getMovieApi().markFavorite(
//                session_id = sessionID,
//                favMovie = favMovie
//            )
//        }
//    }

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