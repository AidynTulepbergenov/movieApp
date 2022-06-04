package com.example.mynavigation.domain.usecases

import com.example.mynavigation.domain.common.BaseUseCase
import com.example.mynavigation.domain.model.data.Movie
import com.example.mynavigation.domain.repositories.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMovieDetailUseCase(private val repository: MoviesRepository) : BaseUseCase<Movie, Int>() {
    override suspend fun run(params: Int?): Movie {
        val movie = withContext(Dispatchers.IO) {
            try {
                repository.getMovie(params!!)
            } catch (e: Exception) {
                repository.getMovieDb(params!!)
            }
        }
        return movie
    }
}