package com.boxes.articles.di

import com.boxes.articles.data.source.RemoteSource
import com.boxes.articles.presentation.presenter.ArticlesPresenter
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides

@Module
class ArticlesModule {
    @Provides
    @ArticlesScope
    fun provideArticlesPresenter(remoteSource: RemoteSource, locationProviderClient:FusedLocationProviderClient) : ArticlesPresenter {
        return ArticlesPresenter(remoteSource, locationProviderClient)
    }
}