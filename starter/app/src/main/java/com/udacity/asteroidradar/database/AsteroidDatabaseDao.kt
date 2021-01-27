package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDatabaseDao {
    @Query("SELECT * FROM asteroids_table WHERE close_approach_date = date('now') ORDER BY close_approach_date DESC")
    fun getTodayAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroids_table WHERE close_approach_date BETWEEN date('now') AND date('now', '+7 days')")
    fun getWeeklyAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroids_table WHERE close_approach_date >= date('now') ORDER BY close_approach_date DESC")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM picture_of_day_table")
    suspend fun getPictureOfTheDay(): PictureEntity

    @Query("DELETE FROM asteroids_table WHERE close_approach_date < date('now')")
    fun deletePastAsteroids(): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroids: List<AsteroidEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPictureOfTheDay(vararg pictureOfDay: PictureEntity)
}