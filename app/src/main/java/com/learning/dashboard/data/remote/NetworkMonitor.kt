package com.learning.dashboard.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * Thin wrapper so the mock API can honestly fail when the device is offline,
 * which lets the offline-cache path be exercised by actually toggling airplane mode.
 */
class NetworkMonitor(private val context: Context) {

    fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                ?: return false
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
