package com.dicoding.myintermediateapplication.view.main


import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myintermediateapplication.R
import com.dicoding.myintermediateapplication.databinding.ActivityMainBinding
import com.dicoding.myintermediateapplication.view.ViewModelFactory
import com.dicoding.myintermediateapplication.view.adapter.LoadingStateAdapter
import com.dicoding.myintermediateapplication.view.adapter.StoryAdapter
import com.dicoding.myintermediateapplication.view.map.MapsActivity
import com.dicoding.myintermediateapplication.view.upload.UploadActivity
import com.dicoding.myintermediateapplication.view.welcome.WelcomeActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title= getString(R.string.app_name)

        setupView()
        setupAction()
        setupRecyclerView()


        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }else{
                val token = user.token
                val adapter = StoryAdapter()
                binding.RecycleView.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        adapter.retry()
                    }
                )
                viewModel.getStoryPaging(token).observe(this, {
                    adapter.submitData(lifecycle, it)
                })
            }
        }


//        viewModel.setStories()
//        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun setupAction() {
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, UploadActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_list ->{
                viewModel.logout()
                val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.maps ->{
                startActivity(Intent(this,MapsActivity::class.java))
                return true
            }else->{
                return super.onOptionsItemSelected(item)
            }
        }
    }


    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.RecycleView.layoutManager = layoutManager

        storyAdapter = StoryAdapter()
        binding.RecycleView.adapter = storyAdapter.withLoadStateHeaderAndFooter(
            header = LoadingStateAdapter { storyAdapter.retry() },
            footer = LoadingStateAdapter { storyAdapter.retry() }
        )

        // Menggunakan collectLatest untuk mengamati perubahan load state dari adapter
        lifecycleScope.launch {
            storyAdapter.loadStateFlow.collectLatest { loadStates ->
                if (loadStates.refresh is LoadState.Loading) {
                    // Handle loading state
                } else if (loadStates.refresh is LoadState.Error) {
                    // Handle error state
                }
                // Handle other states as needed
            }
        }
    }
}