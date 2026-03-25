package com.xaviermaximin.tv.application

import android.app.Application
import android.os.StrictMode

class TVStreamKitApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Documentation https://developer.android.com/reference/android/os/StrictMode
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .build()
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectAll()
                .build()
        )
    }
}