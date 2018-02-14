package com.boxes.articles

import android.app.Application
import com.boxes.articles.di.DaggerMainComponent
import com.boxes.articles.di.MainComponent
import com.boxes.articles.di.UtilsModule


class App:Application() {

    lateinit var mainComponent: MainComponent
    override fun onCreate() {
        super.onCreate()
        mainComponent = DaggerMainComponent
                .builder()
                .utilsModule(UtilsModule(applicationContext))
                .build()
    }

}