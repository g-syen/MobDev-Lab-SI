package com.example.studentemployee.features.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentemployee.repository.FirestoreRepository
import com.example.studentemployee.features.events.model.Event
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow


class EventViewModel : ViewModel() {
    private val repository = FirestoreRepository()

    private val _event = MutableStateFlow<List<Event>>(emptyList())
    val event: StateFlow<List<Event>> = _event

    fun loadAllEvents() {
        viewModelScope.launch {
            repository.getAllEvents().collect {
                _event.value = it
            }
        }
    }

    fun addEvent(
        event: Event,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        repository
            .addEvent(event = event)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun updateEvent(
        event: Event,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        repository
            .updateEvent(event = event)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun deleteEvent(
        eventId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        repository.deleteEvent(eventId, onSuccess, onError)
    }
}