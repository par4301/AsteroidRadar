package com.udacity.asteroidradar.network

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.data.Asteroid
import com.udacity.asteroidradar.data.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidEntity
import com.udacity.asteroidradar.database.PictureEntity

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 */

@JsonClass(generateAdapter = true)
data class NetworkPictureOfDayContainer(val pictureOfDay: PictureOfDay)

@JsonClass(generateAdapter = true)
data class NetworkPictureOfDay (
    val title: String,
    val url: String,
    val mediaType: String
)

@JsonClass(generateAdapter = true)
data class NetworkAsteroidsContainer(val asteroids: List<Asteroid>)

// Asteroid represents a astroid that can be interacted with
@JsonClass(generateAdapter = true)
data class NetworkAsteroid(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

fun NetworkAsteroidsContainer.asDomainModel(): List<Asteroid> {
    return asteroids.map {
        Asteroid(
                id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absoluteMagnitude = it.absoluteMagnitude,
                estimatedDiameter = it.estimatedDiameter,
                relativeVelocity = it.relativeVelocity,
                distanceFromEarth = it.distanceFromEarth,
                isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun NetworkAsteroidsContainer.asDatabaseModel() : Array<AsteroidEntity> {
    return asteroids.map {
        AsteroidEntity(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}

fun NetworkPictureOfDayContainer.asDomainModel(): PictureOfDay {
    return PictureOfDay(pictureOfDay.mediaType, pictureOfDay.title, pictureOfDay.url)
}

fun NetworkPictureOfDayContainer.asDatabaseModel() : PictureEntity {
    return PictureEntity(pictureOfDay.title, pictureOfDay.url, pictureOfDay.mediaType)
}
