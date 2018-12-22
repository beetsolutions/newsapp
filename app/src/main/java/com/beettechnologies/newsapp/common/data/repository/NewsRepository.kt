package com.beettechnologies.newsapp.common.data.repository

import com.beettechnologies.newsapp.common.data.AppSchedulers
import com.beettechnologies.newsapp.common.data.api.ApiService
import com.beettechnologies.newsapp.common.data.db.NewsDao
import com.beettechnologies.newsapp.common.data.entity.ArticleResponse
import com.beettechnologies.newsapp.common.data.model.News
import com.beettechnologies.newsapp.common.data.model.Resource
import io.reactivex.Flowable
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(val schedulers: AppSchedulers, val api: ApiService, val newsDao: NewsDao) {

    fun getNews(query: String, isNetworkAvailable: Boolean): Flowable<Resource<MutableList<News>>> {
        return object : RxNetworkBoundResource<MutableList<News>, ArticleResponse>(isNetworkAvailable, schedulers) {

            override fun saveCallResult(request: ArticleResponse) {
                request.articles.forEach {
                    val news = News(
                        id = 0,
                        source = it.source.id,
                        title = it.title,
                        description = it.description,
                        url = it.url,
                        urlToImage = it.urlToImage,
                        publishedAt = it.publishedAt,
                        content = it.content
                    )
                    newsDao.insert(news)
                }
            }

            override fun loadFromDb(): Flowable<MutableList<News>> = newsDao.getNews(query)

            override fun createCall(): Flowable<Response<ArticleResponse>> = api.queryReddit(query)
        }.asFlowAble()
    }
}