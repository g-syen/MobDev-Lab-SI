package com.example.studentemployee.features.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentemployee.features.devotion.model.Devotion
import com.example.studentemployee.features.events.model.Event
import com.example.studentemployee.features.members.model.User
import com.example.studentemployee.repository.FirestoreRepository
import com.example.studentemployee.features.news.model.News
import com.example.studentemployee.features.research.model.Research
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContentViewModel:ViewModel() {
    private val repository = FirestoreRepository()
    private val currentUserID = FirebaseAuth.getInstance().currentUser?.uid

    private val _news = MutableStateFlow<List<News>>(emptyList())
    val news: StateFlow<List<News>> = _news

    private val _event = MutableStateFlow<List<Event>>(emptyList())
    val event: StateFlow<List<Event>> = _event

    private val _research = MutableStateFlow<List<Research>>(emptyList())
    val research: StateFlow<List<Research>> = _research

    private val _devotion = MutableStateFlow<List<Devotion>>(emptyList())
    val devotion: StateFlow<List<Devotion>> = _devotion

    private val _user = MutableStateFlow<User>(User())
    val user: StateFlow<User> = _user

    init {
        fetchNews()
        fetchEvents()
        fetchResearches()
        fetchDevotions()
        if (currentUserID != null) {
            fetchUserByID(currentUserID)
        }
    }

    private fun fetchUserByID(currentUserID : String) {
        viewModelScope.launch {
            repository.getUserById(uid = currentUserID).collect { user ->
                if (user != null) {
                    _user.value = user
                }
            }
        }
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