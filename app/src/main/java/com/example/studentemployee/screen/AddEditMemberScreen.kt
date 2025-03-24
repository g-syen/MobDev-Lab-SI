package com.example.studentemployee.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMemberScreen(
    firestore: FirebaseFirestore,
    auth: FirebaseAuth,
    navController: NavController,
    memberId: String?
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var specialist by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("member") }
    var biography by remember { mutableStateOf("") }
    var profileImageUrl by remember { mutableStateOf("") }
    var isLoaded by remember { mutableStateOf(false) }
    var isEdit by remember { mutableStateOf(false) }
    val adminEmail = "admin@ub.ac.id"
    val adminPassword = "adminlab"

    LaunchedEffect(memberId) {
        if (memberId != null) {
            firestore.collection("users").document(memberId)
                .get()
                .addOnSuccessListener { document ->
                    document?.data?.let { data ->
                        name = data["nama"] as? String ?: ""
                        email = data["email"] as? String ?: ""
                        specialist = data["specialist"] as? String ?: ""
                        role = data["role"] as? String ?: "member"
                        biography = data["biography"] as? String ?: ""
                        profileImageUrl = data["profileImageUrl"] as? String ?: ""
                        isLoaded = true
                        isEdit = true
                    }
                }
                .addOnFailureListener {
                    Log.e("Firestore", "Error fetching member", it)
                }
        } else {
            isLoaded = true
            isEdit = false
        }
    }

    fun saveMember() {
        if (!isEdit) {
            auth.createUserWithEmailAndPassword(email, UUID.randomUUID().toString())
                .addOnSuccessListener { authResult ->
                    val uid = authResult.user?.uid ?: return@addOnSuccessListener
                    val newMember = mapOf(
                        "nama" to name,
                        "email" to email,
                        "specialist" to specialist,
                        "role" to "member",
                        "biography" to null,
                        "profileImageUrl" to null
                    )
                    firestore.collection("users").document(uid)
                        .set(newMember)
                        .addOnSuccessListener {
                            val socialMediaLinks = mapOf(
                                "GitHub" to "https://github.com",
                                "Instagram" to "https://instagram.com",
                                "LinkedIn" to "https://linkedin.com",
                                "YouTube" to "https://youtube.com"
                            )
                            socialMediaLinks.forEach { (platform, link) ->
                                firestore.collection("users").document(uid)
                                    .collection("socialmedia").document(platform)
                                    .set(mapOf("link" to link))
                            }

                            auth.signInWithEmailAndPassword(adminEmail, adminPassword)
                                .addOnSuccessListener {
                                    Log.d("Auth", "Re-authenticated as admin")
                                    navController.popBackStack()
                                }
                                .addOnFailureListener { e ->
                                    Log.e("Auth", "Failed to re-authenticate admin", e)
                                }
                            Log.d("Firestore", "Member added")
                            navController.popBackStack()
                        }
                }
                .addOnFailureListener {
                    Log.e("Auth", "Error creating user", it)
                }
        } else {
            val updatedMember = mapOf(
                "nama" to name,
                "email" to email,
                "specialist" to specialist,
                "role" to role,
                "biography" to biography,
                "profileImageUrl" to profileImageUrl
            )
            firestore.collection("users").document(memberId!!)
                .set(updatedMember)
                .addOnSuccessListener {
                    Log.d("Firestore", "Member updated")
                    navController.popBackStack()
                }
                .addOnFailureListener {
                    Log.e("Firestore", "Error updating member", it)
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isEdit) "Edit Member" else "Add Member", color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF19253F)),
                modifier = Modifier.fillMaxWidth().zIndex(1f)
            )
        },
        containerColor = Color(0xFFF9F9F9)
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (!isLoaded) {
                CircularProgressIndicator()
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = specialist, onValueChange = { specialist = it }, label = { Text("Specialist") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = biography, onValueChange = { biography = it }, label = { Text("Biography") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = profileImageUrl, onValueChange = { profileImageUrl = it }, label = { Text("Profile Image URL") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { saveMember() },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Save", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
