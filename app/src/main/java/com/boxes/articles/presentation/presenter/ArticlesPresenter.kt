package com.boxes.articles.presentation.presenter;

import android.annotation.SuppressLint
import com.boxes.articles.data.entity.ArticleItem
import com.boxes.articles.data.entity.ContinueItem
import com.boxes.articles.data.source.RemoteSource
import com.boxes.articles.presentation.ArticlesContract
import com.google.android.gms.location.FusedLocationProviderClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class ArticlesPresenter(private val remoteSource: RemoteSource,
                        private val locationProviderClient: FusedLocationProviderClient) : ArticlesContract.Presenter {

    private var view: ArticlesContract.View? = null
    private var currentData: List<ArticleItem> = listOf()
    private var currentContinueInfo: ContinueItem? = null
    private var isLoadingOnProgress: Boolean = false
    private var disposable: Disposable? = null

    override fun onViewAttach(view: ArticlesContract.View) {
        this.view = view
    }

    override fun onViewDetach() {
        view = null
    }

    override fun onViewStart() {
        view?.let {
            if (!currentData.isEmpty()) {
                it.showContent()
                it.updateItems(currentData)
            }

            refreshData(false)
        }
    }

    override fun onViewStop() {
        disposable?.dispose()
    }

    override fun retry() {
        refreshData(true)
    }

    private fun refreshData(force: Boolean) {
        if (!isLoadingOnProgress) {
            if (currentContinueInfo != null) {
                loadImagesData(currentContinueInfo)
            } else {
                if (currentData.isEmpty() || force) {
                    fetchLocationAndLoadData()
                }
            }
        }
    }

    private fun getPageIds(articleItems: List<ArticleItem>): String {
        return articleItems.fold("", {result, item-> result + item.pageId + '|'}).removeSuffix("|")
    }


    @SuppressLint("MissingPermission")
    private fun fetchLocationAndLoadData() {
        showProgress()
        locationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                loadGeoSearchData(location.latitude, location.longitude)
            } else {
                loadGeoSearchData(0.0, 0.0)
                showError()
            }
        }.addOnFailureListener {
            loadGeoSearchData(0.0, 0.0)
            showError()
            it.printStackTrace()
        }
    }

    private fun loadGeoSearchData(latitude: Double, longitude: Double) {
        showProgress()
        isLoadingOnProgress = true
        disposable = remoteSource.getArticles("$latitude|$longitude")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //в новые айтемы вставляем старые картинки, пока грузятся новые картинки
                    //когда мы рефрешим
                    val oldData = currentData
                    currentData = it.query.geosearch.filter { it.title != null }
                    updateImagesData(oldData)
                    refreshView()
                    isLoadingOnProgress = false
                    loadImagesData()
                }, {
                    isLoadingOnProgress = false
                    showError()
                    it.printStackTrace()
                })
    }

    private fun loadImagesData(continueInfo: ContinueItem? = null) {
        isLoadingOnProgress = true
        disposable = remoteSource.getImageTitles(getPageIds(currentData), continueInfo?.continueKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data ->
                    currentContinueInfo = data.continueData
                    updateImagesData(data.query.pages.values.filter { item -> item.title != null }.toList())
                    refreshView()
                    isLoadingOnProgress = false
                    if (data.continueData != null) {
                        loadImagesData(data.continueData)
                    }
                }, {
                    isLoadingOnProgress = false
                    showError()
                    it.printStackTrace()
                })
    }

    private fun updateImagesData(newData: List<ArticleItem>) {
        newData.forEach { newItem ->
            currentData.forEach { oldItem ->
                if (newItem.pageId == oldItem.pageId && newItem.images != null) {
                    oldItem.images = newItem.images
                }
            }
        }
    }

    private fun showProgress() {
        if (currentData.isEmpty())
            view?.showProgress()
    }

    private fun showError() {
        view?.let { view ->
            if (currentData.isEmpty())
                view.showError()
            else
                view.showUpdateError()
        }

    }

    private fun refreshView() {
        view?.let { view ->
            if (currentData.isEmpty()) {
                view.showEmpty()
            } else {
                view.showContent()
                view.updateItems(currentData)
            }
        }

    }


}
