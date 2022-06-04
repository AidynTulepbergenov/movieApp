package com.example.mynavigation.domain.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "markers")
data class Marker(
    @PrimaryKey
    var id: Int,
    val lat: Double,
    val lng: Double,
    val title: String
)

fun generateMarkers(): List<Marker> {
    val markers: MutableList<Marker> = arrayListOf()
    val marker1 = Marker(
        1,
        43.23428225465299,
        76.9358344910684,
        "Kinopark 16 Forum"
    )
    markers.add(marker1)
    val marker2 = Marker(
        2,
        43.24033637913771,
        76.90576094020143,
        "Kinopark 4 Globus"
    )
    markers.add(marker2)
    val marker3 = Marker(
        3,
        43.264188947146515,
        76.9294945555458,
        "Chaplin Cinemas(Mega)"
    )
    markers.add(marker3)
    return markers
}