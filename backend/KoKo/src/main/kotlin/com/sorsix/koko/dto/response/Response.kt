package com.sorsix.koko.dto.response

sealed interface Response

data class SuccessResponse<T>(val response: T) : Response
data class NotFoundResponse(val message: String): Response
data class BadRequestResponse(val message: String): Response
