package com.aluengo.cleancomposerickandmorty.core.ui

import android.content.Context
import com.aluengo.cleancomposerickandmorty.R
import com.aluengo.cleancomposerickandmorty.core.data.ErrorType

class ErrorMapper(private val context: Context) {
    fun map(errorType: ErrorType?): String {
        return when (errorType) {
            is ErrorType.Default -> {
                context.getString(R.string.error_default)
            }

            is ErrorType.NotConnected -> {
                context.getString(R.string.error_not_connected)
            }

            is ErrorType.NotFound -> {
                context.getString(R.string.error_not_found)
            }

            null -> {
                context.getString(R.string.error_default)
            }
        }
    }

}
