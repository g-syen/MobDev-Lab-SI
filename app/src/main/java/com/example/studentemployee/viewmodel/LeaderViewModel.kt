package com.example.studentemployee.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentemployee.data.FirestoreRepository
import com.example.studentemployee.data.Leader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LeaderViewModel:ViewModel() {
    private val repository = FirestoreRepository()

    //ini untuk dimodifikasi internal
    private val _leaderList = MutableStateFlow<List<Leader>>(emptyList())
    // ini yang diekspos ke View
    val leaderList = _leaderList.asStateFlow()

    init {
        fetchLeader()
    }

    private fun fetchLeader(){
        viewModelScope.launch {
            repository.getLeader().collect(){data ->
                _leaderList.value = data

            }
        }
    }
}