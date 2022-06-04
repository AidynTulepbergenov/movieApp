package com.example.mynavigation.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mynavigation.domain.model.data.Marker
import com.example.mynavigation.domain.model.data.Movie

@Database(entities = [Movie::class, Marker::class], version = 3, exportSchema = false)
abstract class MovieDatabase : RoomDatabase(){

    abstract fun getDao(): MovieDao
    abstract fun getMarkDao(): MarkerDao

    companion object{
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase {
            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie_database"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }
    }

}