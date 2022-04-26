package com.example.mynavigation.model.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "movies_table")
data class Movie (
    @PrimaryKey
    @SerializedName("id")
    val post_id: Int?,

    @SerializedName("title")
    var title: String?,

    @SerializedName("poster_path")
    var poster: String?,

    @SerializedName("overview")
    var overview: String?

) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(post_id)
        parcel.writeString(title)
        parcel.writeString(poster)
        parcel.writeString(overview)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}