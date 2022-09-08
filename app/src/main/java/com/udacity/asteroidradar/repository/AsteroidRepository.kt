package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.asDatabaseModel
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.PictureOfDay
import com.udacity.asteroidradar.util.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidsDatabase) {
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    suspend fun insertAsteroids() {
        withContext(Dispatchers.IO) {
            val response = AsteroidApi.retrofitService.getAsteroidList(
                Util.Companion.getTodaysDate(),
                Util.Companion.getTodaysDate(), "ZkPL6anWyY2iJFvTxGJC3XmAKAQU3eegGgohaDFm"
            )
            val asteroidList: List<Asteroid> = parseAsteroidsJsonResult(JSONObject(response))
            database.asteroidDao.insertAll(asteroidList.asDatabaseModel())
            println("response" + response)
        }
    }

    suspend fun getPictureOfTheDay() {
        val response =
            AsteroidApi.retrofitService.getPictureOfTheDay("ZkPL6anWyY2iJFvTxGJC3XmAKAQU3eegGgohaDFm")
    }

}