package com.example.pokemonultimate.data.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class NetworkAvailability(private val context: Context, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val connectivityManager by lazy { context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    private val mutex = Mutex()

    val isNetworkAvailable: Flow<Boolean> = callbackFlow {
        val networks = mutableListOf<Network>()

        val callback = object : NetworkCallback() {

            override fun onAvailable(network: Network) {
                launch {
                    mutex.withLock {
                        networks.add(network)
                        send(element = hasAvailableNetwork(networks))
                    }
                }
            }

            override fun onLost(network: Network) {
                launch {
                    mutex.withLock {
                        networks.remove(network)
                        send(element = hasAvailableNetwork(networks))
                    }
                }
            }
        }

        launch(ioDispatcher) {
            send(getInitialNetworkAvailability(connectivityManager))
        }

        registerNetworkCallback(connectivityManager, callback, ::send)

        awaitClose { unregisterNetworkCallback(connectivityManager, callback) }
    }

    private suspend fun registerNetworkCallback(
        connectivityManager: ConnectivityManager,
        callback: NetworkCallback,
        send: suspend (Boolean) -> Unit,
    ) {
        runCatching {
            connectivityManager.registerNetworkCallback(networkRequestBuilder(), callback)
        }.onFailure {
            send(false)
        }
    }

    private fun unregisterNetworkCallback(connectivityManager: ConnectivityManager, callback: NetworkCallback) {
        runCatching {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    private fun getInitialNetworkAvailability(connectivityManager: ConnectivityManager): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.activeNetwork?.let(::hasInternetConnectivity) ?: false
        } else {
            connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }

    private fun networkRequestBuilder(): NetworkRequest {
        return NetworkRequest.Builder().apply {
            addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }.build()
    }

    private fun hasInternetConnectivity(network: Network) = runCatching {
        network.getByName(ROOT_SERVER_URL) != null
    }.getOrDefault(false)

    private suspend fun hasAvailableNetwork(networks: List<Network>) = withContext(ioDispatcher) {
        networks.any(::hasInternetConnectivity)
    }

    companion object {
        private const val ROOT_SERVER_URL = "a.root-servers.net"
    }
}
