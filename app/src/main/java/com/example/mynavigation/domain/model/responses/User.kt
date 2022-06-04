package com.example.mynavigation.domain.model.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("username")
    @Expose
    var username: String,
    @SerializedName("name")
    @Expose
    var name: String
)
