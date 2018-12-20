package com.beettechnologies.newsapp.common.data

import okhttp3.Interceptor
import okhttp3.Response

private const val PARAM_API_ID: String = "apiKey"
private const val PARAM_CACHE: String = "Cache-Control"

class ApiInterceptor(private val apiKey: String, private val cacheDuration: Int) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val url = request.url().newBuilder()
            .addQueryParameter(PARAM_API_ID, apiKey)
            .build()

        val newRequest = request.newBuilder()
            .url(url)
            .addHeader(PARAM_CACHE, "public, max-age=$cacheDuration")
            .build()

        return chain.proceed(newRequest)
    }
}