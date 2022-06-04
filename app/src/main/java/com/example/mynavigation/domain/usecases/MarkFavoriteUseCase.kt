package com.example.mynavigation.domain.usecases

import com.example.mynavigation.domain.common.BaseUseCase
import com.example.mynavigation.domain.model.responses.AccountStates
import com.example.mynavigation.domain.model.responses.FavResponse
import com.example.mynavigation.domain.repositories.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class MarkFavoriteUseCase(private val repository: MoviesRepository): BaseUseCase<AccountStates, Int>() {
    override suspend fun run(params: Int?): AccountStates {
        val favResponse = withContext(Dispatchers.Main){
            try {
                repository.markFavorite(params!!)
                repository.checkFavorite(params)
            } catch (e: Exception){

            }
        }
        return favResponse as AccountStates
    }
}