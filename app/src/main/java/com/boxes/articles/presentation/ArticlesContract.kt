package com.boxes.articles.presentation

import com.boxes.articles.data.entity.ArticleItem
import com.boxes.articles.presentation.presenter.BasePresenter
import com.boxes.articles.presentation.view.BaseView


interface ArticlesContract {
    interface View : BaseView{
        fun updateItems(items:List<ArticleItem>)
        fun showContent()
        fun showEmpty()
        fun showProgress()
        fun showLocationError()
        fun showUpdateError()
        fun showError()

    }

    interface Presenter : BasePresenter<View> {
        fun retry()
    }
}