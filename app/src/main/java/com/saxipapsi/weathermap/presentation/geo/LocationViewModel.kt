package com.saxipapsi.weathermap.presentation.geo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saxipapsi.weathermap.common.Resource
import com.saxipapsi.weathermap.domain.use_case.geo.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LocationViewModel( private val searchLocationUseCase: SearchLocationUseCase, private val manageSelectedLocationUseCase: ManageSelectedLocationUseCase, private val selectedLocationUseCase: SelectedLocationUseCase) : ViewModel() {

    private val _state = MutableStateFlow(SearchLocationState())
    val state: StateFlow<SearchLocationState> = _state
    fun searchLocation(search : String) = CoroutineScope(Dispatchers.IO).launch {
        searchLocationUseCase(search).onEach { resource ->
            when (resource) {
                is Resource.Loading -> { _state.value = SearchLocationState(isLoading = true) }
                is Resource.Error -> { _state.value = SearchLocationState(error = resource.message ?: "Unexpected error occured.") }
                is Resource.Success -> { _state.value = SearchLocationState(data = resource.data) }
            }
        }.launchIn(viewModelScope)
    }

    private val _selectionSearchState = MutableStateFlow(GeneralManageLocationState())
    val selectionSearchState: StateFlow<GeneralManageLocationState> = _selectionSearchState
    fun onSearchSelect(searchLocationModel : SearchLocationModel) = CoroutineScope(Dispatchers.IO).launch {
        manageSelectedLocationUseCase.onSearchSelected(searchLocationModel).onEach { resource ->
            when (resource) {
                is Resource.Loading -> { _selectionSearchState.value = GeneralManageLocationState(isLoading = true) }
                is Resource.Error -> { _selectionSearchState.value = GeneralManageLocationState(error = resource.message ?: "Unexpected error occured.") }
                is Resource.Success -> { _selectionSearchState.value = GeneralManageLocationState(data = resource.data ?: false) }
            }
        }.launchIn(viewModelScope)
    }

    private val _manageState = MutableStateFlow(GeneralManageLocationState())
    val manageState: StateFlow<GeneralManageLocationState> = _manageState
    fun onManageSelect(id : Long) = manage(id, true)
    fun onManageDeSelect(id : Long) = manage(id, false)
    private fun manage(id : Long , isSelected : Boolean = false) = CoroutineScope(Dispatchers.IO).launch {
        manageSelectedLocationUseCase.onManageSelectedById(id, isSelected).onEach { resource ->
            when (resource) {
                is Resource.Loading -> { _manageState.value = GeneralManageLocationState(isLoading = true) }
                is Resource.Error -> { _manageState.value = GeneralManageLocationState(error = resource.message ?: "Unexpected error occured.") }
                is Resource.Success -> { _manageState.value = GeneralManageLocationState(data = resource.data ?: false) }
            }
        }.launchIn(viewModelScope)
    }

    private val _allSelectedLocationsState = MutableStateFlow(AllSelectedLocationsState())
    val allSelectedLocationsState : StateFlow<AllSelectedLocationsState> = _allSelectedLocationsState.apply {  }
    fun allSelectedLocationsState() = CoroutineScope(Dispatchers.IO).launch {
        selectedLocationUseCase.getAllSelected().onEach { resource ->
               when(resource){
                   is Resource.Loading -> {_allSelectedLocationsState.value = AllSelectedLocationsState(isLoading = true)}
                   is Resource.Error -> {_allSelectedLocationsState.value = AllSelectedLocationsState(error = resource.message ?: "Unexpected error occured.")}
                   is Resource.Success -> {_allSelectedLocationsState.value = AllSelectedLocationsState(data = resource.data)}
               }
        }.launchIn(viewModelScope)
    }

    private val _currentSelectedLocationState = MutableStateFlow(CurrentSelectedLocationState())
    val currentSelectedLocationState : StateFlow<CurrentSelectedLocationState> = _currentSelectedLocationState.apply {  }
    fun currentSelectedLocationState() = CoroutineScope(Dispatchers.IO).launch {
        selectedLocationUseCase.getCurrentSelected().onEach { resource ->
            when(resource){
                is Resource.Loading -> {_currentSelectedLocationState.value = CurrentSelectedLocationState(isLoading = true)}
                is Resource.Error -> {_currentSelectedLocationState.value = CurrentSelectedLocationState(error = resource.message ?: "Unexpected error occured.")}
                is Resource.Success -> {_currentSelectedLocationState.value = CurrentSelectedLocationState(data = resource.data)}
            }
        }.launchIn(viewModelScope)
    }

}

data class SearchLocationState(
    val isLoading: Boolean = false,
    val data: List<SearchLocationModel>? = null,
    val error: String = ""
)

data class GeneralManageLocationState(
    val isLoading: Boolean = false,
    val data: Boolean = false,
    val error: String = ""
)

data class AllSelectedLocationsState(
    val isLoading: Boolean = false,
    val data: List<SelectedLocationModel>? = null,
    val error: String = ""
)

data class CurrentSelectedLocationState(
    val isLoading: Boolean = false,
    val data: SelectedLocationModel? = null,
    val error: String = ""
)