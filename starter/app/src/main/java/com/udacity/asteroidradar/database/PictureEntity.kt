package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "picture_of_day_table")
data class PictureEntity constructor(
    @PrimaryKey
    var url: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "media_type")
    var mediaType: String
)