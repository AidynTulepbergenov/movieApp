package com.example.mynavigation.domain.usecases

import com.example.mynavigation.domain.common.BaseUseCase
import com.example.mynavigation.domain.model.responses.User
import com.example.mynavigation.domain.repositories.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class GetAccountDetailUseCase(private val repository: MoviesRepository) :
    BaseUseCase<User, String>() {
    override suspend fun run(params: String?): User {
        val user = withContext(Dispatchers.Main) {
            try {
                repository.getAccountDetails(params!!)
            } catch (e: Exception) {
                User("Def", "ault")
            }
        }
        return user
    }

}