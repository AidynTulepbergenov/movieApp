package com.example.mynavigation.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mynavigation.domain.model.data.Movie

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieList(list: List<Movie>)

    @Query("SELECT * FROM movies_table")
    fun getMovies(): List<Movie>

    @Query("Select * from movies_table where post_id = :id")
    fun getMovieById(id: Int): Movie

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(movie: Movie)

    @Query("SELECT * FROM movies_table WHERE isFavorite = 1")
    fun getFavMovies(): LiveData<List<Movie>>

    @Query("UPDATE movies_table SET isFavorite = 1 WHERE post_id = :id")
    suspend fun markFavorite(id: Int)

    @Query("UPDATE movies_table SET isFavorite = 0 WHERE post_id = :id")
    suspend fun deleteFavorites(id: Int)
}