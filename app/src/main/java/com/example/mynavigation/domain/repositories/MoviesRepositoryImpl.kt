package com.example.mynavigation.domain.repositories

import com.example.mynavigation.data.db.MovieDao
import com.example.mynavigation.data.network.RetrofitService
import com.example.mynavigation.domain.model.data.Movie
import com.example.mynavigation.domain.model.responses.*
import com.example.mynavigation.presentation.viewModel.MovieCatalogViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesRepositoryImpl(
    private val database: MovieDao,
    private val service: RetrofitService
) : MoviesRepository {

    override suspend fun refreshMovies(): List<Movie> {
        return service.getMovieApi().getMovieListCoroutine().body()!!.movies
    }

    override suspend fun getSimilarMovies(movieId: Int): List<Movie> {
        return service.getMovieApi().getSimilarMovies(movieId).body()!!.movies
    }

    override suspend fun getReviews(movieId: Int): List<Comment> {
        return service.getMovieApi().getMovieReview(movieId).body()!!.results
    }

    override suspend fun getFavList(sessionId: String): List<Movie> {
        return service.getMovieApi().getFavList(session_id = sessionId).body()!!.movies
    }

    override suspend fun login(data: LoginApprove): Session {
        val session = withContext(Dispatchers.Main) {
            try {
                val responseGet = service.getMovieApi().getToken()
                val loginApprove = LoginApprove(
                    username = data.username,
                    password = data.password,
                    request_token = responseGet.body()?.request_token as String
                )
                val responseApprove =
                    service.getMovieApi().validateToken(loginApprove = loginApprove)
                val sessionResponse =
                    service.getMovieApi()
                        .createSession(token = responseApprove.body() as Token)
                sessionResponse.body()

            } catch (e: Exception) {
                Session(success = false, session_id = "", status_code = 0)
            }
        }
        return session!!
    }

    override suspend fun logout(sessionId: String): Boolean {
        return service.getMovieApi().deleteSession(session_id = sessionId).body()!!
    }

    override suspend fun getAccountDetails(sessionId: String): User {
        return service.getMovieApi().getAccountDetails(session_id = sessionId).body()!!
    }

    override suspend fun checkFavorite(movieId: Int): AccountStates {
        val accountStates = withContext(Dispatchers.Main) {
            try {
                service.getMovieApi()
                    .checkFavoriteMovie(session_id = MovieCatalogViewModel.sessionID, id = movieId)
                    .body()
            } catch (e: Exception) {
                AccountStates(id = -1, favorite = false)
            }
        }
        return accountStates!!
    }

    override suspend fun markFavorite(movieId: Int) {
        val response =
            service.getMovieApi()
                .checkFavoriteMovie(session_id = MovieCatalogViewModel.sessionID, id = movieId)
        if (response.body()!!.favorite) {
            val markMovie = FavMovie(media_id = movieId, favourite = false)
            service.getMovieApi().markFavorite(
                session_id = MovieCatalogViewModel.sessionID,
                favMovie = markMovie
            )
        } else {
            val markMovie = FavMovie(media_id = movieId, favourite = true)
            service.getMovieApi().markFavorite(
                session_id = MovieCatalogViewModel.sessionID,
                favMovie = markMovie
            )
        }
    }

    override suspend fun getMovie(id: Int): Movie {
        val response = service.getMovieApi().getMovieByIdC(id)
        return response.body()!!
    }

    override fun insertAll(list: List<Movie>) {
        return database.insertAll(list)
    }

    override fun getAll(): List<Movie> {
        return database.getMovies()
    }

    override fun getMovieDb(id: Int): Movie {
        return database.getMovieById(id)
    }

}