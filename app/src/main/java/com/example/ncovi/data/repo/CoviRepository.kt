package com.example.ncovi.data.repo

import androidx.lifecycle.LiveData
import com.example.ncovi.data.model.Recent

interface CoviRepository {

    suspend fun getRecent(): Recent
    suspend fun getHistory(limit: Int, offset: Int): List<Recent>
    suspend fun insert(entity: Recent)
    suspend fun delete(id: Int)
    fun getAllHistories(): LiveData<List<Recent>>
}
