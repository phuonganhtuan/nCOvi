package com.example.ncovi.data.datasource.remote

import com.example.ncovi.data.datasource.remote.api.ResultCovi
import com.example.ncovi.data.model.Recent

interface RemoteCoviDataSource {

    suspend fun getRecent(): Recent
    suspend fun getHistory(limit: Int, offset: Int): List<Recent>
}
