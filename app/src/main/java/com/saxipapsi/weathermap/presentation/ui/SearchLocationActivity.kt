package com.saxipapsi.weathermap.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.saxipapsi.weathermap.databinding.ActivitySearchLocationBinding
import com.saxipapsi.weathermap.domain.use_case.geo.SearchLocationModel
import com.saxipapsi.weathermap.presentation.geo.LocationViewModel
import com.saxipapsi.weathermap.presentation.geo.components.OnLocationSearchListener
import com.saxipapsi.weathermap.presentation.geo.components.OnRecentSearchVisibilityListener
import com.saxipapsi.weathermap.presentation.geo.components.SearchLocationAdapter
import com.saxipapsi.weathermap.presentation.geo.components.SelectedLocationAdapter
import com.saxipapsi.weathermap.utility.extension.hideKeyboard
import com.saxipapsi.weathermap.utility.extension.showKeyboard
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SearchLocationActivity : AppCompatActivity(), OnLocationSearchListener, OnRecentSearchVisibilityListener {
    private lateinit var binding: ActivitySearchLocationBinding
    private val locationViewModel: LocationViewModel by inject()
    private val searchAdapter: SearchLocationAdapter by lazy { SearchLocationAdapter(this) }
    private val recommendedLocationsAdapter: SelectedLocationAdapter by lazy { SelectedLocationAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivBack.setOnClickListener { finish() }
        binding.rvSearch.adapter = searchAdapter
        binding.rvRecommendedLocations.adapter = recommendedLocationsAdapter
        initObservers()
        binding.eTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { locationViewModel.searchLocation(s.toString().lowercase()) }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        binding.eTextView.showKeyboard()
    }

    override fun onPause() {
        super.onPause()
        binding.eTextView.hideKeyboard()
    }

    private fun initObservers() {
        lifecycleScope.launch { observeSelectSearchState() }
        lifecycleScope.launch { observeSearchResult() }
    }

    private suspend fun observeSearchResult() {
        locationViewModel.state.collectLatest { result ->
            binding.loading.loading.visibility = if (result.isLoading) View.VISIBLE else View.GONE
            binding.loading.tvError.visibility = if (result.error.isNotEmpty()) View.VISIBLE else View.GONE
            binding.loading.tvError.text = result.error
            searchAdapter.differ.submitList(result.data)
            onVisibilityChanged(searchAdapter.differ.currentList.isEmpty())
        }
    }

    private suspend fun observeSelectSearchState() {
        locationViewModel.selectionSearchState.collectLatest { result -> if (result.data) finish() }
    }

    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, SearchLocationActivity::class.java))
    }
    override fun onVisibilityChanged(isVisible: Boolean) { binding.rvRecommendedLocations.visibility = if(isVisible) View.VISIBLE else View.GONE }
    override fun onSelectSearch(searchLocationModel: SearchLocationModel) { locationViewModel.onSearchSelect(searchLocationModel) }
}