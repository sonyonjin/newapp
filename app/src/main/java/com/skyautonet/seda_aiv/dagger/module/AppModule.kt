package com.skyautonet.seda_aiv.dagger.module

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(application:Application) {
    private var mApplication: Application = application

    @Provides
    @Singleton
    internal fun provideApplication():Application {
        return mApplication
    }
}