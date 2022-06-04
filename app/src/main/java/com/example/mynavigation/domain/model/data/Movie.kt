package com.example.mynavigation.domain.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Entity(tableName = "movies_table")
data class Movie(
    @PrimaryKey
    @SerializedName("id")
    @Expose
    val post_id: Int?,

    @SerializedName("title")
    @Expose
    var title: String?,

    @SerializedName("poster_path")
    @Expose
    var poster: String?,

    @SerializedName("overview")
    @Expose
    var overview: String?,

    var isFavorite: Boolean = false
)
