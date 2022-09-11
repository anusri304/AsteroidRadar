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

package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.work.DeleteAsteroidWorker
import com.udacity.asteroidradar.work.DownloadAsteroidWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Override application to setup background work via WorkManager
 */
class AsteroidApplication : Application() {

    val applicationScope = CoroutineScope(Dispatchers.Default)

    private fun delayedInit() {
        applicationScope.launch {
            setupDownloadWork()
            setupDeleteWork()
        }
    }

    private fun setupDownloadWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .build()

        val oneTimeRequest= OneTimeWorkRequestBuilder<DownloadAsteroidWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniqueWork( DownloadAsteroidWorker.WORK_NAME, ExistingWorkPolicy.KEEP,oneTimeRequest)
    }

    private fun setupDeleteWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .build()

        val oneTimeRequest= OneTimeWorkRequestBuilder<DeleteAsteroidWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniqueWork( DeleteAsteroidWorker.WORK_NAME, ExistingWorkPolicy.KEEP,oneTimeRequest)
    }

    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }
}
