package com.example.fakegpsdetector.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

fun <A, B> combineLatest(a: LiveData<A>, b: LiveData<B>): MutableLiveData<Pair<A, B>> {

    return MediatorLiveData<Pair<A, B>>().apply {
        var lastA: A? = null
        var lastB: B? = null

        fun update() {
            val localLastA = lastA
            val localLastB = lastB
            if (localLastA != null && localLastB != null)
                this.value = Pair(localLastA, localLastB)
        }

        addSource(a) {
            lastA = it
            update()
        }
        addSource(b) {
            lastB = it
            update()
        }
    }
}

fun <A, B> LiveData<A>.combineLatestWith(b: LiveData<B>): LiveData<Pair<A, B>> = combineLatest(this, b)