package com.skyautonet.seda_aiv.rest

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object {
        const val BASE_URL_APP ="http://axonfms.com/services/"

        fun getSAAppInterface(): SAAppInterface {
            val okClient = OkHttpClient.Builder()
            okClient.connectTimeout(60000, TimeUnit.MILLISECONDS)
            okClient.writeTimeout(60000, TimeUnit.MILLISECONDS)
            okClient.readTimeout(60000, TimeUnit.MILLISECONDS)

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            okClient.interceptors().add(interceptor)

            okClient.interceptors().add(Interceptor { chain ->
                val response = chain.proceed(chain.request())
                response.newBuilder()
                    .header("Cache-Control", "only-if-cached")
//                        .header("Accept", "application/rss+xml")
//                        .header("Content-type", "application/rss+xml")
//                        .header("Scope", "Bearer iwRduC25KbSn4FfzGNcYkraOcRcKRrJ0UrTB5aatKAHXbgJCf9YnY13c4OMD")
                    .build()
                response

            })


            val client = Retrofit.Builder()
                .baseUrl(BASE_URL_APP)
                .client(okClient.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return client.create(SAAppInterface::class.java)
        }
    }
}