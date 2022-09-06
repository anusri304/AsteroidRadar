package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Util
import com.udacity.asteroidradar.Util.Companion.getTodaysDate
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.Asteroid
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.AsteroidApiService
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat

enum class AsteroidApiStatus { LOADING, ERROR, DONE }

class MainViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<AsteroidApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<AsteroidApiStatus>
        get() = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of Asteroid
    // with new values
    private val _asteroidList = MutableLiveData<List<Asteroid>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    // Internally, we use a MutableLiveData to handle navigation to the selected property
    private val _navigateToSelectedProperty = MutableLiveData<Asteroid>()

    // The external immutable LiveData for the navigation property
    val navigateToSelectedProperty: LiveData<Asteroid>
        get() = _navigateToSelectedProperty

    init {
        getAsteroidList()
    }

    private fun getAsteroidList() {
        viewModelScope.launch {
            _status.value = AsteroidApiStatus.LOADING
            try {
                val response= AsteroidApi.retrofitService.getAsteroidList(Util.Companion.getTodaysDate(),Util.Companion.getTodaysDate(),"ZkPL6anWyY2iJFvTxGJC3XmAKAQU3eegGgohaDFm")
             //  _asteroidList.value = parseAsteroidsJsonResult(JSONObject(response))
                val asteroidList:List<Asteroid> = parseAsteroidsJsonResult(JSONObject(response))
                _asteroidList.value = parseAsteroidsJsonResult(JSONObject(response))
               println("response" + asteroidList.size)
                _status.value = AsteroidApiStatus.DONE
            } catch (e: Exception) {
                _status.value = AsteroidApiStatus.ERROR
                _asteroidList.value = ArrayList()
            }
        }
    }

}