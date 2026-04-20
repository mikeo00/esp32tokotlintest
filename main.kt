package com.example.espapptest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var textViewResult: TextView
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Link to your TextView (make sure id matches XML)
        textViewResult = findViewById(R.id.textViewResult)

        // Call ESP32 when app starts
        getPrediction()
    }

    private fun getPrediction() {
        val request = Request.Builder()
            .url("http://192.168.4.1/predict") // 🔴 replace with your ESP32 IP
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    textViewResult.text = getString(R.string.error_message, e.message ?: "Unknown error")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                if (body != null) {
                    try {
                        val obj = JSONObject(body)

                        val type = obj.getString("type")
                        val status = obj.getString("status")
                        val confidence = obj.getDouble("confidence")

                        val resultText = getString(
                            R.string.prediction_format,
                            type,
                            status,
                            (confidence * 100).toInt()
                        )

                        runOnUiThread {
                            textViewResult.text = resultText
                        }

                    } catch (e: Exception) {
                        runOnUiThread {
                            textViewResult.text = getString(R.string.parsing_error)
                        }
                    }
                }
            }
        })
    }
}
