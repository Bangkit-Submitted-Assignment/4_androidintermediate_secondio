package com.dicoding.myintermediateapplication.data

import android.util.Log
import com.dicoding.myintermediateapplication.data.pref.UserModel
import com.dicoding.myintermediateapplication.data.pref.UserPreference
import com.dicoding.myintermediateapplication.data.response.DetailResponse
import com.dicoding.myintermediateapplication.data.response.ListStoryResponse
import com.dicoding.myintermediateapplication.data.response.LoginResponse
import com.dicoding.myintermediateapplication.data.response.RegisterResponse
import com.dicoding.myintermediateapplication.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
    private var token: String?=null
) {

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun getStory():ListStoryResponse{
        try {
             token = userPreference.getToken()
        }catch (e:Exception){
            Log.e("User Repo ", e.toString())
        }
        return apiService.getStories("Bearer $token")
    }

    suspend fun getStoriesWithLocation(): ListStoryResponse {
        try {
            token = userPreference.getToken()
        }catch (e:Exception){
            Log.e("User Repo ", e.toString())
        }
        return apiService.getStoriesWithLocation("Bearer $token",1)
    }


    suspend fun getStoryDetail(storyId: String): DetailResponse? {
        var detailResponse: DetailResponse? = null
        try {
            token = userPreference.getToken()
            if (!token.isNullOrEmpty()) {
                detailResponse = apiService.getStoryDetail("Bearer $token", storyId)
            }
        } catch (e: Exception) {
            Log.e("User Repo ", e.toString())
        }
        return detailResponse
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}