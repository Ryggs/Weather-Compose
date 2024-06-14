package com.dvt.weather.interview.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dvt.weather.interview.data.model.Model

@Entity(tableName = "history_search_address")
data class HistorySearchAddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val addressName: String,
    val timeSearch: Long,
) : Model()
