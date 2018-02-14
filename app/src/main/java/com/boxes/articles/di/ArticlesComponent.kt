package com.boxes.articles.di

import com.boxes.articles.presentation.view.ArticlesFragment
import dagger.Component


@ArticlesScope
@Component(dependencies = arrayOf(MainComponent::class), modules = arrayOf(ArticlesModule::class))
interface ArticlesComponent {
    fun injectArticlesFragment(articlesFragment: ArticlesFragment)
}