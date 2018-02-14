package com.boxes.articles.data.source

import com.boxes.articles.data.entity.GeoSearchResponse
import com.boxes.articles.data.entity.ImageTitlesReponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface RemoteSource {
    @GET("w/api.php?action=query&list=geosearch&gsradius=10000&gslimit=50&format=json")
    fun getArticles(@Query("gscoord") location:String): Single<GeoSearchResponse>

    @GET("w/api.php?action=query&prop=images&format=json&imlimit=500")
    fun getImageTitles(@Query("pageids") pageids:String, @Query("imcontinue") continueInfo:String? = null): Single<ImageTitlesReponse>
}