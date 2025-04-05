package com.example.antiquecollector.ui.screens.additem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antiquecollector.domain.model.Antique
import com.example.antiquecollector.domain.model.Category
import com.example.antiquecollector.domain.model.Condition
import com.example.antiquecollector.domain.usecase.antique.AntiqueUseCases
import com.example.antiquecollector.domain.usecase.category.CategoryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val antiqueUseCases: AntiqueUseCases,
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    // UI state for the form
    private val _uiState = MutableStateFlow(AddItemUiState())
    val uiState: StateFlow<AddItemUiState> = _uiState.asStateFlow()

    // Available categories for dropdown
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    // Loading and error states
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                categoryUseCases.getCategories().collect { categoryList ->
                    _categories.value = categoryList
                }
            } catch (e: Exception) {
                _error.value = "Failed to load categories: ${e.message}"
            }
        }
    }

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun onCategoryChange(categoryId: Long) {
        _uiState.value = _uiState.value.copy(categoryId = categoryId)
    }

    fun onAcquisitionDateChange(date: Date) {
        _uiState.value = _uiState.value.copy(acquisitionDate = date)
    }

    fun onValueChange(value: String) {
        try {
            val doubleValue = value.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 0.0
            _uiState.value = _uiState.value.copy(value = doubleValue, valueText = value)
        } catch (e: Exception) {
            // Keep the text but don't update the numeric value
            _uiState.value = _uiState.value.copy(valueText = value)
        }
    }

    fun onConditionChange(condition: Condition) {
        _uiState.value = _uiState.value.copy(condition = condition)
    }

    fun onDescriptionChange(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun onLocationChange(location: String) {
        _uiState.value = _uiState.value.copy(location = location)
    }

    fun onPhotoAdded(uri: String) {
        val currentPhotos = _uiState.value.photos.toMutableList()
        currentPhotos.add(uri)
        _uiState.value = _uiState.value.copy(photos = currentPhotos)
    }

    fun saveItem(onSuccess: () -> Unit) {
        val currentState = _uiState.value

        // Validate form
        if (!isFormValid()) {
            _error.value = "Please fill out all required fields."
            return
        }

        _isLoading.value = true

        val category = _categories.value.find { it.id == currentState.categoryId.toString() }

        val newAntique = Antique(
            name = currentState.name,
            category = category,
            acquisitionDate = currentState.acquisitionDate ?: Date(),
            currentValue = currentState.value,
            condition = currentState.condition,
            description = currentState.description.takeIf { it.isNotBlank() },
            location = currentState.location.takeIf { it.isNotBlank() },
            images = currentState.photos,
            lastModified = Date()
        )

        viewModelScope.launch {
            try {
                antiqueUseCases.addAntiqueUseCase.invoke(newAntique)
                _isLoading.value = false
                onSuccess()
            } catch (e: Exception) {
                _error.value = "Failed to save item: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    private fun isFormValid(): Boolean {
        val state = _uiState.value
        return state.name.isNotBlank() &&
                state.categoryId != null &&
                state.acquisitionDate != null &&
                state.value > 0
    }
}

data class AddItemUiState(
    val name: String = "",
    val categoryId: Long? = null,
    val acquisitionDate: Date? = null,
    val value: Double = 0.0,
    val valueText: String = "",
    val condition: Condition = Condition.GOOD,
    val description: String = "",
    val location: String = "",
    val photos: List<String> = emptyList()
)