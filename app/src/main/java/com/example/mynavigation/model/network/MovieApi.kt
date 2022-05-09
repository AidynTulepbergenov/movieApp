package com.example.mynavigation.model.network

import com.example.mynavigation.model.MovieResponse
import com.example.mynavigation.model.data.Movie
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
    fun getMovieByIdC(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String
    ): Response<Movie>

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

    @DELETE("authentication/session?session_id={sessionId}?api_key=a8800d6d40188783fd088998d7926c03")
    suspend fun deleteSession(@Path("session_id") sessionId: String): Response<Boolean>

}