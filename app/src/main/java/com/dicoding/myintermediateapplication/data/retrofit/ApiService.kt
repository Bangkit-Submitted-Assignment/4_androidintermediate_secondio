package com.dicoding.myintermediateapplication.data.retrofit

import com.dicoding.myintermediateapplication.data.response.DetailResponse
import com.dicoding.myintermediateapplication.data.response.ListStoryResponse
import com.dicoding.myintermediateapplication.data.response.LoginResponse
import com.dicoding.myintermediateapplication.data.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(@Header("Authorization") token: String): ListStoryResponse

    @GET("stories/{storyId}")
    suspend fun getStoryDetail(
        @Header("Authorization") token: String,
        @Path("storyId") storyId: String
    ): DetailResponse
}