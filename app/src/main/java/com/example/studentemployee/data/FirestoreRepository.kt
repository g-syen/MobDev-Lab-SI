package com.example.studentemployee.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose


class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()

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

    fun getMembers(): Flow<List<User>> = callbackFlow {
        val listener = db.collection("users")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val memberList = snapshot.documents.mapNotNull { doc ->
                        doc.toObject<User>()?.copy(id = doc.id)
                    }
                    trySend(memberList).isSuccess
                }
            }

        awaitClose { listener.remove() }
    }

    fun getUserProfile(userId: String): Flow<UserProfile?> = callbackFlow {
        val userRef = db.collection("users").document(userId)

        val listener = userRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error) // Tutup flow jika ada error
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val userProfile = snapshot.toObject<UserProfile>()
                trySend(userProfile).isSuccess
            } else {
                trySend(null).isSuccess
            }
        }

        awaitClose { listener.remove() }
    }

    fun getUserSocialLinks(userId: String): Flow<Map<String, String>> = callbackFlow {
        val listener = db.collection("users")
            .document(userId)
            .collection("socialmedia")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val linksMap = mutableMapOf<String, String>()
                    for (doc in snapshot) {
                        val platform = doc.id
                        val link = doc.getString("link")
                        if (!link.isNullOrEmpty()) {
                            linksMap[platform] = link
                        }
                    }
                    trySend(linksMap).isSuccess
                }
            }

        awaitClose { listener.remove() }
    }

    fun updateUserProfile(userId: String, profileData: Map<String, Any>): Task<Void> {
        return db.collection("users").document(userId).update(profileData)
    }

    fun updateSocialLink(userId: String, platform: String, link: String): Task<Void> {
        val data = mapOf("link" to link)
        return db.collection("users")
            .document(userId)
            .collection("socialmedia")
            .document(platform)
            .set(data)
    }

    fun getUserArticles(userId: String): Flow<List<Article>> = callbackFlow {
        val listener = db.collection("users")
            .document(userId)
            .collection("articles")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val articles = snapshot.documents.mapNotNull { it.toObject(Article::class.java) }
                    trySend(articles).isSuccess
                }
            }

        awaitClose { listener.remove() }
    }

    fun getUserDevotions(userId: String): Flow<List<Devotion>> = callbackFlow {
        val listener = db.collection("users")
            .document(userId)
            .collection("devotions")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val devotions = snapshot.documents.mapNotNull { it.toObject(Devotion::class.java) }
                    trySend(devotions).isSuccess
                }
            }

        awaitClose { listener.remove() }
    }

}