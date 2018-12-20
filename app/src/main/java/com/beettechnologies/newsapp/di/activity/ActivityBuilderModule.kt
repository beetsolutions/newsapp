package com.beettechnologies.newsapp.di.activity

import android.arch.lifecycle.ViewModelProvider
import com.beettechnologies.newsapp.di.viewmodel.ViewModelFactory
import com.beettechnologies.newsapp.main.MainActivity
import com.beettechnologies.newsapp.main.di.MainComponent
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ActivityBuilderModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector(modules = [
        MainComponent.MainModule::class,
        MainComponent.MainActivityFragmentsModule::class
    ])
    internal abstract fun contributeMainActivity(): MainActivity
}