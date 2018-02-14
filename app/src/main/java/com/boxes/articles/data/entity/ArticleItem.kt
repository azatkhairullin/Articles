package com.boxes.articles.data.entity

import com.google.gson.annotations.SerializedName


class ArticleItem {
    @SerializedName("pageid")
    lateinit var pageId:String
    var title:String? = null
    var images:Array<ImageItem>? = null
}