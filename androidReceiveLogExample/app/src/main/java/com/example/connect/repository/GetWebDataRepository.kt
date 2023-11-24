package com.example.connect.repository

import android.util.Log
import com.example.connect.domain.WebData

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET



private object NoticeNetwork {

    const val baseUrl = "http://ping2.doky.space"

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



class GetWebDataRepository: IGetWebDataRepository {

    private val TAG = "[Repository]"

    override fun getWebDataByUrl(url: String): WebData {

        // 데이터 가져오기
        val call = NoticeNetwork.getWebData().getWebData()
        var resultRaw: String = ""
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                Log.i("[Repository]", "onResponse")
                if(response.isSuccessful) {
                    try {
                        resultRaw = response.body()!!.string() // <- 여기에 결과가 들어있음
                        Log.i("============", resultRaw)
                    } catch (e: Exception) {
                        Log.e("[Repository]", e.toString())
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("[Repository]", "Network failure")
            }
        })


        var webData = WebData(
            url = url,
            header = "metadata=content-header;",
            body = "<html>hello world!</html>",
            status = 200
        )

        return webData
    }

}