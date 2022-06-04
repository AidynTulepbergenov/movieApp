package com.example.mynavigation.domain.repositories

import com.example.mynavigation.domain.model.data.Movie
import com.example.mynavigation.domain.model.responses.*

interface MoviesRepository {

    suspend fun refreshMovies(): List<Movie>

    suspend fun getFavList(sessionId: String): List<Movie>

    suspend fun login(data: LoginApprove): Session

    suspend fun logout(sessionId: String): Boolean

    suspend fun getAccountDetails(sessionId: String): User

    suspend fun checkFavorite(movieId: Int): AccountStates

    suspend fun markFavorite(movieId: Int)

    suspend fun getMovie(id: Int): Movie

    fun insertAll(list: List<Movie>)

    fun getAll(): List<Movie>

    fun getMovieDb(id: Int): Movie
}