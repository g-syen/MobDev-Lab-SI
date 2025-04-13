package com.example.studentemployee.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentemployee.data.FirestoreRepository
import com.example.studentemployee.data.Research
import com.example.studentemployee.data.Teaching
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TeachingViewModel : ViewModel() {
    private val repository = FirestoreRepository()

    private val _teachings = MutableStateFlow<List<Teaching>>(emptyList())
    val teachings: StateFlow<List<Teaching>> = _teachings

    fun loadUserTeachings(userId: String) {
        viewModelScope.launch {
            repository.getUserTeachings(userId = userId).collect {
                _teachings.value = it
            }
        }
    }

    fun addTeaching(
        userId: String,
        teaching: Teaching,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        repository
            .addTeaching(userId = userId, teaching = teaching)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun updateTeaching(
        userId: String,
        teaching: Teaching,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        repository
            .updateTeaching(userId = userId, teaching = teaching)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun deleteTeaching(
        userId: String,
        teachingId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        repository.deleteTeaching(userId, teachingId, onSuccess, onError)
    }
}