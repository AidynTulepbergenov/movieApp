package com.example.mynavigation.domain.usecases

import com.example.mynavigation.domain.common.BaseUseCase
import com.example.mynavigation.domain.model.responses.LoginApprove
import com.example.mynavigation.domain.model.responses.Session
import com.example.mynavigation.domain.repositories.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class LoginUseCase(private val repository: MoviesRepository) :
    BaseUseCase<Session, LoginApprove>() {
    override suspend fun run(params: LoginApprove?): Session {
        val session = withContext(Dispatchers.Main){
            try {
                repository.login(params!!)
            } catch (e: Exception){

            }
        }
        return session as Session
    }

}