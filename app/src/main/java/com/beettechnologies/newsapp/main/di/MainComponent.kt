package com.beettechnologies.newsapp.main.di

import com.beettechnologies.newsapp.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

interface MainComponent {

    @Module
    abstract class MainModule

    @Module
    abstract class MainActivityFragmentsModule {

        @ContributesAndroidInjector
        internal abstract fun mainFragment(): MainFragment
    }
}