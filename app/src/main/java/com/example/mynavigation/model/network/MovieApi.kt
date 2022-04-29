package com.example.mynavigation.model.network

import com.example.mynavigation.model.MovieResponse
import com.example.mynavigation.model.data.Movie
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MovieApi {
    @GET("movie/popular?api_key=a8800d6d40188783fd088998d7926c03")
    suspend fun getMovieListCoroutine(): Response<MovieResponse>

    @GET("movie/{id}?api_key=a8800d6d40188783fd088998d7926c03")
    suspend fun getMovieByIdC(@Path("id") id: Int): Response<Movie>

    @GET("authentication/token/new?api_key=a8800d6d40188783fd088998d7926c03")
    suspend fun getToken(): Response<TokenResponse>

    @POST("authentication/token/validate_with_login?username={login}?password={pass}?request_token={request_token}?api_key=a8800d6d40188783fd088998d7926c03")
    suspend fun validateToken(
        @Path("username") username: String,
        @Path("password") pass: String,
        @Path("request_token") request_token: String
    ): Response<TokenResponse>

    @POST("authentication/session/new?request_token={request_token}?api_key=a8800d6d40188783fd088998d7926c03")
    suspend fun createSession(@Path("request_token") request_token: String): Response<Session>

    @DELETE("authentication/session?session_id={sessionId}")
    suspend fun deleteSession(@Path("session_id") sessionId: String): Response<Boolean>

}