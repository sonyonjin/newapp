package com.skyautonet.seda_aiv.rest

import com.skyautonet.seda_aiv.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface SAAppInterface {

    @GET("Alert.php")
    fun alert(): Call<AlertResponse>

    @GET("VideoList.php")
    fun video_list(
        @Query("video_type") video_type: Int
    ): Call<VideoListResponse>

    @GET("DownloadVideo.php")
    fun download_video(
        @Query("file_name")  file_name: String
    ): Call<ResponseBody>

    @GET("CalibRegi.php")
    fun calib_regi(): Call<CalibRegiResponse>

    @GET("GetCameraChannel.php")
    fun get_camera_channel(): Call<GetCameraChannelResponse>

    @GET("SetCameraChannel.php")
    fun set_camera_channel(
        @Query("channel_mode") channel_mode: Int
    ): Call<SetCameraChannelResponse>
}