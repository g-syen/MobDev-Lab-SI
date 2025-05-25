package com.example.studentemployee.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentemployee.core.database.AppDatabase
import com.example.studentemployee.features.search.model.SearchHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val dao = db.searchHistoryDao()

    private val _history = MutableStateFlow<List<SearchHistory>>(emptyList())
    val history: StateFlow<List<SearchHistory>> = _history

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            dao.getAllHistory().collect { result ->
                _history.value = result
            }
        }
    }

    fun addSearchQuery(query: String) {
        viewModelScope.launch {
            dao.insertSearch(SearchHistory(query = query))
            loadHistory()
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            dao.clearHistory()
            loadHistory()
        }
    }

    fun deleteSearchQuery(item: SearchHistory) {
        viewModelScope.launch {
            dao.deleteSearch(item)
            loadHistory()
        }
    }

}
