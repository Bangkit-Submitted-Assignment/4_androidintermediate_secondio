package com.dicoding.myintermediateapplication.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
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

        // Mendapatkan storyId dari intent
        val storyId = intent.getStringExtra("STORY_ID")

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(MainViewModel::class.java)

        // Memanggil fungsi getStoryDetail dari ViewModel untuk mendapatkan detail cerita
        storyId?.let {
            viewModel.getStoryDetail(it)
        }

        // Observer untuk mendapatkan hasil dari getStoryDetail
        viewModel.detail.observe(this, { detailResponse ->
            // Lakukan apa yang diperlukan dengan detailResponse di sini
            // Contoh: Mengatur detail cerita ke tampilan
            binding.description.text = detailResponse?.story?.description ?: "No Description Available"
            binding.name.text = detailResponse?.story?.name ?: "No Description Available"
        })
    }
}