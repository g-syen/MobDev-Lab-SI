package com.example.studentemployee.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose


class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()
//    mengambil data diawal sekali
//    fun getLeader(): Flow<List<Leader>> = flow {
//        val result = db.collection("leader").get().await()
//        val leaderList = result.documents.mapNotNull { doc ->
//            doc.toObject<Leader>()?.copy(id = doc.id)
//        }
//        emit(leaderList)
//    }

    // mengambil data secara real-time tanpa reload ketika ada perubahan
    fun getLeader(): Flow<List<Leader>> = callbackFlow {
        val listener = db.collection("leader")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error) // Tutup aliran jika terjadi error
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val leaderList = snapshot.documents.mapNotNull { doc ->
                        doc.toObject<Leader>()?.copy(id = doc.id)
                    }
                    trySend(leaderList).isSuccess
                }
            }

        awaitClose { listener.remove() }
    }

}