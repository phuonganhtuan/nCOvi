package com.example.ncovi.data.repo

import com.example.ncovi.data.datasource.local.LocalCoviDataSource
import com.example.ncovi.data.datasource.remote.RemoteCoviDataSource
import com.example.ncovi.data.model.Recent

class CoviRepositoryImpl(
    private val remoteCoviDataSource: RemoteCoviDataSource,
    private val localCoviDataSource: LocalCoviDataSource
) : CoviRepository {

    override suspend fun getRecent() = remoteCoviDataSource.getRecent()

    override suspend fun getHistory(limit: Int, offset: Int) =
        remoteCoviDataSource.getHistory(limit, offset)

    override fun getAllHistories() = localCoviDataSource.getRecent()

    override suspend fun insert(entity: Recent) = localCoviDataSource.insert(entity)

    override suspend fun delete(id: Int) = localCoviDataSource.delete(id)
}
