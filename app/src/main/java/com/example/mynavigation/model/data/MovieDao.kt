package com.example.mynavigation.model.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieList(list: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(movie: Movie)

    @Query("SELECT * FROM movies_table")
    suspend fun readAllData(): List<Movie>

    @Query("Select title, poster, overview  from movies_table where post_id = :id")
    suspend fun getMovieById(id: Int): Movie
}