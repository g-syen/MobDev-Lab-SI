package com.example.studentemployee.repository

import com.example.studentemployee.features.leadership.model.Leader
import com.example.studentemployee.features.news.model.News
import com.example.studentemployee.features.research.model.Research
import com.example.studentemployee.features.teaching.model.Teaching
import com.example.studentemployee.features.members.model.User
import com.example.studentemployee.features.devotion.model.Devotion
import com.example.studentemployee.features.events.model.Event
import com.example.studentemployee.features.profile.model.UserProfile
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    fun getUserSocialLinks(userId: String): Flow<Map<String, String>> =
        callbackFlow {
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

    fun updateUserProfile(
        userId: String,
        profileData: Map<String, Any>
    ): Task<Void> {
        return db.collection("users").document(userId).update(profileData)
    }

    fun updateSocialLink(
        userId: String,
        platform: String,
        link: String
    ): Task<Void> {
        val data = mapOf("link" to link)
        return db.collection("users")
            .document(userId)
            .collection("socialmedia")
            .document(platform)
            .set(data)
    }

    fun getAllNews(): Flow<List<News>> = callbackFlow {
        val listener = db.collection("news")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val news =
                        snapshot.documents.mapNotNull { it.toObject(News::class.java) }
                    trySend(news).isSuccess
                }
            }

        awaitClose { listener.remove() }
    }

    fun addNews(news: News): Task<Void> {
        val docRef = db
            .collection("news")
            .document()
        val newsWithId = news.copy(id = docRef.id)

        return docRef.set(newsWithId)
    }

    fun updateNews(news: News): Task<Void> {
        return db
            .collection("news")
            .document(news.id).set(news)
    }

    fun deleteNews(
        newsId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("news")
            .document(newsId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun getAllEvents(): Flow<List<Event>> = callbackFlow {
        val listener = db.collection("events")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val events =
                        snapshot.documents.mapNotNull { it.toObject(Event::class.java) }
                    trySend(events).isSuccess
                }
            }

        awaitClose { listener.remove() }
    }

    fun addEvent(event: Event): Task<Void> {
        val docRef = db
            .collection("events")
            .document()
        val eventWithId = event.copy(id = docRef.id)

        return docRef.set(eventWithId)
    }

    fun updateEvent(event: Event): Task<Void> {
        return db
            .collection("events")
            .document(event.id).set(event)
    }

    fun deleteEvent(
        eventId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("events")
            .document(eventId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun getUserResearches(userId: String): Flow<List<Research>> = callbackFlow {
        val listener = db
            .collection("users")
            .document(userId)
            .collection("researches")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val researches =
                        snapshot.documents.mapNotNull { doc ->
                            val research = doc.toObject(Research::class.java)
                            research?.copy(id = doc.id)
                        }
                    trySend(researches).isSuccess
                }
            }

        awaitClose { listener.remove() }
    }

    fun addResearch(userId: String, research: Research): Task<Void> {
        val docRef = db
            .collection("users")
            .document(userId)
            .collection("researches")
            .document()
        val researchWithId = research.copy(id = docRef.id)

        return docRef.set(researchWithId)
    }

    fun updateResearch(userId: String, research: Research): Task<Void> {
        return db
            .collection("users")
            .document(userId)
            .collection("researches")
            .document(research.id).set(research)
    }

    fun deleteResearch(
        userId: String,
        researchId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("users")
            .document(userId)
            .collection("researches")
            .document(researchId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun getUserTeachings(userId: String): Flow<List<Teaching>> = callbackFlow {
        val listener = db
            .collection("users")
            .document(userId)
            .collection("teachings")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val teachings =
                        snapshot.documents.mapNotNull { doc ->
                            val research = doc.toObject(Teaching::class.java)
                            research?.copy(id = doc.id)
                        }
                    trySend(teachings).isSuccess
                }
            }

        awaitClose { listener.remove() }
    }

    fun addTeaching(userId: String, teaching: Teaching): Task<Void> {
        val docRef = db
            .collection("users")
            .document(userId)
            .collection("teachings")
            .document()
        val teachingWithId = teaching.copy(id = docRef.id)

        return docRef.set(teachingWithId)
    }

    fun updateTeaching(userId: String, teaching: Teaching): Task<Void> {
        return db
            .collection("users")
            .document(userId)
            .collection("teachings")
            .document(teaching.id).set(teaching)
    }

    fun deleteTeaching(
        userId: String,
        teachingId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("users")
            .document(userId)
            .collection("teachings")
            .document(teachingId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }


    fun getUserDevotions(userId: String): Flow<List<Devotion>> = callbackFlow {
        val listener = db
            .collection("users")
            .document(userId)
            .collection("devotions")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val devotions =
                        snapshot.documents.mapNotNull { doc ->
                            val research = doc.toObject(Devotion::class.java)
                            research?.copy(id = doc.id)
                        }
                    trySend(devotions).isSuccess
                }
            }

        awaitClose { listener.remove() }
    }

    fun addDevotion(userId: String, devotion: Devotion): Task<Void> {
        val docRef = db
            .collection("users")
            .document(userId)
            .collection("devotions")
            .document()
        val devotionWithId = devotion.copy(id = docRef.id)

        return docRef.set(devotionWithId)
    }

    fun updateDevotion(userId: String, devotion: Devotion): Task<Void> {
        return db
            .collection("users")
            .document(userId)
            .collection("devotions")
            .document(devotion.id).set(devotion)
    }

    fun deleteDevotion(
        userId: String,
        devotionId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("users")
            .document(userId)
            .collection("devotions")
            .document(devotionId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

//    fun getAllNews(): Flow<List<News>> = callbackFlow {
//        val listener = db
//            .collection("news")
//            .addSnapshotListener { snapshot, error ->
//                if (error != null) {
//                    close(error)
//                    return@addSnapshotListener
//                }
//
//                if (snapshot != null) {
//                    val newsList = snapshot.documents.mapNotNull { doc ->
//                        doc.toObject(News::class.java)
//                    }
//                    trySend(newsList).isSuccess
//                }
//            }
//
//        awaitClose { listener.remove() }
//    }
//
//    fun getAllEvents(): Flow<List<Event>> = callbackFlow {
//        val listener = db
//            .collection("events")
//            .addSnapshotListener { snapshot, error ->
//                if (error != null) {
//                    close(error)
//                    return@addSnapshotListener
//                }
//
//                if (snapshot != null) {
//                    val eventList = snapshot.documents.mapNotNull { doc ->
//                        doc.toObject(Event::class.java)
//                    }
//                    trySend(eventList).isSuccess
//                }
//            }
//
//        awaitClose { listener.remove() }
//    }

    fun getAllUserResearches(): Flow<List<Research>> = callbackFlow {
        val listener = db
            .collectionGroup("researches")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val researches = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Research::class.java)?.copy(id = doc.id)
                    }
                    trySend(researches).isSuccess
                }
            }

        awaitClose { listener.remove() }
    }

    fun getAllUserDevotions(): Flow<List<Devotion>> = callbackFlow {
        val listener = db
            .collectionGroup("devotions")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val devotions = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Devotion::class.java)?.copy(id = doc.id)
                    }
                    trySend(devotions).isSuccess
                }
            }

        awaitClose { listener.remove() }
    }

}