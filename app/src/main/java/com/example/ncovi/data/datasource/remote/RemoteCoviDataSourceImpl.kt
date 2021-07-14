package com.example.ncovi.data.datasource.remote

import com.example.ncovi.data.datasource.remote.api.ApiService

class RemoteCoviDataSourceImpl(private val apiService: ApiService) : RemoteCoviDataSource {

    override suspend fun getRecent() = apiService.getRecent()

    override suspend fun getHistory(limit: Int, offset: Int) = apiService.getHistory(limit, offset)
}
