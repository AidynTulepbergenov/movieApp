package com.example.mynavigation.domain.usecases

import com.example.mynavigation.domain.common.BaseUseCase
import com.example.mynavigation.domain.model.data.Movie
import com.example.mynavigation.domain.repositories.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSimilarMovieUseCase(private val repository: MoviesRepository) :
    BaseUseCase<List<Movie>, Int>() {

    override suspend fun run(params: Int?): List<Movie> {
        val list = withContext(Dispatchers.Default) {
            try {
                val result = repository.getSimilarMovies(params!!)
                result

            } catch (e: Exception) {
                listOf()
            }
        }
        return list
    }
}