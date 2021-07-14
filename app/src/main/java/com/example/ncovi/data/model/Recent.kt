package com.example.ncovi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recent(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val sourceUrl: String = "",
    val lastUpdatedAtApify: String = "",
    val lastUpdatedAtSource: String = "",
    val infected: Int = 0,
    val treated: Int = 0,
    val recovered: Int = 0,
    val deceased: Int = 0,
)
