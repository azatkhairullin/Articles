package com.boxes.articles.di

import android.content.Context
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class UtilsModule(private val applicationContext: Context) {
    @Provides
    @Singleton
    fun provideLocationProviderClient() = LocationServices.getFusedLocationProviderClient(applicationContext)
}