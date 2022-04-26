package com.example.mynavigation.model.network

import com.example.mynavigation.model.MovieResponse
import com.example.mynavigation.model.data.Movie
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApi {
    @GET("movie/popular?api_key=a8800d6d40188783fd088998d7926c03")
    fun getMovieList(): Call<MovieResponse>

    @GET("movie/{id}?api_key=a8800d6d40188783fd088998d7926c03")
    fun getMovieById(@Path("id") id: Int): Call<Movie>

    @GET("movie/popular?api_key=a8800d6d40188783fd088998d7926c03")
    suspend fun getMovieListCoroutine(): Response<MovieResponse>

    @GET("movie/{id}?api_key=a8800d6d40188783fd088998d7926c03")
    suspend fun getMovieByIdC(@Path("id") id: Int): Response<Movie>
}