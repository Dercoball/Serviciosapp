package com.example.serviciosapp.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.serviciosapp.data.model.ServiceProvider
import com.example.serviciosapp.data.sample.SampleData

class HomeViewModel(
    sampleData: SampleData
) : ViewModel() {

    val categories: List<String> = sampleData.categories
    private val allProviders: List<ServiceProvider> = sampleData.providers

    var searchQuery by mutableStateOf("")
        private set
    var selectedCategory by mutableStateOf<String?>(null)
        private set
    var providers by mutableStateOf(allProviders)
        private set

    fun onSearchChange(value: String) {
        searchQuery = value
        filter()
    }

    fun onCategorySelected(category: String) {
        selectedCategory = if (selectedCategory == category) null else category
        filter()
    }

    private fun filter() {
        providers = allProviders.filter { provider ->
            val matchesCategory = selectedCategory?.let { provider.category.equals(it, ignoreCase = true) } ?: true
            val matchesQuery = searchQuery.isBlank() || provider.name.contains(searchQuery, ignoreCase = true) ||
                provider.trade.contains(searchQuery, ignoreCase = true) ||
                provider.city.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }
}
