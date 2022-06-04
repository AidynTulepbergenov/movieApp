package com.example.mynavigation.domain.repositories

import com.example.mynavigation.data.db.MarkerDao
import com.example.mynavigation.domain.model.data.Marker

class MapRepositoryImpl(private val markerDao: MarkerDao): MapRepository {
    override fun insertAll(markers: List<Marker>) {
        markerDao.insertAll(markers)
    }

    override fun getMarkers(): List<Marker> {
        return markerDao.getMarkers()
    }

    override fun deleteAll() {
        markerDao.deleteAll()
    }
}