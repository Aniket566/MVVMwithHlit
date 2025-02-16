package com.example.usertask

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.usertask.adapter.MovieAdapter
import com.example.usertask.adapter.MovieLoadStateAdapter
import com.example.usertask.databinding.ActivityMovieListBinding
import com.example.usertask.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieListBinding
    private lateinit var movieAdapter: MovieAdapter
    private val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        observeMovies()
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter { movie ->
            val intent = Intent(this, MovieDetailActivity::class.java).apply {
                putExtra("id", movie.id)
            }
            startActivity(intent)
        }

        binding.moviesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MovieListActivity)
            adapter = movieAdapter.withLoadStateFooter(
                footer = MovieLoadStateAdapter { movieAdapter.retry() }
            )
        }
    }
    private fun observeMovies() {
        lifecycleScope.launch {
            viewModel.getMovies().collectLatest { pagingData ->
                movieAdapter.submitData(pagingData)
            }
        }

        movieAdapter.addLoadStateListener { loadState ->
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
        }
    }
}
