package com.example.mynavigation.domain.usecases

import com.example.mynavigation.domain.common.BaseUseCase
import com.example.mynavigation.domain.repositories.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class LogoutUseCase(private val repository: MoviesRepository): BaseUseCase<Any, String>() {
    override suspend fun run(params: String?) {
        repository.logout(params!!)
    }

}