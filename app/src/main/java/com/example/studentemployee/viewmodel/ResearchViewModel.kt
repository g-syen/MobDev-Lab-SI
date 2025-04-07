package com.example.studentemployee.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentemployee.data.FirestoreRepository
import com.example.studentemployee.data.Research
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow


class ResearchViewModel : ViewModel() {
    private val repository = FirestoreRepository()

    private val _researches = MutableStateFlow<List<Research>>(emptyList())
    val researches: StateFlow<List<Research>> = _researches

    fun loadUserResearches(userId: String) {
        viewModelScope.launch {
            repository.getUserResearches(userId = userId).collect {
                _researches.value = it
            }
        }
    }

    fun addResearch(
        userId: String,
        research: Research,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        repository
            .addResearch(userId = userId, research = research)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun updateResearch(
        userId: String,
        research: Research,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        repository
            .updateResearch(userId = userId, research = research)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun deleteResearch(
        userId: String,
        researchId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        repository.deleteResearch(userId, researchId, onSuccess, onError)
    }
}