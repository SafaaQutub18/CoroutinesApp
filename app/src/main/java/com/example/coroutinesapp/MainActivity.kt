package com.example.coroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.coroutinesapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    var currentAdvice = ""
    var advcies = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.getAdviceBt.setOnClickListener {
            requestAPI()
        }
    }

    private fun requestAPI() {
        CoroutineScope(IO).launch {
            var data = async{fetchData()}.await()
            if(data.isNotEmpty())
                populateTV(data)
            else
                Log.d("MAIN","unable to get data")
        }
    }

    private fun fetchData():String {
        var response = ""
        try{
            response = URL("https://api.adviceslip.com/advice").readText()
        }catch (e:Exception){
            Log.d("MAIN","ISSUE: $e")
        }
        return response
    }

    private suspend fun populateTV(result:String) {
        withContext(Main){
        val jason = JSONObject(result)
            currentAdvice = jason.getJSONObject("slip").getString("advice")
            binding.adviceTV.setText(currentAdvice)
        }
    }
}