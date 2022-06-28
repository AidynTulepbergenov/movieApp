package com.example.mynavigation.domain.model.responses

data class ReviewResponse(
    val id: Int,
    val results: List<Comment>,
    val page: Int
)