package com.beettechnologies.newsapp.common.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.beettechnologies.newsapp.common.data.model.News

/**
 * Main database description.
 */
@Database(
    entities =
    [
        News::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NewsDb : RoomDatabase() {

    abstract fun newsDao(): NewsDao
}