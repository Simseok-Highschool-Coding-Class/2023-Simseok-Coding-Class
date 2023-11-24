package com.example.connect.viewmodel

import com.example.connect.service.IGetWebDataService

class MainViewModel(private val service: IGetWebDataService) {

    private val TAG = "[ViewModel]"

    var body: String = ""
        private set
    var message: String = ""
        private set
    var status: Int = 0
        private set
    var isError: Boolean = false
        private set

    fun getWebData() {
        val data = service.getWebDataByUrl("https://www.example.com")
        body = data.body
        status = data.status

        if(data.status < 400) {
            isError = false
            message = "Okay"
        } else if (data.status in 400..499) {
            isError = true
            message = "Connection Error"
        } else if (data.status in 500..599) {
            isError = true
            message = "Server Error"
        } else  {
            isError = true
            message = "Unknown Error"
        }
    }
}