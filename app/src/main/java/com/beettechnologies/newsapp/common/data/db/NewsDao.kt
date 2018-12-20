package com.beettechnologies.newsapp.common.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.beettechnologies.newsapp.common.data.model.News
import io.reactivex.Flowable

@Dao
abstract class NewsDao {

    @Query("""SELECT * FROM News WHERE source = :source""")
    abstract fun getNews(source: String): Flowable<List<News>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg news: News)
}