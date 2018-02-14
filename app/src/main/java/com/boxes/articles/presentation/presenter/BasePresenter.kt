package com.boxes.articles.presentation.presenter

import com.boxes.articles.presentation.view.BaseView


interface BasePresenter<in T : BaseView> {
    fun onViewStart()
    fun onViewStop()

    fun onViewAttach(view: T)
    fun onViewDetach()
}