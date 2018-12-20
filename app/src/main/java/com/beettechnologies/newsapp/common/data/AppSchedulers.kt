package com.beettechnologies.newsapp.common.data

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class AppSchedulers @Inject constructor() {

    open fun io(): Scheduler {
        return Schedulers.io()
    }

    open fun main(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    open fun disk(): Scheduler {
        return Schedulers.computation()
    }
}