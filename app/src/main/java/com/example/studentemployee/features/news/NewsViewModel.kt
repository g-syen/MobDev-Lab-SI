package com.example.studentemployee.features.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentemployee.features.news.model.News
import com.example.studentemployee.repository.FirestoreRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow


class NewsViewModel : ViewModel() {
    private val repository = FirestoreRepository()

    private val _news = MutableStateFlow<List<News>>(emptyList())
    val news: StateFlow<List<News>> = _news

    fun loadAllNews() {
        viewModelScope.launch {
            repository.getAllNews().collect {
                _news.value = it
            }
        }
    }

    fun addNews(
        news: News,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        repository
            .addNews(news = news)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun updateNews(
        news: News,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        repository
            .updateNews(news = news)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun deleteNews(
        newsId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        repository.deleteNews(newsId, onSuccess, onError)
    }
}