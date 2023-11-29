package com.dicoding.myintermediateapplication.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.myintermediateapplication.data.UserRepository
import com.dicoding.myintermediateapplication.data.pref.UserModel
import com.dicoding.myintermediateapplication.data.response.DetailResponse
import com.dicoding.myintermediateapplication.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>>
        get() = _stories

    private val _detail = MutableLiveData<DetailResponse>()
    val detail: LiveData<DetailResponse>
        get() = _detail

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun setStories() {
        viewModelScope.launch {
            try {
                val storiesResponse = repository.getStory()
                _stories.value = storiesResponse.listStory as List<ListStoryItem>?
            } catch (e: Exception) {
                Log.e("API Error", "Error fetching stories: ${e.message}")
            }
        }
    }

    fun getStoryDetail(storyId: String) {
        viewModelScope.launch {
            try {
                val detailResponse = repository.getStoryDetail(storyId)
                _detail.value = detailResponse!!
            } catch (e: Exception) {
                // Tambahkan penanganan error jika diperlukan
            }
        }
    }
}