package com.example.studentemployee.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentemployee.data.Facilities
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

@Composable
fun AddEditFacilityScreen(
    firestore: FirebaseFirestore,
    navController: NavController,
    facilityId: String?
) {
    var facilities by remember { mutableStateOf<List<Facilities>>(emptyList()) }
    LaunchedEffect(Unit) {
        firestore.collection("facilities")
            .get()
            .addOnSuccessListener { result ->
                facilities = result.documents.mapNotNull { doc ->
                    doc.toObject(Facilities::class.java)?.apply { id = doc.id } }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error getting documents", it)
            }
    }

    val facility = facilities.find { fac ->
        fac.id == facilityId
    }

    var title by remember { mutableStateOf(facility?.title ?: "") }
    var imageUrl by remember { mutableStateOf(facility?.imageUrl ?: "") }
    var link by remember { mutableStateOf(facility?.link ?: "") }

    fun saveFacility() {
        val newFacility = facility?.copy(title = title, imageUrl = imageUrl, link = link)
            ?: Facilities(id = UUID.randomUUID().toString(), title = title, imageUrl = imageUrl, link = link)

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = title, onValueChange = { title = it }, label = { Text(facility?.title ?: "") })
        TextField(value = imageUrl, onValueChange = { imageUrl = it }, label = { Text(facility?.imageUrl ?: "") })
        TextField(value = link, onValueChange = { link = it }, label = { Text(facility?.link ?: "") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { saveFacility() }) {
            Text("Save")
        }
    }
}