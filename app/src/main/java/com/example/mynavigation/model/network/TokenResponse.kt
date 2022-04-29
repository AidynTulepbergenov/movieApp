package com.example.mynavigation.model.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("success")
    private val success: Boolean,
    @SerializedName("request_token")
    private val request_token: String
    )