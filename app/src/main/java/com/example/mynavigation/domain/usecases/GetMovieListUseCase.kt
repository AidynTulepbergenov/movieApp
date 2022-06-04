package com.example.mynavigation.domain.usecases

import com.example.mynavigation.domain.common.BaseUseCase
import com.example.mynavigation.domain.model.data.Movie
import com.example.mynavigation.domain.repositories.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMovieListUseCase(private val repository: MoviesRepository) :
    BaseUseCase<List<Movie>, Any>() {

    override suspend fun run(params: Any?): List<Movie> {
        val list = withContext(Dispatchers.IO) {
            try {
                val result = repository.refreshMovies()
                if (!result.isNullOrEmpty()) {
                    repository.insertAll(result)
                }
                result
            } catch (e: Exception) {
                repository.getAll()
            }
        }
        return list
    }

}