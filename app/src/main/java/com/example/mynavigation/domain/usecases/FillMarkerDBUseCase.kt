package com.example.mynavigation.domain.usecases

import com.example.mynavigation.domain.common.BaseUseCase
import com.example.mynavigation.domain.model.data.generateMarkers
import com.example.mynavigation.domain.repositories.MapRepository

class FillMarkerDBUseCase(private val repository: MapRepository) : BaseUseCase<Any, Any>() {
    override suspend fun run(params: Any?): Any {
        repository.deleteAll()
        return repository.insertAll(generateMarkers())
    }
}