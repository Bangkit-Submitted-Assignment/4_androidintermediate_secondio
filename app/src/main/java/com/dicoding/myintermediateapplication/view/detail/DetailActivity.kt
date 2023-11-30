package com.dicoding.myintermediateapplication.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.myintermediateapplication.databinding.ActivityDetailBinding
import com.dicoding.myintermediateapplication.view.ViewModelFactory
import com.dicoding.myintermediateapplication.view.main.MainViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyId = intent.getStringExtra("STORY_ID")

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(MainViewModel::class.java)

        storyId?.let {
            viewModel.getStoryDetail(it)
        }

        viewModel.detail.observe(this) {
            binding.apply {
                name.text = it.story?.name
                description.text = it.story?.description
            }
            Glide.with(this)
                .load(it.story?.photoUrl)
                .into(binding.foto)
        }
    }
}