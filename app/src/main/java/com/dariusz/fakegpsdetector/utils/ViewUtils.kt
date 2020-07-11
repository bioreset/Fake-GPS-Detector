package com.dariusz.fakegpsdetector.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object ViewUtils {

    fun <T> showOnFragment(
        liveData: LiveData<T>,
        lifecycleOwner: LifecycleOwner,
        actionInCoroutine: suspend (T?) -> Unit,
        actionOnMain: (T?) -> Unit
    ) {
        return liveData.observe(lifecycleOwner, Observer {
            lifecycleOwner.lifecycle.coroutineScope.launch {
                actionInCoroutine.invoke(liveData.value)
            }
            actionOnMain.invoke(liveData.value)
        })
    }

    fun performActionInsideCoroutine(
        lifecycleOwner: LifecycleOwner,
        action: suspend () -> Unit
    ): Job {
        return lifecycleOwner.lifecycle.coroutineScope.launch {
            action.invoke()
        }
    }

}