package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.data.Asteroid
import com.udacity.asteroidradar.data.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidRepository
import com.udacity.asteroidradar.network.AsteroidAPIStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class MainViewModel(private val asteroidRepository: AsteroidRepository) : ViewModel() {
    private val asteroidFilter = MutableLiveData(AsteroidFilter.WEEKLY)

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> get() = _status

    private val _imageOfDay = MutableLiveData<PictureOfDay>()
    val imageOfDay: LiveData<PictureOfDay> get() = _imageOfDay

    private val _asteroids = MutableLiveData<List<Asteroid>>()

    val asteroids = Transformations.switchMap(asteroidFilter) {
        when (it!!) {
            AsteroidFilter.WEEKLY -> asteroidRepository.weeklyAsteroids
            AsteroidFilter.TODAY -> asteroidRepository.todayAsteroids
            else -> asteroidRepository.asteroids
        }
    }

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // Defined for navigation
    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetails get() = _navigateToAsteroidDetails

    enum class AsteroidFilter {
        ALL,
        WEEKLY,
        TODAY,
    }

    init {
        viewModelScope.launch {
            refreshPictureOfDayFromNetwork()
            refreshAsteroidsFromNetwork(AsteroidFilter.ALL)
        }
    }


    private fun refreshPictureOfDayFromNetwork() {
        viewModelScope.launch {
            try {
                _status.value = AsteroidAPIStatus.LOADING.toString()
                asteroidRepository.refreshPictureOfTheDay()
                _imageOfDay.value = asteroidRepository.getAsteroidImageOfTheDay()
            } catch (e: Exception) {
                _status.value = "Failure: ${e.message}"
            } finally {
                _status.value = AsteroidAPIStatus.DONE.toString()
            }
        }
    }

    private fun refreshAsteroidsFromNetwork(filter: AsteroidFilter) {
        viewModelScope.launch {
            try {
                _status.value = AsteroidAPIStatus.LOADING.toString()
                asteroidRepository.refreshAsteroids()
            } catch (e: Exception) {
                _status.value = "Failure: ${e.message}"
            } finally {
                _status.value = AsteroidAPIStatus.DONE.toString()
            }
        }
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToAsteroidDetails.value = asteroid
    }

    fun onAsteroidClickedNavigated() {
        _navigateToAsteroidDetails.value = null
    }

    fun setAsteroidFilter(filter: AsteroidFilter) {
        asteroidFilter.postValue(filter)
    }
}