package com.cesar.storicesar.di

import com.cesar.storicesar.data.repository.MovementRepository
import com.cesar.storicesar.data.repository.MovementRepositoryImpl
import com.cesar.storicesar.data.repository.UserRepository
import com.cesar.storicesar.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    abstract fun bindMovementRepo(impl: MovementRepositoryImpl): MovementRepository

    @Binds
    abstract fun bindUserRepo(impl: UserRepositoryImpl): UserRepository
}