package com.example.studentemployee.features.devotion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentemployee.repository.FirestoreRepository
import com.example.studentemployee.features.devotion.model.Devotion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DevotionViewModel:ViewModel() {
    private val repository = FirestoreRepository()
    private val _devotions = MutableStateFlow<List<Devotion>>(emptyList())
    val devotions: StateFlow<List<Devotion>> = _devotions

    fun loadUserDevotions(userId: String) {
        viewModelScope.launch {
            repository.getUserDevotions(userId = userId).collect {
                _devotions.value = it
            }
        }
    }

    fun addDevotion(
        userId: String,
        devotion: Devotion,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        repository
            .addDevotion(userId = userId, devotion = devotion)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun updateDevotion(
        userId: String,
        devotion: Devotion,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        repository
            .updateDevotion(userId = userId, devotion = devotion)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun deleteDevotion(
        userId: String,
        devotionId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        repository.deleteDevotion(userId, devotionId, onSuccess, onError)
    }


}