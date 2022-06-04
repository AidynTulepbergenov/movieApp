package com.example.mynavigation.domain.model.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Session(
    @SerializedName("session_id")
    @Expose
    var session_id: String,
    @SerializedName("success")
    @Expose
    var success: Boolean,
    @SerializedName("status_code")
    @Expose
    var status_code: Int
)