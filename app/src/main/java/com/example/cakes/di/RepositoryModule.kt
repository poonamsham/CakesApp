package com.example.cakes.di

import com.example.cakes.repository.CakeRepository
import com.example.cakes.repository.CakeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCakeRepository(
        implementation: CakeRepositoryImpl
    ): CakeRepository
}