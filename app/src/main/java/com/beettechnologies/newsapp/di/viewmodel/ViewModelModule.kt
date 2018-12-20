package com.beettechnologies.newsapp.di.viewmodel

import android.arch.lifecycle.ViewModel
import com.beettechnologies.newsapp.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}