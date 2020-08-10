package com.dariusz.fakegpsdetector.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.Job

object ViewUtils {

    fun performActionInsideCoroutine(
        lifecycleOwner: LifecycleOwner,
        action: suspend () -> Unit
    ): Job {
        return lifecycleOwner.lifecycle.coroutineScope.launchWhenCreated {
            action.invoke()
        }
    }
}
