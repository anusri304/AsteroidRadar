package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.asDatabaseModel
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.AsteroidFilter
import com.udacity.asteroidradar.network.PictureOfDay
import com.udacity.asteroidradar.util.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidsDatabase) {
    lateinit var asteroids: LiveData<List<Asteroid>>
    fun getAsteroids(filter: AsteroidFilter): LiveData<List<Asteroid>> {
        if (filter.value == "saved") {
            asteroids =
                Transformations.map(database.asteroidDao.getSavedAsteroids()) {
                    it.asDomainModel()
                }
        } else if (filter.value == "today") {
            asteroids =
                Transformations.map(database.asteroidDao.getAsteroidsForToday(Util.Companion.getStartDate())) {
                    it.asDomainModel()
                }
            println("Asteroids" + (asteroids.value?.size))
        } else {
            asteroids =
                Transformations.map(database.asteroidDao.getAsteroidsForWeek(Util.Companion.getStartDate())) {
                    it.asDomainModel()
                }
        }
        return asteroids

    }


    suspend fun insertAsteroids() {
        withContext(Dispatchers.IO) {
            val response = AsteroidApi.retrofitService.getAsteroidList(
                Util.Companion.getStartDateStr(),
                Util.Companion.getEndDateStr(), "ZkPL6anWyY2iJFvTxGJC3XmAKAQU3eegGgohaDFm"
            )
            val asteroidList: List<Asteroid> = parseAsteroidsJsonResult(JSONObject(response))
            database.asteroidDao.insertAll(asteroidList.asDatabaseModel())
            println("response" + response)
        }
    }

    suspend fun getPictureOfTheDay(): PictureOfDay {
        return AsteroidApi.retrofitService.getPictureOfTheDay("ZkPL6anWyY2iJFvTxGJC3XmAKAQU3eegGgohaDFm")
    }


}


