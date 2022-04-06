package com.skyautonet.seda_aiv.rest

import com.skyautonet.seda_aiv.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface SAAppInterface {

    //alert.php
    @GET("alert2.php")
    fun alert(): Call<AlertResponse>

    //date.php
    @GET("VideoList.php")
    fun video_list(
        @Query("video_type") video_type: Int
    ): Call<VideoListResponse>

}