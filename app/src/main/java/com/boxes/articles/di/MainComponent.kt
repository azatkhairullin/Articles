package com.boxes.articles.di

import com.boxes.articles.data.source.RemoteSource
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(UtilsModule::class, DataSourcesModule::class ))
interface MainComponent {
    fun provideRemoteSource(): RemoteSource
    fun provideLocationProviderClient() : FusedLocationProviderClient
}