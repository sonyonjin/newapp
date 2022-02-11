package com.skyautonet.seda_aiv.dagger.module

import com.skyautonet.seda_aiv.rest.SAAppInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetModule::class])
class APIModule {
    @Provides
    @Singleton
    fun provideSbAppInterface(retrofit: Retrofit): SAAppInterface {
        return retrofit.create(SAAppInterface::class.java)
    }

}