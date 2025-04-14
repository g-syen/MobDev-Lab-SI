package com.example.studentemployee.features.leadership.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.studentemployee.core.components.LeadershipCard
import com.example.studentemployee.features.leadership.model.Leader
import com.example.studentemployee.features.leadership.LeaderViewModel
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeadershipScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    firestore: FirebaseFirestore,
    viewModel: LeaderViewModel = viewModel()
) {
    val leaderList by viewModel.leaderList.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Pimpinan Lab Sistem Informasi", color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF19253F)
                )
            )
        },
        containerColor = Color(0xFFF9F9F9)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),

            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ListPimpinan(leaderList = leaderList, navController = navController)
        }
    }
}

@Preview
@Composable
private fun LeadershipScreenPreview() {
    LeadershipScreen(navController = rememberNavController(), firestore = FirebaseFirestore.getInstance())
}

@Composable
fun ListPimpinan(modifier: Modifier = Modifier, leaderList: List<Leader>, navController : NavController) {
    Column( verticalArrangement = Arrangement.spacedBy(10.dp)) {
        leaderList.forEach { leader ->
            LeadershipCard(leader = leader, navController = navController)
        }
    }
}

@Preview
@Composable
private fun ListPimpinanPreview() {
    val leaderList = listOf(
        Leader(id = "1", name = "Dr. Aditya Wibowo, M.Kom.", position = "Kepala Lab"),
        Leader(id = "2", name = "Prof. Bambang Prasetyo, Ph.D.", position = "Koordinator Riset"),
        Leader(id = "3", name = "Dewi Kartika, S.Kom., M.Sc.", position = "Kepala Divisi Sistem Informasi"),
        Leader(id = "4", name = "Andi Nugraha, S.T., M.T.", position = "Kepala Divisi Keamanan Siber")
    )
    ListPimpinan(leaderList = leaderList, navController = rememberNavController())
}