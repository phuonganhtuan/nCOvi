package com.example.ncovi.data.datasource.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.ncovi.data.model.Recent

@Dao
interface CoviDao {

    @Query("select * from Recent")
    fun getAllEntities(): LiveData<List<Recent>>

    @Insert(onConflict = REPLACE)
    suspend fun insertEntity(entity: Recent)

    @Query("delete from Recent where id = :id")
    suspend fun delete(id: Int)
}
