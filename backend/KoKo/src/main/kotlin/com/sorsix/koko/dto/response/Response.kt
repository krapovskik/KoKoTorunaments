package com.sorsix.koko.dto.response

sealed interface Response

data class SuccessResponse<T>(val response: T) : Response
data class ErrorResponse(val message: String): Response
