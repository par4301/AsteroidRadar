package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM asteroids_table WHERE close_approach_date = date('now') ORDER BY close_approach_date DESC")
    fun getTodayAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroids_table WHERE close_approach_date BETWEEN date('now') AND date('now', '+7 days')")
    fun getWeeklyAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroids_table WHERE close_approach_date >= date('now') ORDER BY close_approach_date DESC")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("DELETE FROM asteroids_table")
    fun clear()

    @Query("DELETE FROM asteroids_table WHERE close_approach_date < date('now')")
    fun deletePastAsteroids(): Int

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertAll(asteroids: List<AsteroidEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: AsteroidEntity)

//    @Query("SELECT * FROM picture_of_day")
//    suspend fun getPictureOfTheDay(): DatabasePictureOfDay

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertPictureOfTheDay(vararg pictureOfDay: DatabasePictureOfDay)
}