package com.example.connect.service

import com.example.connect.domain.WebData

interface IGetWebDataService {
    fun getWebDataByUrl(url: String): WebData
}