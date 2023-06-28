package com.aluengo.cleancomposerickandmorty.core.data

import androidx.annotation.Keep

@Keep
data class ErrorData(
    val error: String? = null,
    @Transient
    var errorType: ErrorType?
)

data class ErrorResponse(
    val statusCode: Int? = 0,
    val errorData: ErrorData?
){
    val errorType: ErrorType get() = errorData?.errorType ?: ErrorType.Default
}

sealed class ErrorType {
    object Default : ErrorType()
    object NotConnected : ErrorType()
    object NotFound : ErrorType()
}