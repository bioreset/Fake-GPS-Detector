package com.dariusz.fakegpsdetector.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.Job

object ViewUtils {

    fun <T> performActionInsideCoroutineWithLiveData(
        liveData: LiveData<T>,
        lifecycleOwner: LifecycleOwner,
        actionInCoroutine: suspend (T?) -> Unit?,
        actionOnMain: (T?) -> Unit?
    ) {
        return liveData.observe(
            lifecycleOwner,
            Observer {
                lifecycleOwner.lifecycle.coroutineScope.launchWhenCreated {
                    actionInCoroutine.invoke(liveData.value)
                }
                actionOnMain.invoke(liveData.value)
            }
        )
    }

    fun performActionInsideCoroutine(
        lifecycleOwner: LifecycleOwner,
        action: suspend () -> Unit
    ): Job {
        return lifecycleOwner.lifecycle.coroutineScope.launchWhenCreated {
            action.invoke()
        }
    }

    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(
            lifecycleOwner,
            object : Observer<T> {
                override fun onChanged(t: T?) {
                    observer.onChanged(t)
                    removeObserver(this)
                }
            }
        )
    }
}
