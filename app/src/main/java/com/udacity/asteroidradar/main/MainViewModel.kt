package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.util.Util
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.AsteroidFilter
import com.udacity.asteroidradar.network.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import org.json.JSONObject

enum class AsteroidApiStatus { LOADING, ERROR, DONE }

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<AsteroidApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<AsteroidApiStatus>
        get() = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of Asteroid
    // with new values
//    private val _asteroids = MutableLiveData<List<Asteroid>>()
//
//    // The external LiveData interface to the property is immutable, so only this class can modify
//    val asteroids: LiveData<List<Asteroid>>
//        get() = _asteroids

    // Internally, we use a MutableLiveData to handle navigation to the selected property
    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()

    // The external immutable LiveData for the navigation property
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()

    val pictureOfTheDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay


    init {
        viewModelScope.launch {
            if(Util.Companion.isNetworkAvailable(application.applicationContext)) {
                asteroidRepository.insertAsteroids()
            }
        }
        viewModelScope.launch {
            _pictureOfTheDay.value= asteroidRepository.getPictureOfTheDay()
        }
//        viewModelScope.launch {
//            asteroidRepository.(AsteroidFilter.SHOW_WEEK_ASTEROIDS)
//        }
    }



val asteroids = asteroidRepository.asteroids

 val todayAsteroids = asteroidRepository.todayAsteroids


//    private fun getPictureOfTheDay() {
//        viewModelScope.launch {
//            try {
//                val response =
//                    AsteroidApi.retrofitService.getPictureOfTheDay("ZkPL6anWyY2iJFvTxGJC3XmAKAQU3eegGgohaDFm")
//                //  _asteroidList.value = parseAsteroidsJsonResult(JSONObject(response))
//                _pictureOfTheDay.value = response
//                println("response" + response)
//            } catch (e: Exception) {
//                _pictureOfTheDay.value = PictureOfDay("", "", "")
//            }
//        }
//    }

//    private fun getAsteroidList() {
//        viewModelScope.launch {
//            _status.value = AsteroidApiStatus.LOADING
//            try {
//                val response = AsteroidApi.retrofitService.getAsteroidList(
//                    Util.Companion.getTodaysDate(),
//                    Util.Companion.getTodaysDate(), "ZkPL6anWyY2iJFvTxGJC3XmAKAQU3eegGgohaDFm"
//                )
//                //  _asteroidList.value = parseAsteroidsJsonResult(JSONObject(response))
//                val asteroidList: List<Asteroid> = parseAsteroidsJsonResult(JSONObject(response))
//                _asteroidList.value = parseAsteroidsJsonResult(JSONObject(response))
//                println("response" + asteroidList.size)
//                _status.value = AsteroidApiStatus.DONE
//            } catch (e: Exception) {
//                _status.value = AsteroidApiStatus.ERROR
//                _asteroidList.value = ArrayList()
//            }
//        }
//    }


    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    /**
     * After the navigation has taken place, make sure navigateToSelectedProperty is set to null
     */
    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}