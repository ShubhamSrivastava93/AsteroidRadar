package com.udacity.asteroidradar.repository

import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.utils.Constants.DEFAULT_END_DATE_DAYS
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.utils.DateUtils.Companion.getCurrentDate
import com.udacity.asteroidradar.utils.DateUtils.Companion.getEndDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.await


class AsteroidRepo(asteroidDatabase: AsteroidDatabase) {

    private val asteroidDatabase: AsteroidDatabase

    init {
        this.asteroidDatabase = asteroidDatabase
    }

    suspend fun refreshAsteroids(apiKey: String) {
        withContext(Dispatchers.IO) {
            try {
                val asteroidResult = Network.asteroids.getAsteroids(
                    getCurrentDate(),
                    getEndDate(DEFAULT_END_DATE_DAYS),
                    apiKey
                ).await()
                if (asteroidResult.isNotEmpty()) {
                    asteroidDatabase.asteroidDao.insertAll(
                        parseAsteroidsJsonResult(JSONObject(asteroidResult))
                    )
                }
            } catch (e: Exception) {

            }
        }
    }

    suspend fun getPictureOfTheDay(apiKey: String, liveData: MutableLiveData<PictureOfDay>) {
        withContext(Dispatchers.IO) {
            try {
                val pictureOfDay = Network.asteroids.getImageOfDay(apiKey).await()
                if (pictureOfDay.title.isNotEmpty()) {
                    liveData.postValue(pictureOfDay)
                }
            } catch (e: Exception) {

            }
        }
    }
}