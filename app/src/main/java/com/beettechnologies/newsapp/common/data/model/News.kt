package com.beettechnologies.newsapp.common.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class News(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val source: String? = null,
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String? = null,
    val content: String? = null
)