package com.example.usertask

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.room.Room
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.usertask.databinding.ActivityAddUserBinding
import com.example.usertask.retrofit.SyncWorker
import com.example.usertask.viewmodel.AddUserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class AddUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddUserBinding
    private val viewModel: AddUserViewModel by viewModels()
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    private val connectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        observeUiState()
        setupNetworkCallback()
        scheduleSyncWork()
    }

    private fun setupClickListeners() {
        binding.btnCreateUser.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val job = binding.etJob.text.toString().trim()

            if (name.isNotEmpty() && job.isNotEmpty()) {
                viewModel.createUser(name, job, checkInternetConnection())
                binding.etName.text?.clear()
                binding.etJob.text?.clear()
            } else {
                showToast("Please enter both name and job")
            }
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is AddUserViewModel.UiState.Loading -> showLoading(true)
                        is AddUserViewModel.UiState.Success -> {
                            showLoading(false)
                            showToast(
                                if (state.isOnline) "User created and synced with server"
                                else "User saved offline. Will sync when online"
                            )
                        }
                        is AddUserViewModel.UiState.Error -> {
                            showLoading(false)
                            showToast("Error: ${state.message}")
                        }
                        is AddUserViewModel.UiState.Idle -> {
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }

    private fun scheduleSyncWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                "syncUsers",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }

    private fun setupNetworkCallback() {
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)

                Log.d("NetworkCallback", "Internet available. Starting sync...")
                val workRequest = OneTimeWorkRequestBuilder<SyncWorker>().build()
                WorkManager.getInstance(applicationContext).enqueueUniqueWork(
                    "immediateSync",
                    ExistingWorkPolicy.REPLACE,
                    workRequest
                )
                runOnUiThread { showToast("Connected to network. Syncing data...") }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.d("NetworkCallback", "Internet lost")
                runOnUiThread { showToast("Network lost. Data will sync when online.") }
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.btnCreateUser.isEnabled = !isLoading
    }

    private fun checkInternetConnection(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}

