package com.example.studentemployee.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentemployee.data.Devotion
import com.example.studentemployee.data.Event
import com.example.studentemployee.data.FirestoreRepository
import com.example.studentemployee.data.News
import com.example.studentemployee.data.Research
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContentViewModel:ViewModel() {
    private val repository = FirestoreRepository()

    private val _news = MutableStateFlow<List<News>>(emptyList())
    val news: StateFlow<List<News>> = _news

    private val _event = MutableStateFlow<List<Event>>(emptyList())
    val event: StateFlow<List<Event>> = _event

    private val _research = MutableStateFlow<List<Research>>(emptyList())
    val research: StateFlow<List<Research>> = _research

    private val _devotion = MutableStateFlow<List<Devotion>>(emptyList())
    val devotion: StateFlow<List<Devotion>> = _devotion

    init {
        fetchNews()
        fetchEvents()
        fetchResearches()
        fetchDevotions()
    }

    private fun fetchNews() {
        viewModelScope.launch {
            repository.getAllNews().collect { newsList ->
                _news.value = newsList
            }
        }
    }

    private fun fetchEvents() {
        viewModelScope.launch {
            repository.getAllEvents().collect { eventList ->
                _event.value = eventList
            }
        }
    }

    private fun fetchResearches() {
        viewModelScope.launch {
            repository.getAllUserResearches().collect { researchList ->
                _research.value = researchList
            }
        }
    }

    private fun fetchDevotions() {
        viewModelScope.launch {
            repository.getAllUserDevotions().collect { devotionList ->
                _devotion.value = devotionList
            }
        }
    }
}