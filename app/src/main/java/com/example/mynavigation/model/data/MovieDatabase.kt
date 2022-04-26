package com.example.mynavigation.model.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.coroutines.CoroutineContext

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase(){

    abstract fun getDao(): MovieDao

    companion object{
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase{
            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie_database"
                )
                    .build()
            }
            return INSTANCE!!
        }
    }

}