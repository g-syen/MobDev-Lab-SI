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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.studentemployee.data.Facilities
import com.example.studentemployee.components.FacilitiesCard
import com.example.studentemployee.components.ImageCarousel
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacilitiesScreen(
    firestore: FirebaseFirestore,
    navController: NavController,
    onClickBack: () -> Unit
) {
    var facilities by remember { mutableStateOf<List<Facilities>>(emptyList()) }

    LaunchedEffect(Unit) {
        firestore.collection("facilities")
            .get()
            .addOnSuccessListener { result ->
                facilities = result.documents.mapNotNull { it.toObject(Facilities::class.java) }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error getting documents", it)
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fasilitas Lab SI", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { onClickBack() }) {
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
        },
        containerColor = Color(0xFFF9F9F9)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment =  Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                ImageCarousel(facilities)
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn{
                items(facilities) { facility ->
                    FacilitiesCard(facility)
                }
            }
        }
    }

}