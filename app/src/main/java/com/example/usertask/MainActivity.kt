package com.example.usertask

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.usertask.adapter.UserAdapter
import com.example.usertask.databinding.ActivityMainBinding
import com.example.usertask.repo.UserRepository
import com.example.usertask.util.NetworkUtils
import com.example.usertask.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: UserViewModel by viewModels()

    private val userAdapter = UserAdapter { user ->
        val intent = Intent(this, MovieListActivity::class.java)
        intent.putExtra("userId", user.id)
        startActivity(intent)
    }

    var isNetworkAvailable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val networkUtil = NetworkUtils(this@MainActivity)
        isNetworkAvailable = networkUtil.isOnline()
        if (!isNetworkAvailable) {
            Toast.makeText(this@MainActivity, "Please Connect to network...", Toast.LENGTH_LONG).show()
        }

        setupRecyclerView()
        observeUsers()
        goToNextActivity()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
        }

        userAdapter.addLoadStateListener { loadState ->
            binding.progressBar.visibility =
                if (loadState.source.refresh is LoadState.Loading) View.VISIBLE else View.GONE
        }
    }

    private fun observeUsers() {
        lifecycleScope.launch {
            viewModel.userList.collectLatest { pagingData ->
                userAdapter.submitData(pagingData)
            }
        }
    }

    private fun goToNextActivity() {
        binding.fabBack.setOnClickListener {
            val intent = Intent(this@MainActivity, AddUserActivity::class.java)
            startActivity(intent)
        }
    }
}
