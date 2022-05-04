package com.skyautonet.seda_aiv.rest

import com.skyautonet.seda_aiv.BuildConfig
import com.skyautonet.seda_aiv.util.RoomDatabaseUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object {
        fun getSAAppInterface(): SAAppInterface {
            val okClient = OkHttpClient.Builder()
            okClient.connectTimeout(5000, TimeUnit.MILLISECONDS)
            okClient.writeTimeout(5000, TimeUnit.MILLISECONDS)
            okClient.readTimeout(5000, TimeUnit.MILLISECONDS)

            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                okClient.interceptors().add(interceptor)
            }

            okClient.interceptors().add(Interceptor { chain ->
                val response = chain.proceed(chain.request())
                response.newBuilder()
                    .header("Cache-Control", "only-if-cached")
//                        .header("Accept", "application/rss+xml")
//                        .header("Content-type", "application/rss+xml")
//                        .header("Scope", "Bearer iwRduC25KbSn4FfzGNcYkraOcRcKRrJ0UrTB5aatKAHXbgJCf9YnY13c4OMD")
                    .header("content-type", "text/html; charset=UTF-8")
                    .build()
                response

            })

            val baseUrl = "http://${RoomDatabaseUtil.getApplicationConfigData().ipAddress}/"

            val client = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okClient.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return client.create(SAAppInterface::class.java)
        }
    }
}