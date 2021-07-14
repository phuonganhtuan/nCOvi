package com.example.ncovi.data.datasource.remote.api

import com.example.ncovi.data.model.Recent
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("key-value-stores/EaCBL1JNntjR3EakU/records/LATEST?disableRedirect=true")
    suspend fun getRecent(): Recent

    @GET("datasets/RnzK2Aea1RlgBcauw/items")
    suspend fun getHistory(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("desc") desc: Int = 1,
        @Query("format") format: String = "json",
    ): List<Recent>
}
