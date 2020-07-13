package com.dariusz.fakegpsdetector.utils

import android.util.Log
import com.dariusz.fakegpsdetector.model.ErrorContent

object ErrorHandling {

    fun handleError(errorType: String, errorMessage: String): ErrorContent {
        val result = ErrorContent(errorType, errorMessage)
        return result.also { errorAsLog(it) }
    }

    private fun errorAsLog(errorHandle: ErrorContent): Int {
        return Log.e("APPLICATION-DEBUG-ERROR: ", "$errorHandle")
    }
}
