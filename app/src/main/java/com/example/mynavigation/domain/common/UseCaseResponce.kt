package com.example.mynavigation.domain.common

import com.example.mynavigation.domain.model.ApiError

interface UseCaseResponse<Type> {

    fun onSuccess(result: Type)

    fun onError(apiError: ApiError?)
}