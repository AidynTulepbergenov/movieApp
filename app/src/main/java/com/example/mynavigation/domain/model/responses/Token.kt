package com.example.mynavigation.domain.model.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("request_token")
    @Expose
    val request_token: String
)
