package com.example.connect.repository

import com.example.connect.domain.WebData

interface IGetWebDataRepository {
    fun getWebDataByUrl(url: String): WebData
}