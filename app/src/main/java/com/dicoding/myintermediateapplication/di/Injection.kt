package com.dicoding.myintermediateapplication.di

import android.content.Context
import com.dicoding.myintermediateapplication.data.UserRepository
import com.dicoding.myintermediateapplication.data.pref.UserPreference
import com.dicoding.myintermediateapplication.data.pref.dataStore
import com.dicoding.myintermediateapplication.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val api = ApiConfig.getApiService()
        return UserRepository.getInstance(pref,api)
    }
}