package com.cci.testandroid

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.*

class SocketViewModel(application: Application) : AndroidViewModel(application) {
  private val serverSocket = DatagramSocket(8888)
  val ipAddress = MutableLiveData<String>(null)

  init {
    listenSocket()

    viewModelScope.launch(Dispatchers.IO) {
      val networkInterfaces = NetworkInterface.getNetworkInterfaces()

      val ip =
        networkInterfaces.toList()
          .find { it.displayName == "wlan0"}
          ?.inetAddresses
          ?.toList()
          ?.find { it is Inet4Address }
          ?.hostAddress ?: "127.0.0.1"

      ipAddress.postValue(ip)

      // Sert pour voir la liste des interfaces réseau et de leurs adresses IP :
//      ipAddress.postValue(
//        networkInterfaces.toList().fold("") { acc, ni ->
//          ni.inetAddresses.toList().fold(acc) { acc2, add ->
//            "$acc2\n${ni.displayName} : ${add.hostAddress}"
//          }
//        }
//      )
//      ipAddress.postValue(InetAddress.getLocalHost().hostAddress)
    }
  }

  fun listenSocket() {
    viewModelScope.launch(Dispatchers.IO) {
      val buffer = ByteArray(2)
      var packet = DatagramPacket(buffer, buffer.size)

      serverSocket.receive(packet)

      val data = String(packet.data)

      viewModelScope.launch(Dispatchers.Main) {
        Toast.makeText(getApplication(), "Data received : $data", Toast.LENGTH_LONG).show()
      }

      listenSocket()
    }
  }

  fun sendUDPData(data: String) {
    viewModelScope.launch(Dispatchers.IO) {
      DatagramSocket().use {
        val dataBytes = data.toByteArray()
        val address = InetAddress.getByName("192.168.1.237")

        val packet = DatagramPacket(dataBytes, dataBytes.size, address, 8888)
        it.send(packet)
      }
    }
  }
}