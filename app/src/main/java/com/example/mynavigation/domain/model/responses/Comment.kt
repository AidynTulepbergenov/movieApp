package com.example.mynavigation.domain.model.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("author")
    @Expose
    val author: String,
    @SerializedName("content")
    @Expose
    val content: String,
    @SerializedName("created_at")
    @Expose
    val created_at: String
)