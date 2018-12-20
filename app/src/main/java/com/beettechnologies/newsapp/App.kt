package com.beettechnologies.newsapp

import com.beettechnologies.newsapp.di.AppLifecycleCallbacks
import com.beettechnologies.newsapp.di.DaggerAppComponent
import com.beettechnologies.newsapp.di.applyAutoInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject

class App : DaggerApplication() {

    @Inject lateinit var appLifecycleCallbacks: AppLifecycleCallbacks

    override fun applicationInjector() = DaggerAppComponent.builder()
        .application(this)
        .build()

    override fun onCreate() {
        super.onCreate()
        applyAutoInjector()
        appLifecycleCallbacks.onCreate(this)
    }

    override fun onTerminate() {
        appLifecycleCallbacks.onTerminate(this)
        super.onTerminate()
    }
}