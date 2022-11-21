package android.websocket

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.websocket.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONObject
import java.net.URI
import javax.net.ssl.SSLSocketFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var webSocketClient: WebSocketClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.btnSend.setOnClickListener {
            sendMessage()
        }
    }


    private fun initWebSocket() {
        val coinbaseUri: URI = URI(WEB_SOCKET_URL)
        webSocketClient = object : WebSocketClient(coinbaseUri) {
            @SuppressLint("SetTextI18n")
            override fun onOpen(handshakedata: ServerHandshake?) {
                this@MainActivity.runOnUiThread(java.lang.Runnable {
                    binding.txtServerStatus.apply {
                        text = "onOpen"
                        setTextColor(Color.GREEN)
                    }
                })
                Log.i(TAG, "onOpen")
            }

            override fun onMessage(message: String?) {
                Log.i(TAG, "onMessage: $message")
                this@MainActivity.runOnUiThread(java.lang.Runnable {
                    binding.txtMessage.text = message
                })
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                this@MainActivity.runOnUiThread(java.lang.Runnable {
                    binding.txtServerStatus.apply {
                        text = reason
                        setTextColor(Color.RED)
                    }
                })
                Log.i(TAG, "onClose -> code: $code, reason: $reason, remote: $remote")
                initWebSocket()
            }

            override fun onError(ex: Exception?) {
                this@MainActivity.runOnUiThread(java.lang.Runnable {
                    binding.txtServerStatus.apply {
                        text = ex?.message
                        setTextColor(Color.RED)
                    }
                })
                Log.e(TAG, "onError -> Exception: ${ex?.message}")
            }
        }

        // val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory
        // webSocketClient.setSocketFactory(socketFactory)
        webSocketClient.connect()
    }

    @SuppressLint("HardwareIds")
    private fun sendMessage() {
        val random = (0..999).random()
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val jsonString = "{\"devideId\":$deviceId,\"random\":$random}"
        val jsonObject: JSONObject = JSONObject(jsonString)

        binding.btnSend.text = random.toString()

        if (webSocketClient.isOpen)
            webSocketClient.send(jsonString)
        else {
            val message = "Message wasn't sent cause of webSocketClient is close"
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                binding.txtServerStatus.apply {
                    text = message
                    setTextColor(Color.RED)
                }
            })
            Log.i(TAG, message)
        }
    }

    override fun onResume() {
        super.onResume()
        initWebSocket()
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketClient.close()
    }

    companion object {
        const val WEB_SOCKET_URL = "http://192.168.1.2:3357"
        const val TAG = "APPLOG"
    }
}