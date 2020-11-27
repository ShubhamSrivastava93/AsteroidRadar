package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepo
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private var asteroidRepo = AsteroidRepo(database)
    private val _pictureOfDayLiveData = MutableLiveData<PictureOfDay>()
    val pictureOfDayLiveData: LiveData<PictureOfDay>
        get() = _pictureOfDayLiveData


    init {
        viewModelScope.launch {
            val apiKey = application.getString(R.string.api_key)
            asteroidRepo.refreshAsteroids(apiKey)
            asteroidRepo.getPictureOfTheDay(apiKey, _pictureOfDayLiveData)
        }
    }

}