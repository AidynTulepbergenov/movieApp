package com.example.mynavigation.domain.model.responses

import com.example.mynavigation.domain.model.data.Movie
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("results")
    @Expose
    val movies: List<Movie>
)