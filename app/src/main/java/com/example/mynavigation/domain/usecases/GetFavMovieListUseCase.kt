package com.example.mynavigation.domain.usecases

import com.example.mynavigation.domain.common.BaseUseCase
import com.example.mynavigation.domain.model.data.Movie
import com.example.mynavigation.domain.repositories.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class GetFavMovieListUseCase(private val repository: MoviesRepository) :
    BaseUseCase<List<Movie>, String>() {
    override suspend fun run(params: String?): List<Movie> {
        val favList = withContext(Dispatchers.Main) {
            try {
                repository.getFavList(params!!)
            } catch (e: Exception) {
                listOf<Movie>()
            }
        }
        return favList
    }
}