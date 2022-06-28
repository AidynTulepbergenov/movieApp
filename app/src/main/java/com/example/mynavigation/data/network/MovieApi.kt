package com.example.mynavigation.data.network

import com.example.mynavigation.domain.model.data.Movie
import com.example.mynavigation.domain.model.responses.User
import com.example.mynavigation.domain.model.responses.*
import retrofit2.Response
import retrofit2.http.*

interface MovieApi {
    companion object {
        private var SESSION_ID = ""
        private var API_KEY = "a8800d6d40188783fd088998d7926c03"
    }

    @GET("movie/popular")
    suspend fun getMovieListCoroutine(@Query("api_key") api_key: String = API_KEY): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieByIdC(
        @Path("movie_id") id: Int?,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<Movie>

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReview(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<ReviewResponse>

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") id: Int ,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MovieResponse>


    @GET("authentication/token/new")
    suspend fun getToken(@Query("api_key") api_key: String = API_KEY): Response<Token>

    @POST("authentication/token/validate_with_login")
    suspend fun validateToken(
        @Query("api_key") apiKey: String = API_KEY,
        @Body loginApprove: LoginApprove
    ): Response<Token>

    @POST("authentication/session/new")
    suspend fun createSession(
        @Query("api_key") apiKey: String = API_KEY,
        @Body token: Token
    ): Response<Session>

    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    suspend fun deleteSession(
        @Query("api_key") apiKey: String = API_KEY,
        @Body session_id: String
    ): Response<Boolean>

    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavList(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("session_id") session_id: String = SESSION_ID,
    ): Response<MovieResponse>

    @POST("account/{account_id}/favorite")
    suspend fun markFavorite(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("session_id") session_id: String = SESSION_ID,
        @Body favMovie: FavMovie
    ): Response<FavResponse>

    @GET("movie/{movie_id}/account_states")
    suspend fun checkFavoriteMovie(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("session_id") session_id: String = SESSION_ID,
    ): Response<AccountStates>

    @GET("account")
    suspend fun getAccountDetails(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("session_id") session_id: String = SESSION_ID
    ): Response<User>
}