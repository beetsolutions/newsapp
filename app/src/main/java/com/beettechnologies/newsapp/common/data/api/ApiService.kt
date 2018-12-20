package com.beettechnologies.newsapp.common.data.api

import com.beettechnologies.newsapp.common.data.entity.ArticleResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("top-headlines")
    fun queryReddit(@Query("sources") sources: String): Flowable<ArticleResponse>
}