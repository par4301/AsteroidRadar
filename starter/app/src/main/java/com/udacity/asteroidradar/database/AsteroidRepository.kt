package com.udacity.asteroidradar.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.data.Asteroid
import com.udacity.asteroidradar.data.PictureOfDay
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val databaseDao: AsteroidDatabaseDao) {

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(databaseDao.getAsteroids()) {
        it.asDomainModel()
    }

    val todayAsteroids: LiveData<List<Asteroid>> = Transformations.map(databaseDao.getTodayAsteroids()) {
        it.asDomainModel()
    }

    val weeklyAsteroids: LiveData<List<Asteroid>> = Transformations.map(databaseDao.getWeeklyAsteroids()) {
        it.asDomainModel()
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val jsonResult = AsteroidApi.retrofitService.getAsteroids()
            val asteroids = parseAsteroidsJsonResult(JSONObject(jsonResult))
            val listDbAsteroids = mutableListOf<AsteroidEntity>()
            print(asteroids)

            for (asteroid in asteroids) {
                val entityAsteroid = AsteroidEntity(asteroid.id,
                    asteroid.codename,
                    asteroid.closeApproachDate,
                    asteroid.absoluteMagnitude,
                    asteroid.estimatedDiameter,
                    asteroid.relativeVelocity,
                    asteroid.distanceFromEarth,
                    asteroid.isPotentiallyHazardous
                )

                listDbAsteroids.add(entityAsteroid)
            }

            databaseDao.insertAll(listDbAsteroids.toList())
        }
    }


    suspend fun refreshPictureOfTheDay() {
        withContext(Dispatchers.IO) {
            val pictureOfDay = AsteroidApi.retrofitService.getPictureOfTheDay()
            val dbPictureOfDay = PictureEntity(pictureOfDay.url, pictureOfDay.title, pictureOfDay.mediaType)
            databaseDao.insertPictureOfTheDay(dbPictureOfDay)
        }
    }

    @WorkerThread
    suspend fun getAsteroidImageOfTheDay(): PictureOfDay {
        val databasePictureOfDay = databaseDao.getPictureOfTheDay()
        return PictureOfDay(databasePictureOfDay.mediaType, databasePictureOfDay.title, databasePictureOfDay.url)
    }
}