package com.example.studentemployee.repository

import com.example.studentemployee.features.leadership.model.Leader
import com.example.studentemployee.features.news.model.News
import com.example.studentemployee.features.research.model.Research
import com.example.studentemployee.features.teaching.model.Teaching
import com.example.studentemployee.features.members.model.User
import com.example.studentemployee.features.devotion.model.Devotion
import com.example.studentemployee.features.events.model.Event
import com.example.studentemployee.features.members.model.Anggota
import com.example.studentemployee.features.members.model.Divisi
import com.example.studentemployee.features.members.model.Kelompok
import com.example.studentemployee.features.members.model.StudentEmployee
import com.example.studentemployee.features.profile.model.UserProfile
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import android.util.Log
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await


class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()
    private val studentEmployeesCollection = db.collection("studentEmployees")
    private val TAG = "FirestoreRepository" // Consistent TAG

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

    fun getUserById(uid: String): Flow<User?> = callbackFlow {
        if (uid.isEmpty()) {
            trySend(null).isSuccess
            close()
            return@callbackFlow
        }

        val docRef =
            db.collection("users").document(uid)

        val listener = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val user = snapshot.toObject<User>()?.copy(id = snapshot.id)
                trySend(user).isSuccess
            } else {
                trySend(null).isSuccess
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

    fun getAllStudentEmployeesData(): Flow<List<StudentEmployee>> = callbackFlow {
        val studentEmployeesCollection = db.collection("studentEmployees")

        val listener = studentEmployeesCollection.addSnapshotListener { studentEmployeesSnapshot, error ->
            if (error != null) {
                Log.e("FirestoreRepo", "Error listening to studentEmployees collection", error)
                close(error) // Close Flow with error
                return@addSnapshotListener
            }

            if (studentEmployeesSnapshot == null) {
                Log.w("FirestoreRepo", "StudentEmployees snapshot was null")
                // You might want to send an empty list or let it be,
                // depending on how you want to handle this case.
                // trySend(emptyList()).isSuccess
                return@addSnapshotListener
            }

            // Launch a coroutine within the ProducerScope of callbackFlow
            // to handle asynchronous fetching of subcollections.
            this.launch {
                val studentEmployeeList = mutableListOf<StudentEmployee>()
                try {
                    for (seDoc in studentEmployeesSnapshot.documents) {
                        val seId = seDoc.id
                        val batch = seDoc.getString("batch")
                        val year = seDoc.getString("year")

                        // Fetch Divisi subcollection
                        val divisiList = mutableListOf<Divisi>()
                        val divisiSnapshot = db.collection("studentEmployees").document(seId)
                            .collection("divisi").get().await()

                        for (divisiDoc in divisiSnapshot.documents) {
                            val divisiId = divisiDoc.id
                            val divisiName = divisiDoc.getString("divisi")

                            // Fetch Kelompok subcollection
                            val kelompokList = mutableListOf<Kelompok>()
                            val kelompokSnapshot = db.collection("studentEmployees").document(seId)
                                .collection("divisi").document(divisiId)
                                .collection("kelompok").get().await()

                            for (kelompokDoc in kelompokSnapshot.documents) {
                                val kelompokId = kelompokDoc.id
                                val kelompokName = kelompokDoc.getDouble("kelompok")

                                // Fetch Anggota subcollection
                                val anggotaList = mutableListOf<Anggota>()
                                val anggotaSnapshot = db.collection("studentEmployees").document(seId)
                                    .collection("divisi").document(divisiId)
                                    .collection("kelompok").document(kelompokId)
                                    .collection("anggota").get().await()

                                for (anggotaDoc in anggotaSnapshot.documents) {
                                    anggotaList.add(
                                        Anggota(
                                            id = anggotaDoc.id,
                                            nama = anggotaDoc.getString("nama"),
                                            nim = anggotaDoc.getString("nim")
                                        )
                                    )
                                }
                                kelompokList.add(
                                    Kelompok(
                                        id = kelompokId,
                                        kelompok = kelompokName,
                                        anggota = anggotaList
                                    )
                                )
                            }
                            divisiList.add(
                                Divisi(
                                    id = divisiId,
                                    divisi = divisiName,
                                    kelompok = kelompokList
                                )
                            )
                        }
                        studentEmployeeList.add(
                            StudentEmployee(
                                id = seId,
                                batch = batch,
                                year = year,
                                divisi = divisiList
                            )
                        )
                    }
                    trySend(studentEmployeeList).isSuccess // Send the fully populated list
                } catch (e: Exception) {
                    Log.e("FirestoreRepo", "Error fetching nested collections", e)
                    close(e) // Close the flow with the exception if any sub-fetch fails
                }
            }
        }

        // This is called when the Flow collector is cancelled
        awaitClose {
            Log.d("FirestoreRepo", "Closing studentEmployees listener.")
            listener.remove()
        }
    }

    suspend fun getStudentEmployeeById(id: String): Result<StudentEmployee?> {
        // This method would need to be updated to fetch nested Divisi, Kelompok, Anggota
        // if a fully populated StudentEmployee is expected.
        return try {
            if (id.isEmpty()) {
                Log.w(TAG, "getStudentEmployeeById called with empty ID.")
                return Result.success(null)
            }
            val documentSnapshot = studentEmployeesCollection.document(id).get().await()
            if (documentSnapshot.exists()) {
                // This toObject will only get year/batch unless StudentEmployee @Exclude s divisi
                // or you manually reconstruct it like in getAllStudentEmployeesData.
                val studentEmployee = documentSnapshot.toObject<StudentEmployee>()?.copy(id = documentSnapshot.id)
                Log.d(TAG, "Fetched student employee by ID (simple): $id, Data: $studentEmployee")
                Result.success(studentEmployee)
            } else {
                Log.d(TAG, "No student employee found with ID: $id")
                Result.success(null)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching student employee by ID (simple): $id", e)
            Result.failure(e)
        }
    }

    suspend fun addStudentEmployee(studentEmployee: StudentEmployee): Result<String> {
        // This method needs to be updated to handle writing nested 'divisi' data
        // into their respective subcollections. Current implementation only writes year/batch.
        return try {
            val documentReference = studentEmployeesCollection.add(
                mapOf(
                    "year" to studentEmployee.year,
                    "batch" to studentEmployee.batch
                    // Does NOT save studentEmployee.divisi here.
                )
            ).await()
            Log.d(TAG, "Student employee added (simple - year/batch only) with ID: ${documentReference.id}")
            Result.success(documentReference.id)
        } catch (e: Exception) {
            Log.e(TAG, "Error adding student employee (simple)", e)
            Result.failure(e)
        }
    }

    suspend fun updateStudentEmployee(studentEmployee: StudentEmployee): Result<Unit> {
        // This method needs to be updated to handle writing/updating nested 'divisi' data.
        // Current implementation only updates year/batch.
        return try {
            if (studentEmployee.id?.isEmpty() == true) {
                Log.e(TAG, "Update failed (simple): StudentEmployee ID is empty.")
                return Result.failure(IllegalArgumentException("StudentEmployee ID cannot be empty for update."))
            }
            studentEmployee.id?.let {
                studentEmployeesCollection.document(it).set(
                    mapOf(
                        "year" to studentEmployee.year,
                        "batch" to studentEmployee.batch
                        // Does NOT update studentEmployee.divisi here.
                    )
                ).await()
            }
            Log.d(TAG, "Student employee updated (simple - year/batch only): ${studentEmployee.id}")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating student employee (simple): ${studentEmployee.id}", e)
            Result.failure(e)
        }
    }

    suspend fun deleteStudentEmployee(id: String): Result<Unit> {
        return try {
            if (id.isEmpty()) {
                Log.e(TAG, "Delete StudentEmployee failed: StudentEmployee ID is empty.")
                return Result.failure(IllegalArgumentException("StudentEmployee ID cannot be empty for delete."))
            }
            val studentEmployeeDocRef = studentEmployeesCollection.document(id)

            // 1. Delete all Divisi and their nested subcollections (Kelompok and Anggota)
            val divisiCollectionRef = studentEmployeeDocRef.collection("divisi")
            val divisiSnapshot = divisiCollectionRef.get().await()
            for (divisiDoc in divisiSnapshot.documents) {
                // a. Delete Kelompok and their Anggota for this Divisi
                val kelompokCollectionRef = divisiDoc.reference.collection("kelompok")
                val kelompokSnapshot = kelompokCollectionRef.get().await()
                for (kelompokDoc in kelompokSnapshot.documents) {
                    val anggotaCollectionRef = kelompokDoc.reference.collection("anggota")
                    val anggotaSnapshot = anggotaCollectionRef.get().await()
                    for (anggotaDoc in anggotaSnapshot.documents) {
                        anggotaDoc.reference.delete().await()
                    }
                    Log.d(TAG, "All anggota deleted for Kelompok '${kelompokDoc.id}' in Divisi '${divisiDoc.id}' (SE '$id')")

                    kelompokDoc.reference.delete().await()
                    Log.d(TAG, "Kelompok '${kelompokDoc.id}' deleted from Divisi '${divisiDoc.id}' (SE '$id')")
                }
                Log.d(TAG, "All kelompok and their anggota deleted for Divisi '${divisiDoc.id}' (SE '$id')")

                divisiDoc.reference.delete().await()
                Log.d(TAG, "Divisi '${divisiDoc.id}' and its subcollections deleted from SE '$id'")
            }
            Log.d(TAG, "All divisi and their subcollections successfully deleted for SE '$id'")

            studentEmployeeDocRef.delete().await()
            Log.d(TAG, "Student employee '$id' and all its subcollections successfully deleted.")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting student employee '$id' and its subcollections", e)
            Result.failure(e)
        }
    }

    // CRUD Divisi

    suspend fun addDivisiToStudentEmployee(studentEmployeeId: String, divisiName: String): Result<String> {
        return try {
            if (studentEmployeeId.isEmpty() || divisiName.isEmpty()) {
                return Result.failure(IllegalArgumentException("StudentEmployee ID and Divisi Name cannot be empty."))
            }
            val divisiData = mapOf("divisi" to divisiName) // Field name in Firestore is "divisi" for the name
            val documentReference = studentEmployeesCollection.document(studentEmployeeId)
                .collection("divisi")
                .add(divisiData)
                .await()
            Log.d(TAG, "Divisi '$divisiName' added to SE '$studentEmployeeId' with new ID: ${documentReference.id}")
            Result.success(documentReference.id)
        } catch (e: Exception) {
            Log.e(TAG, "Error adding divisi to SE '$studentEmployeeId'", e)
            Result.failure(e)
        }
    }

    suspend fun updateDivisiInStudentEmployee(studentEmployeeId: String, divisi: Divisi): Result<Unit> {
        return try {
            if (studentEmployeeId.isEmpty() || divisi.id?.isEmpty() == true || divisi.divisi == null) {
                return Result.failure(IllegalArgumentException("IDs and Divisi Name cannot be empty for update."))
            }
            val divisiData = mapOf("divisi" to divisi.divisi)
            divisi.id?.let {
                studentEmployeesCollection.document(studentEmployeeId)
                    .collection("divisi").document(it)
                    .set(divisiData)
                    .await()
            }
            Log.d(TAG, "Divisi '${divisi.id}' updated in SE '$studentEmployeeId'")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating divisi '${divisi.id}' in SE '$studentEmployeeId'", e)
            Result.failure(e)
        }
    }

    suspend fun deleteDivisiFromStudentEmployee(studentEmployeeId: String, divisiId: String): Result<Unit> {
        return try {
            if (studentEmployeeId.isEmpty() || divisiId.isEmpty()) {
                Log.e(TAG, "Delete Divisi failed: IDs cannot be empty.")
                return Result.failure(IllegalArgumentException("StudentEmployee ID and Divisi ID cannot be empty for delete."))
            }
            val divisiDocRef = studentEmployeesCollection.document(studentEmployeeId)
                .collection("divisi").document(divisiId)

            val kelompokCollectionRef = divisiDocRef.collection("kelompok")
            val kelompokSnapshot = kelompokCollectionRef.get().await()
            for (kelompokDoc in kelompokSnapshot.documents) {
                val anggotaCollectionRef = kelompokDoc.reference.collection("anggota")
                val anggotaSnapshot = anggotaCollectionRef.get().await()
                for (anggotaDoc in anggotaSnapshot.documents) {
                    anggotaDoc.reference.delete().await()
                }
                Log.d(TAG, "All anggota deleted for Kelompok '${kelompokDoc.id}' in Divisi '$divisiId'")

                kelompokDoc.reference.delete().await()
                Log.d(TAG, "Kelompok '${kelompokDoc.id}' deleted from Divisi '$divisiId'")
            }
            Log.d(TAG, "All kelompok and their anggota successfully deleted for Divisi '$divisiId'")

            divisiDocRef.delete().await()
            Log.d(TAG, "Divisi '$divisiId' and its subcollections successfully deleted from SE '$studentEmployeeId'")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting divisi '$divisiId' from SE '$studentEmployeeId'", e)
            Result.failure(e)
        }
    }

    // Kelompok CRUD
    suspend fun addKelompokToDivisi(studentEmployeeId: String, divisiId: String, kelompokValue: Double): Result<String> {
        return try {
            if (studentEmployeeId.isEmpty() || divisiId.isEmpty()) {
                return Result.failure(IllegalArgumentException("StudentEmployee ID and Divisi ID cannot be empty."))
            }
            val kelompokData = mapOf("kelompok" to kelompokValue)
            val documentReference = studentEmployeesCollection.document(studentEmployeeId)
                .collection("divisi").document(divisiId)
                .collection("kelompok")
                .add(kelompokData)
                .await()
            Log.d(TAG, "Kelompok '$kelompokValue' added to Divisi '$divisiId' (SE '$studentEmployeeId') with new ID: ${documentReference.id}")
            Result.success(documentReference.id)
        } catch (e: Exception) {
            Log.e(TAG, "Error adding kelompok to Divisi '$divisiId' (SE '$studentEmployeeId')", e)
            Result.failure(e)
        }
    }

    suspend fun updateKelompokInDivisi(studentEmployeeId: String, divisiId: String, kelompok: Kelompok): Result<Unit> {
        return try {
            if (studentEmployeeId.isEmpty() || divisiId.isEmpty() || kelompok.id?.isEmpty() == true || kelompok.kelompok == null) {
                return Result.failure(IllegalArgumentException("IDs and Kelompok value cannot be empty for update."))
            }
            val kelompokData = mapOf("kelompok" to kelompok.kelompok) // Field name in Firestore is "kelompok"
            kelompok.id?.let {
                studentEmployeesCollection.document(studentEmployeeId)
                    .collection("divisi").document(divisiId)
                    .collection("kelompok").document(it)
                    .set(kelompokData) // Use set to update the value. If other fields existed, merge might be better.
                    .await()
            }
            Log.d(TAG, "Kelompok '${kelompok.id}' updated in Divisi '$divisiId' (SE '$studentEmployeeId')")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating kelompok '${kelompok.id}' in Divisi '$divisiId' (SE '$studentEmployeeId')", e)
            Result.failure(e)
        }
    }

    suspend fun deleteKelompokFromDivisi(studentEmployeeId: String, divisiId: String, kelompokId: String): Result<Unit> {
        return try {
            if (studentEmployeeId.isEmpty() || divisiId.isEmpty() || kelompokId.isEmpty()) {
                Log.e(TAG, "Delete Kelompok failed: IDs cannot be empty.")
                return Result.failure(IllegalArgumentException("StudentEmployee ID, Divisi ID, and Kelompok ID cannot be empty for delete."))
            }

            val kelompokDocRef = studentEmployeesCollection.document(studentEmployeeId)
                .collection("divisi").document(divisiId)
                .collection("kelompok").document(kelompokId)

            val anggotaCollectionRef = kelompokDocRef.collection("anggota")
            val anggotaSnapshot = anggotaCollectionRef.get().await()
            for (anggotaDoc in anggotaSnapshot.documents) {
                anggotaDoc.reference.delete().await()
            }
            Log.d(TAG, "All anggota successfully deleted for Kelompok '$kelompokId' in Divisi '$divisiId' (SE '$studentEmployeeId')")

            kelompokDocRef.delete().await()
            Log.d(TAG, "Kelompok '$kelompokId' successfully deleted from Divisi '$divisiId' (SE '$studentEmployeeId')")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting kelompok '$kelompokId' from Divisi '$divisiId' (SE '$studentEmployeeId')", e)
            Result.failure(e)
        }
    }

    suspend fun addAnggotaToKelompok(studentEmployeeId: String, divisiId: String, kelompokId: String, anggota: Anggota): Result<String> {
        return try {
            if (studentEmployeeId.isEmpty() || divisiId.isEmpty() || kelompokId.isEmpty() || anggota.nama.isNullOrBlank() || anggota.nim.isNullOrBlank()) {
                return Result.failure(IllegalArgumentException("IDs, Anggota Name, and NIM cannot be empty."))
            }
            val anggotaData = mapOf(
                "nama" to anggota.nama,
                "nim" to anggota.nim
            )
            val documentReference = studentEmployeesCollection.document(studentEmployeeId)
                .collection("divisi").document(divisiId)
                .collection("kelompok").document(kelompokId)
                .collection("anggota")
                .add(anggotaData)
                .await()
            Log.d(TAG, "Anggota '${anggota.nama}' added to Kelompok '$kelompokId' with new ID: ${documentReference.id}")
            Result.success(documentReference.id)
        } catch (e: Exception) {
            Log.e(TAG, "Error adding anggota to Kelompok '$kelompokId'", e)
            Result.failure(e)
        }
    }

    suspend fun updateAnggotaInKelompok(studentEmployeeId: String, divisiId: String, kelompokId: String, anggota: Anggota): Result<Unit> {
        return try {
            if (studentEmployeeId.isEmpty() || divisiId.isEmpty() || kelompokId.isEmpty() || anggota.id?.isEmpty() == true || anggota.nama.isNullOrBlank() || anggota.nim.isNullOrBlank()) {
                return Result.failure(IllegalArgumentException("IDs, Anggota Name, and NIM cannot be empty for update."))
            }
            val anggotaData = mapOf(
                "nama" to anggota.nama,
                "nim" to anggota.nim
            )
            anggota.id?.let {
                studentEmployeesCollection.document(studentEmployeeId)
                    .collection("divisi").document(divisiId)
                    .collection("kelompok").document(kelompokId)
                    .collection("anggota").document(it)
                    .set(anggotaData) // Use set to update.
                    .await()
            }
            Log.d(TAG, "Anggota '${anggota.id}' updated in Kelompok '$kelompokId'")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating anggota '${anggota.id}' in Kelompok '$kelompokId'", e)
            Result.failure(e)
        }
    }

    suspend fun deleteAnggotaFromKelompok(studentEmployeeId: String, divisiId: String, kelompokId: String, anggotaId: String): Result<Unit> {
        return try {
            if (studentEmployeeId.isEmpty() || divisiId.isEmpty() || kelompokId.isEmpty() || anggotaId.isEmpty()) {
                return Result.failure(IllegalArgumentException("IDs cannot be empty for delete."))
            }
            studentEmployeesCollection.document(studentEmployeeId)
                .collection("divisi").document(divisiId)
                .collection("kelompok").document(kelompokId)
                .collection("anggota").document(anggotaId)
                .delete()
                .await()
            Log.d(TAG, "Anggota '$anggotaId' deleted from Kelompok '$kelompokId'")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting anggota '$anggotaId' from Kelompok '$kelompokId'", e)
            Result.failure(e)
        }
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