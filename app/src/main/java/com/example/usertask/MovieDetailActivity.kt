package com.example.usertask

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.usertask.databinding.ActivityMovieDetailBinding
import com.example.usertask.model.MovieDetail
import com.example.usertask.util.MovieDetailState
import com.example.usertask.viewmodel.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {
    private val viewModel: MovieDetailViewModel by viewModels()
    private lateinit var binding: ActivityMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        observeViewModel()
        loadMovieDetails()
    }

    private fun setupUI() {
        enableEdgeToEdge()
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun observeViewModel() {
        viewModel.movieDetailState.observe(this) { state ->
            when (state) {
                is MovieDetailState.Loading -> showLoading()
                is MovieDetailState.Success -> showMovieDetails(state.movie)
                is MovieDetailState.Error -> showError(state.message)
            }
        }
    }

    private fun loadMovieDetails() {
        val apiKey = BuildConfig.TMDB_API_KEY
        val movieId = intent.getIntExtra("id", 0)
        viewModel.loadMovieDetails(movieId ,apiKey)
    }

    private fun showLoading() {
        binding.posterProgressBar.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        binding.posterProgressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showMovieDetails(movie: MovieDetail) {
        with(binding) {
            movieTitle.text = movie.title
            movieReleaseDate.text = "Release Date: ${movie.release_date}"
            movieDescription.text = movie.overview
            movie.poster_path?.let { loadMoviePoster(it) }
        }
    }

    private fun loadMoviePoster(posterPath: String) {
        val imageUrl = "https://image.tmdb.org/t/p/w500$posterPath"

        Glide.with(this)
            .load(imageUrl)
            .timeout(60000)
            .listener(createGlideListener())
            .into(binding.moviePoster)
    }

    private fun createGlideListener() = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>,
            isFirstResource: Boolean
        ): Boolean {
            binding.posterProgressBar.visibility = View.GONE
            showError("Failed to load movie poster")
            return false
        }

        override fun onResourceReady(
            resource: Drawable,
            model: Any,
            target: Target<Drawable>?,
            dataSource: DataSource,
            isFirstResource: Boolean
        ): Boolean {
            binding.posterProgressBar.visibility = View.GONE
            return false
        }
    }
}
