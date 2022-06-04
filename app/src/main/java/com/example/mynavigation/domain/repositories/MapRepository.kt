package com.example.mynavigation.domain.repositories

import com.example.mynavigation.domain.model.data.Marker

interface MapRepository {
    fun insertAll(markers: List<Marker>)

    fun getMarkers(): List<Marker>

    fun deleteAll()
}