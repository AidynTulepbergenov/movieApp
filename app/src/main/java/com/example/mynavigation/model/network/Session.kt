package com.example.mynavigation.model.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Session(
    @SerializedName("request_token")
    @Expose
    var session_id: String
)