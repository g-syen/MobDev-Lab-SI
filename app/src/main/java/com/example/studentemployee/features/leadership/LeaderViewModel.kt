package com.example.studentemployee.features.leadership

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentemployee.repository.FirestoreRepository
import com.example.studentemployee.features.leadership.model.Leader
import com.google.firebase.firestore.FirebaseFirestore
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

    fun deleteLeader(leader: Leader, firestore: FirebaseFirestore) {
        val userDocRef = firestore.collection("leader").document(leader.id)

        userDocRef.delete()
            .addOnSuccessListener {
                _leaderList.value = _leaderList.value.filter { it.id != leader.id }
                Log.d("Firestore", "Member document deleted: ${leader.id}")
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error deleting Firestore document", it)
            }

    }
}