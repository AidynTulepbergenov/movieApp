package com.example.mynavigation.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.mynavigation.domain.model.data.Marker

@Dao
interface MarkerDao {
    @Insert(onConflict = REPLACE)
    fun insertAll(markers: List<Marker>)

    @Query("SELECT * FROM markers")
    fun getMarkers(): List<Marker>

    @Query("DELETE FROM markers")
    fun deleteAll()
}