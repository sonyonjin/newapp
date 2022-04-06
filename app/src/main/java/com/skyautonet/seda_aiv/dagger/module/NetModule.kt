package com.skyautonet.seda_aiv.dagger.module

import android.app.Application
import android.text.TextUtils
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.skyautonet.seda_aiv.BuildConfig
import com.skyautonet.seda_aiv.common.CommonUtils
import com.skyautonet.seda_aiv.common.SharedPref
import com.skyautonet.seda_aiv.util.RoomDatabaseUtil

import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class NetModule() {
    @Provides
    @Singleton
    internal fun provideOkHttpCache(application:Application):Cache {

        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        return Cache(application.cacheDir, cacheSize.toLong())
    }
    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val okClient = OkHttpClient.Builder()
        okClient.connectTimeout(30000, TimeUnit.MILLISECONDS)
        okClient.writeTimeout(30000, TimeUnit.MILLISECONDS)
        okClient.readTimeout(30000, TimeUnit.MILLISECONDS)

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            okClient.interceptors().add(interceptor)
        }

        okClient.interceptors().add(Interceptor { chain ->
            val response = chain.proceed(chain.request())
            response.newBuilder()
                .header("Cache-Control", "only-if-cached")
                .header("content-type", "text/html; charset=UTF-8")
                .build()
            response
        })
        return okClient.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        val baseUrl = if (TextUtils.isEmpty(RoomDatabaseUtil.getApplicationConfigData().ipAddress)) "http://localhost/" else
            "http://${RoomDatabaseUtil.getApplicationConfigData().ipAddress}/"
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
    }


    @Provides
    @Singleton
    internal fun provideCommonUtils(application: Application): CommonUtils {
        return CommonUtils(application)
    }
    @Provides
    @Singleton
    internal fun providePref(application:Application): SharedPref {
        return SharedPref(application)
    }

}