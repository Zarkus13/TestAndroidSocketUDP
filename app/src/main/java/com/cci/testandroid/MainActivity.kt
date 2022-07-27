package com.cci.testandroid

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.format.Formatter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cci.testandroid.databinding.ActivityMainBinding
import java.net.InetAddress

class MainActivity : AppCompatActivity() {
  val socketViewModel: SocketViewModel by viewModels()
  lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater, null, false)

    setContentView(binding.root)

    binding.udpSendButton.setOnClickListener {
      socketViewModel.sendUDPData("TEST SEND UDP")
    }

    socketViewModel.ipAddress.observe(this) { ip ->
      binding.ipTextView.text = ip
    }
  }

}