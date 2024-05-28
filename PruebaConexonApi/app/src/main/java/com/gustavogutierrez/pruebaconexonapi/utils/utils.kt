package com.gustavogutierrez.demo05.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import com.gustavogutierrez.pruebaconexonapi.models.TasksItem

fun checkConnection(context: Context): Boolean {
    val cm = context.getSystemService<ConnectivityManager>()
    val networkInfo = cm!!.activeNetwork
    if (networkInfo != null) {
      val activeNetwork = cm.getNetworkCapabilities(networkInfo)
        if (activeNetwork != null) {
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                else -> return false
            }
        }
    }

    return false
}