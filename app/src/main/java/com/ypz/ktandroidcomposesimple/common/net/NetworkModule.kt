package com.ypz.ktandroidcomposesimple.common.net

import com.ypz.ktandroidcomposesimple.net.wanAndroid.api.WanAndroidNetService
import com.ypz.ktandroidcomposesimple.net.wanAndroid.client.WanAndroidApiCall
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

//这里使用了SingletonComponent，因此 NetworkModule 绑定到 Application 的生命周期
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideApiService(): WanAndroidNetService = WanAndroidApiCall.netService

    @Singleton
    @Provides
    fun provideOkHttp(): OkHttpClient = WanAndroidApiCall.okHttp
}