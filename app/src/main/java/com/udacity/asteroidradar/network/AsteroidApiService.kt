/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.udacity.asteroidradar.network

// import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
// import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.scalars.ScalarsConverterFactory

//TODO move to constants
private const val BASE_URL = "https://api.nasa.gov/"
//enum class MarsApiFilter(val value: String) { SHOW_RENT("rent"), SHOW_BUY("buy"), SHOW_ALL("all") }

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()


/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

/**
 * A public interface that exposes the [getProperties] method
 */
interface AsteroidApiService {
    /**
     * Returns a Coroutine [List] of [Asteroid] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that  endpoint will be requested with the GET
     * HTTP method
     */
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidList(
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String,
        @Query("api_key") api_key: String
    ): String

    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(
        @Query("api_key") api_key: String
    ): PictureOfDay
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy { retrofit.create(AsteroidApiService::class.java) }
}
