package com.example.demomotion.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ncovi.data.datasource.local.dao.CoviDao
import com.example.ncovi.data.model.Recent

@Database(entities = [Recent::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun coviDao(): CoviDao

    companion object {

        private const val DATABASE_NAME = "CoviDB"

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, DATABASE_NAME
        ).build()
    }
}
