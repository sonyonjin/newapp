package com.skyautonet.seda_aiv.rest

import com.skyautonet.seda_aiv.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface SAAppInterface {

    //InfoList
    @FormUrlEncoded
    @POST("vehiclelist")
    fun infoAxon(
        @Field("start") page: Int,
        @Field("user_id") userId: String,
        @Field("status") type: String,
        @Field("search_keyword") searchKeyword: String,
        @Field("api_key") apiKey: String
    ): Call<InfoResponce>

}