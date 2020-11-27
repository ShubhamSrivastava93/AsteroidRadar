package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.model.Asteroid

@Dao
interface AsteroidDao {
    @Query("select * from asteroid_database where closeApproachDate>=:date order by closeApproachDate ASC")
    fun getAsteroidsFromDate(date: String): LiveData<List<Asteroid>>

    @Query("select * from asteroid_database where closeApproachDate=:date order by closeApproachDate ASC")
    fun getAsteroidsByDate(date: String): LiveData<List<Asteroid>>

    @Query("select * from asteroid_database order by closeApproachDate ASC")
    fun getAllAsteroids(): LiveData<List<Asteroid>>

    @Query("select * from asteroid_database where closeApproachDate between :startDate and :endDate order by closeApproachDate ASC")
    fun getAsteroidsBetweenDates(startDate: String, endDate: String): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids: List<Asteroid>)
}

@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroids"
            ).build()
        }
    }
    return INSTANCE
}
