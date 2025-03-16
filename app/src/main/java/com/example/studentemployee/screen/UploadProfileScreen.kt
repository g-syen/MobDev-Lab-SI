package com.example.studentemployee.screen

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

@Composable
fun UploadProfileScreen() {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { selectedUri ->
            imageUri = selectedUri
            uploadProfilePicture(selectedUri,
                onSuccess = { Log.d("Profile", "Image uploaded: $it") },
                onFailure = { Log.e("Profile", "Upload failed", it) })
        }
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        imageUri?.let {
            AsyncImage(model = it, contentDescription = "Profile Picture")
        }
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Select Profile Picture")
        }
    }
}


fun uploadProfilePicture(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val storageRef = FirebaseStorage.getInstance().reference.child("profile_pictures/$userId.jpg")

    storageRef.putFile(imageUri)
        .addOnSuccessListener { taskSnapshot ->
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                saveProfileUrlToFirestore(userId, uri.toString())
                onSuccess(uri.toString()) // Pass the download URL
            }
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}

fun saveProfileUrlToFirestore(userId: String, imageUrl: String) {
    val firestore = FirebaseFirestore.getInstance()
    firestore.collection("users").document(userId)
        .update("profileImageUrl", imageUrl)
        .addOnSuccessListener {
            Log.d("Firestore", "Profile image URL updated")
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error updating profile image", e)
        }
}