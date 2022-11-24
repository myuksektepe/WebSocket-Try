package android.websocket.presentation.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.websocket.R
import android.websocket.databinding.ActivityMainBinding
import android.websocket.domain.model.CurrenciesModel
import android.websocket.domain.model.CurrencyRecyclerViewItems
import android.websocket.presentation.adapter.CurrencyAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONObject
import java.net.URI

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var webSocketClient: WebSocketClient
    lateinit var currencyAdapter: CurrencyAdapter
    val gson = Gson()
    private var currenciesList: MutableList<CurrencyRecyclerViewItems> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        currencyAdapter = CurrencyAdapter()
        binding.listCurrencies.apply {
            adapter = currencyAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        /*
        binding.btnSend.setOnClickListener {
            sendMessage()
        }
        */
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
                    val response = gson.fromJson(message, CurrenciesModel::class.java)
                    setAdapter(response)
                    //binding.txtMessage.text = message
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

    private fun setAdapter(response: CurrenciesModel) {
        currenciesList.clear()
        response.title.let {
            currenciesList.add(CurrencyRecyclerViewItems.Title(it))
        }
        response.row.forEach {
            it.let {
                currenciesList.add(CurrencyRecyclerViewItems.Row(it.currencyName, it.currencyAmount))
            }
        }
        currencyAdapter.items = currenciesList
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