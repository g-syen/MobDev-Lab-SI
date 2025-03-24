package com.example.studentemployee.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditLeaderScreen(
    firestore: FirebaseFirestore,
    navController: NavController,
    leaderId: String?
) {
    var position by remember { mutableStateOf("") }
    var isLoaded by remember { mutableStateOf(false) }
    var isEdit by remember { mutableStateOf(false) }
    var usersList by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var selectedUserId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(leaderId) {
        firestore.collection("users").get()
            .addOnSuccessListener { result ->
                usersList = result.documents.mapNotNull { doc ->
                    val name = doc.getString("nama")
                    if (name != null) doc.id to name else null
                }.toMap()
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error fetching users", it)
            }

        if (leaderId != null) {
            firestore.collection("leader").document(leaderId)
                .get()
                .addOnSuccessListener { document ->
                    document?.data?.let { data ->
                        val leaderName = data["name"] as? String
                        position = data["position"] as? String ?: ""
                        selectedUserId = usersList.entries.find { it.value == leaderName }?.key
                        isLoaded = true
                        isEdit = true
                    }
                }
                .addOnFailureListener {
                    Log.e("Firestore", "Error fetching leader", it)
                }
        } else {
            isLoaded = true
            isEdit = false
        }
    }

    fun saveLeader() {
        if (selectedUserId.isNullOrEmpty() || position.isEmpty()) {
            Log.e("Validation", "Fields cannot be empty")
            return
        }

        val leaderData = mapOf(
            "name" to usersList[selectedUserId],
            "position" to position
        )

        val leaderDocRef = firestore.collection("leader").document(selectedUserId!!)

        leaderDocRef.set(leaderData)
            .addOnSuccessListener {
                Log.d("Firestore", "Leader saved")
                navController.popBackStack()
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error saving leader", it)
            }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdit) "Edit Leader" else "Add Leader", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF19253F))
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
                    var expanded by remember { mutableStateOf(false) }

                    if(leaderId != null){
                        usersList[leaderId]?.let {
                            OutlinedTextField(
                                value = it,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } else {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = it }
                        ) {
                            OutlinedTextField(
                                value = usersList[selectedUserId] ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Select Leader") },
                                trailingIcon = {
                                    IconButton(onClick = { expanded = !expanded }) {
                                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown", Modifier.clickable { expanded = !expanded })
                                    }
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                usersList.forEach { (userId, userName) ->
                                    DropdownMenuItem(
                                        text = { Text(userName) },
                                        onClick = {
                                            selectedUserId = userId
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = position,
                        onValueChange = { position = it },
                        label = { Text("Position") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { saveLeader() },
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
