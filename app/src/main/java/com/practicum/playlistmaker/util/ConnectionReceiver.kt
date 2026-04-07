package com.practicum.playlistmaker.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import com.practicum.playlistmaker.R

class ConnectionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_CONNECTIVITY) {

            val isConnected = isNetworkConnected(context)

            if (!isConnected) {
                Toast.makeText(context, R.string.error_no_connection, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isNetworkConnected(context: Context?): Boolean {
        //получение информации о сетевом соединении
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //версия Android больше 10 (Q) или нет (для совместимости)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //получение активного соединения
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            //получаем состояние по соединению, при полном отсутствии возвращаем false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

    companion object {
        const val ACTION_CONNECTIVITY = "android.net.conn.CONNECTIVITY_CHANGE"
    }
}