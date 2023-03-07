package com.verodigital.androidtask.di

import com.verodigital.androidtask.data.datasource.remote.api.LoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideLoginService(): LoginService{
        return LoginService.create()
    }


}