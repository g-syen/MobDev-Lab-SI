package com.example.studentemployee.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.studentemployee.data.Facilities
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditFacilityScreen(
    firestore: FirebaseFirestore,
    navController: NavController,
    facilityId: String?
) {
    var title by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }
    var isLoaded by remember { mutableStateOf(false) }
    var isEdit by remember { mutableStateOf(false) }

    LaunchedEffect(facilityId) {
        if (facilityId != null) {
            firestore.collection("facilities").document(facilityId)
                .get()
                .addOnSuccessListener { document ->
                    document.toObject(Facilities::class.java)?.let { facility ->
                        title = facility.title
                        imageUrl = facility.imageUrl
                        link = facility.link
                        isLoaded = true
                        isEdit  = true
                    }
                }
                .addOnFailureListener {
                    Log.e("Firestore", "Error fetching facility", it)
                }
        } else {
            isLoaded = true
            isEdit = false
        }
    }

    fun saveFacility() {
        val newFacility = facilityId?.let { id ->
            Facilities(id = id, title = title, imageUrl = imageUrl, link = link)
        } ?: Facilities(
            id = UUID.randomUUID().toString(),
            title = title,
            imageUrl = imageUrl,
            link = link
        )

        firestore.collection("facilities").document(newFacility.id)
            .set(newFacility)
            .addOnSuccessListener {
                Log.d("Firestore", "Facility saved")
                navController.popBackStack()
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error saving facility", it)
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                if(isEdit) {
                    Text("Edit Facility",
                        color = Color.White) }
                else {
                Text("Add Facility",
                    color = Color.White) }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF19253F)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(1f)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
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
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        label = { Text("Image URL") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = link,
                        onValueChange = { link = it },
                        label = { Text("Link") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { saveFacility() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10375E))
                    ) {
                        Text("Save", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
