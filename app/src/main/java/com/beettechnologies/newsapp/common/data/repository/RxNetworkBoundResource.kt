package com.beettechnologies.newsapp.common.data.repository

import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import com.beettechnologies.newsapp.common.data.AppSchedulers
import com.beettechnologies.newsapp.common.data.model.Resource
import io.reactivex.Flowable
import io.reactivex.exceptions.Exceptions
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */

@Suppress("UNCHECKED_CAST")
abstract class RxNetworkBoundResource<ResultType, RequestType>
@MainThread constructor(isNetworkAvailable: Boolean, private val appSchedulers: AppSchedulers) {

    private val result: Flowable<Resource<ResultType>>

    init {
        // Lazy disk observable.
        val diskObservable = Flowable.defer {
            // Read from disk on Computation Scheduler
            loadFromDb().subscribeOn(appSchedulers.disk())
        }

        // Lazy network observable.
        val networkObservable = Flowable.defer {
            createCall()
                // Request API on IO Scheduler
                .subscribeOn(appSchedulers.io())
                // Read/Write to disk on Computation Scheduler
                .observeOn(appSchedulers.disk())
                .doOnNext { response: Response<RequestType> ->
                    if (response.isSuccessful) {
                        saveCallResult(processResponse(response))
                    }
                }
                .onErrorReturn { throwable: Throwable ->
                    when (throwable) {
                        is HttpException -> {
                            throw Exceptions.propagate(Throwable("No server connectivity error"))
                        }
                        is IOException -> {
                            throw Exceptions.propagate(Throwable("No network connectivity error"))
                        }
                        else -> {
                            throw Exceptions.propagate(Throwable("unexpected error"))
                        }
                    }
                }
                .flatMap {
                    loadFromDb()
                }
        }

        result = when {
            isNetworkAvailable -> networkObservable
                .map<Resource<ResultType>> {
                    Resource.success(it)
                }
                .onErrorReturn { throwable ->
                    onFetchFailed()
                    val value = throwable as ResultType
                    Resource.error(throwable.message!!, value)
                }
                // Read results in Android Main Thread (UI)
                .observeOn(appSchedulers.main())
                .startWith(Resource.loading())
            else -> diskObservable
                .map<Resource<ResultType>> {
                    Resource.success(it)
                }
                .onErrorReturn { throwable ->
                    val value = throwable as ResultType
                    Resource.error(throwable.message!!, value)
                }
                // Read results in Android Main Thread (UI)
                .observeOn(appSchedulers.main())
                .startWith(Resource.loading())
        }
    }

    protected open fun onFetchFailed() {}

    fun asFlowAble(): Flowable<Resource<ResultType>> = result

    @WorkerThread
    private fun processResponse(response: Response<RequestType>): RequestType = response.body()!!

    @WorkerThread
    protected abstract fun saveCallResult(request: RequestType)

    @MainThread
    protected abstract fun loadFromDb(): Flowable<ResultType>

    @MainThread
    protected abstract fun createCall(): Flowable<Response<RequestType>>
}
