package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    init {
        getAsteroidRealEstateProperties()
    }

    private fun getAsteroidRealEstateProperties() {
//        AsteroidApi.retrofitService.getProperties().enqueue( object: Callback<String> {
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                _response.value = "Failure: " + t.message
//            }
//
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                _response.value = response.body()
//            }
//        })
    }
}