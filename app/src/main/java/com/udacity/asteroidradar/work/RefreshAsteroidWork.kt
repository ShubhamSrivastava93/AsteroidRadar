package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepo

class RefreshAsteroidWork(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "asteroidRefreshWorker"
    }

    override suspend fun doWork(): Result {
        val asteroidDatabase = getDatabase(applicationContext)
        try {
            AsteroidRepo(asteroidDatabase).refreshAsteroids(applicationContext.getString(R.string.api_key))
            return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }
    }

}