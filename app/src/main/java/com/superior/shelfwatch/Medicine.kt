package com.superior.shelfwatch

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class Medicine(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,
    val quantity: Int,

    // Date format: YYYY-MM-DD
    val expiryDate: String? = null
)