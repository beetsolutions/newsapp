package com.beettechnologies.newsapp.di

import android.arch.persistence.room.Room
import com.beettechnologies.newsapp.App
import com.beettechnologies.newsapp.BuildConfig
import com.beettechnologies.newsapp.common.data.ApiInterceptor
import com.beettechnologies.newsapp.common.data.api.ApiService
import com.beettechnologies.newsapp.common.data.db.NewsDao
import com.beettechnologies.newsapp.common.data.db.NewsDb
import com.beettechnologies.newsapp.di.viewmodel.ViewModelModule
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL: String = "https://newsapi.org/v2/"
private const val API_KEY: String = "86f976d6ff2b443d8f25de83567d9169"
private const val DATABASE: String = "news.db"

@Module(includes = [ViewModelModule::class])
internal object AppModule {

    @Singleton
    @Provides
    @JvmStatic
    fun provideAppLifecycleCallbacks(): AppLifecycleCallbacks = DebugAppLifecycleCallbacks()

    @Singleton
    @Provides
    @JvmStatic
    fun provideApiService(client: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    @JvmStatic
    fun provideOkHttpClient(cache: Cache, interceptor: ApiInterceptor, app: App): OkHttpClient {
        return OkHttpClient().newBuilder()
            .cache(cache)
            .addInterceptor(interceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            })
            .build()
    }

    @Singleton
    @Provides
    @JvmStatic
    fun provideRequestInterceptor(): ApiInterceptor {
        return ApiInterceptor(API_KEY, 86400)
    }

    @Singleton
    @Provides
    @JvmStatic
    fun provideCache(context: App) = Cache(context.cacheDir, 10 * 1024 * 1024.toLong())

    @Singleton
    @Provides
    @JvmStatic
    fun provideDb(app: App): NewsDb {
        return Room
            .databaseBuilder(app, NewsDb::class.java, DATABASE)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    @JvmStatic
    fun provideNewsDao(db: NewsDb): NewsDao {
        return db.newsDao()
    }
}