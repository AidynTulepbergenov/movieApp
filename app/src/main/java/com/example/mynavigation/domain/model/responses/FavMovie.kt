package com.example.mynavigation.domain.model.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FavMovie(
    @SerializedName("media_type")
    @Expose
    val media_type: String = "movie",
    @SerializedName("media_id")
    @Expose
    val media_id: Int,
    @SerializedName("favorite")
    @Expose
    val favourite: Boolean
)