package com.boxes.articles.di

import com.boxes.articles.data.source.RemoteSourceFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataSourcesModule() {

    @Provides
    @Singleton
    fun provideRemoteSource() = RemoteSourceFactory.create()
}