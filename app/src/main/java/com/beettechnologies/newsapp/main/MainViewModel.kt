package com.beettechnologies.newsapp.main

import android.arch.lifecycle.*
import com.beettechnologies.newsapp.App
import com.beettechnologies.newsapp.common.data.model.News
import com.beettechnologies.newsapp.common.data.model.Resource
import com.beettechnologies.newsapp.common.data.repository.NewsRepository
import com.beettechnologies.newsapp.util.AbsentLiveData
import com.beettechnologies.newsapp.util.isConnected
import javax.inject.Inject

class MainViewModel @Inject constructor(app: App, repository: NewsRepository) : ViewModel() {

    private val source: MutableLiveData<String> = MutableLiveData()

    val newsResourceLiveData: LiveData<Resource<MutableList<News>>> = Transformations
        .switchMap(source) { source ->
            if (source == null) {
                AbsentLiveData.create()
            } else {
                val fromPublisher = LiveDataReactiveStreams.fromPublisher(repository.getNews(source, app.isConnected()))

                fromPublisher
            }
        }

    fun setSource(source: String?) {
        if (this.source.value != source) {
            this.source.value = source
        }
    }
}