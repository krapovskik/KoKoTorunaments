package com.sorsix.koko.dto.response

sealed interface Response

data class SuccessResponse<T>(val response: T) : Response
data class NotFoundResponse(val error: String): Response
data class BadRequestResponse(val error: String): Response