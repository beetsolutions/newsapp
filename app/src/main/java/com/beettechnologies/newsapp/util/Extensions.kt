package com.beettechnologies.newsapp.util

import android.content.Context
import android.net.ConnectivityManager

fun Context.isConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    connectivityManager?.let {
        it.activeNetworkInfo?.let { info ->
            if (info.isConnected) return true
        }
    }
    return false
}