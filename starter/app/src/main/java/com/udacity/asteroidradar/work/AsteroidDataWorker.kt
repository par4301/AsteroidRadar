package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidRepository
import retrofit2.HttpException

class AsteroidDataWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "AsteroidDataWorker"
    }


    override suspend fun doWork(): Result {

        val database = AsteroidDatabase.getInstance(applicationContext).asteroidDatabaseDao
        val repository = AsteroidRepository(database)

        return try {
            repository.refreshAsteroids()
            repository.refreshPictureOfTheDay()
            Result.success()
        } catch (httpException: HttpException) {
            Result.retry()
        }
    }

}