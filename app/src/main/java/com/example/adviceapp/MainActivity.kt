package com.example.adviceapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.adviceapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btGetAdvice.setOnClickListener {
            requestAPI()
        }
    }

    private fun requestAPI() {
        CoroutineScope(IO).launch {
            val data = async {
                fetchData()
            }.await()
            if (data.isNotEmpty()){
                populateTV(data)
            }
            else
                Log.e("MAIN", "Unable to get data")
        }
    }

    private suspend fun populateTV(data: String) {
        withContext(Main){
            val jason = JSONObject(data)

            val advice = jason.getJSONObject("slip").getString("advice")
            binding.tvAdvice.text = advice
        }
    }

    private fun fetchData():String{
        var response = ""

        try {
            response = URL("https://api.adviceslip.com/advice").readText()
        }
        catch (e: Exception){
            Log.e("MAIN", "ISSUE: $e")
        }
        return response
    }
}