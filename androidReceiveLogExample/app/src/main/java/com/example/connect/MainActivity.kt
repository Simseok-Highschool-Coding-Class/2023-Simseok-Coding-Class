package com.example.connect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.connect.databinding.ActivityMainBinding
import com.example.connect.repository.GetWebDataRepository
import com.example.connect.service.GetWebDataService
import com.example.connect.viewmodel.MainViewModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

private const val baseUrl = "http://ping2.doky.space"
private val TAG = "[Activity]"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this
    }

    fun onClickGetData(view: View) {
        getData()
    }

    fun displayData(message: String, date: String){
        binding.lblBody.text = message
    }





    // =====================

    fun handleData(rawStr: String){
        val rawStrLines = rawStr.lines()
        val getLatest = rawStrLines[rawStrLines.size-2].split(" : ")
        var dateRaw = getLatest[0]
        val log = getLatest[1]

        Log.e("[$TAG] handleData", "$dateRaw / $log")
        displayData(log, dateRaw)
    }

    fun getData() {
        // 데이터 가져오기
        val call = NoticeNetwork.getWebData().getWebData()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                Log.i("[$TAG] handleData", "onResponse")
                if(response.isSuccessful) {
                    try {
                        val resultRaw = response.body()!!.string() // <- 여기에 결과가 들어있음
                        handleData(resultRaw)
                        Log.i("[$TAG] handleData: success / ", resultRaw)
                    } catch (e: Exception) {
                        Log.e("[$TAG] handleData: failure / ", e.toString())
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("[$TAG] handleData: failure / ", "Network failure")
            }
        })
    }
}

object NoticeNetwork {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .build()

    interface JsonPlaceHolderApi {
        @GET("logs-test-arr.txt")
        fun getWebData(): Call<ResponseBody>
    }

    fun getWebData(): JsonPlaceHolderApi {
        return retrofit.create(JsonPlaceHolderApi::class.java)
    }
}


