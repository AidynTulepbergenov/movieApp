package com.example.mynavigation.domain.usecases

import com.example.mynavigation.domain.common.BaseUseCase
import com.example.mynavigation.domain.model.responses.Comment
import com.example.mynavigation.domain.repositories.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMovieReviewUseCase(private val repository: MoviesRepository): BaseUseCase<List<Comment>, Int>() {


    override suspend fun run(params: Int?): List<Comment> {
        val list = withContext(Dispatchers.Default) {
            try {
                val result = repository.getReviews(params!!)
                result

            } catch (e: Exception) {
                listOf()
            }
        }
        return list
    }
}