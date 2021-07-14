package com.example.ncovi.data.datasource.local

import androidx.lifecycle.LiveData
import com.example.ncovi.data.model.Recent

interface LocalCoviDataSource {

    suspend fun insert(entity: Recent)

    suspend fun delete(id: Int)

    fun getRecent(): LiveData<List<Recent>>
}