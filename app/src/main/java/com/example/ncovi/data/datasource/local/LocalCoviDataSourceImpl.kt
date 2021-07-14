package com.example.ncovi.data.datasource.local

import com.example.ncovi.data.datasource.local.dao.CoviDao
import com.example.ncovi.data.model.Recent

class LocalCoviDataSourceImpl(private val dao: CoviDao) : LocalCoviDataSource {

    override fun getRecent() = dao.getAllEntities()

    override suspend fun insert(entity: Recent) = dao.insertEntity(entity)

    override suspend fun delete(id: Int) = dao.delete(id)
}
