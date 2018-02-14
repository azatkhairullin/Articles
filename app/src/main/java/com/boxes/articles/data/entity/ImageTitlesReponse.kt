package com.boxes.articles.data.entity

import com.google.gson.annotations.SerializedName


class ImageTitlesReponse {
    lateinit var query: ImageTitlesQuery

    @SerializedName("continue")
    var continueData: ContinueItem? = null
}