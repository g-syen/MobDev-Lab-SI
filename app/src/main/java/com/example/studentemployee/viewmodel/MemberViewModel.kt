package com.example.studentemployee.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentemployee.data.FirestoreRepository
import com.example.studentemployee.data.Leader
import com.example.studentemployee.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MemberViewModel:ViewModel() {
    private val repository = FirestoreRepository()
    private val auth = FirebaseAuth.getInstance()

    //ini untuk dimodifikasi internal
    private val _memberList = MutableStateFlow<List<User>>(emptyList())
    // ini yang diekspos ke View
    val memberList: StateFlow<List<User>> = _memberList

    init {
        fetchLeader()
    }

    private fun fetchLeader(){
        viewModelScope.launch {
            repository.getMembers().collect(){data ->
                _memberList.value = data
            }
        }
    }

    fun deleteMember(member: User, firestore: FirebaseFirestore, auth: FirebaseAuth) {
        val userDocRef = firestore.collection("users").document(member.id)

        userDocRef.collection("socialmedia").get()
            .addOnSuccessListener { snapshot ->
                val batch = firestore.batch()

                for (doc in snapshot.documents) {
                    batch.delete(doc.reference)
                }

                batch.commit()
                    .addOnSuccessListener {
                        Log.d("Firestore", "Deleted subcollection: socialmedia for user ${member.id}")
                        
                        userDocRef.delete()
                            .addOnSuccessListener {
                                _memberList.value = _memberList.value.filter { it.id != member.id }
                                Log.d("Firestore", "Member document deleted: ${member.id}")
                            }
                            .addOnFailureListener {
                                Log.e("Firestore", "Error deleting Firestore document", it)
                            }
                    }
                    .addOnFailureListener {
                        Log.e("Firestore", "Error deleting socialmedia subcollection", it)
                    }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error retrieving socialmedia subcollection", it)
            }
    }


}