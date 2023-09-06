package com.saxipapsi.weathermap.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.saxipapsi.weathermap.databinding.ActivityManageLocationBinding
import com.saxipapsi.weathermap.domain.use_case.geo.SelectedLocationModel
import com.saxipapsi.weathermap.presentation.geo.LocationViewModel
import com.saxipapsi.weathermap.presentation.geo.components.OnLocationManageListener
import com.saxipapsi.weathermap.presentation.geo.components.SelectedLocationAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ManageLocationActivity : AppCompatActivity(), OnLocationManageListener {

    override fun onSelect(searchLocationModel: SelectedLocationModel) { locationViewModel.onManageSelect(searchLocationModel.id.toLong()) }
    override fun onDeSelect(searchLocationModel: SelectedLocationModel) { locationViewModel.onManageDeSelect(searchLocationModel.id.toLong()) }

    private lateinit var binding: ActivityManageLocationBinding
    private val locationViewModel: LocationViewModel by inject()
    private val recentSearchAdapter: SelectedLocationAdapter by lazy { SelectedLocationAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivBack.setOnClickListener { finish() }
        binding.rvSelectedLocations.adapter = recentSearchAdapter
        binding.tTextView.setOnClickListener { SearchLocationActivity.start(this) }
        binding.eTextView.setOnClickListener { SearchLocationActivity.start(this) }
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        locationViewModel.allSelectedLocationsState()
    }

    private fun initObservers() {
        lifecycleScope.launch { observeRecentSearch() }
        lifecycleScope.launch { observeManage() }
    }

    private suspend fun observeRecentSearch() {
        locationViewModel.allSelectedLocationsState.collectLatest { result ->
            binding.loading.loading.visibility = if (result.isLoading) View.VISIBLE else View.GONE
            binding.loading.tvError.visibility = if (result.error.isNotEmpty()) View.VISIBLE else View.GONE
            binding.loading.tvError.text = result.error
            recentSearchAdapter.differ.submitList(result.data)
        }
    }

    private suspend fun observeManage(){
        locationViewModel.manageState.collectLatest { result ->
            if (result.error.isNotEmpty()) Toast.makeText(this@ManageLocationActivity, result.error, Toast.LENGTH_LONG).show()
            if(result.data) finish()
        }
    }

    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, ManageLocationActivity::class.java))
    }

}