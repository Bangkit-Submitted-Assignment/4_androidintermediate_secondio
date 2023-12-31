package com.dicoding.myintermediateapplication.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.dicoding.myintermediateapplication.data.UserRepository
import com.dicoding.myintermediateapplication.data.pref.UserModel
import com.dicoding.myintermediateapplication.data.response.DetailResponse
import com.dicoding.myintermediateapplication.data.response.ListStoryItem
import com.dicoding.myintermediateapplication.data.response.ListStoryResponse
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>>
        get() = _stories

    private val _detail = MutableLiveData<DetailResponse>()
    val detail: LiveData<DetailResponse>
        get() = _detail

    private val _maps = MutableLiveData<ListStoryResponse>()
    val maps: LiveData<ListStoryResponse>
        get() = _maps

    fun getStoryPaging(token:String): LiveData<PagingData<ListStoryItem>> {
        return repository.getStoryPaging(token)
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getStoryDetail(storyId: String) {
        viewModelScope.launch {
            try {
                val detailResponse = repository.getStoryDetail(storyId)
                _detail.value = detailResponse!!
            } catch (e: Exception) {

            }
        }
    }

    fun getStoryWithLocation() {
        viewModelScope.launch {
            try {
                val mapsResponse = repository.getStoriesWithLocation()
                _maps.value = mapsResponse
            } catch (e: Exception) {
                Log.e("API Error", "Error fetching stories with location: ${e.message}")
            }
        }
    }
}