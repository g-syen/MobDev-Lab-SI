package com.example.studentemployee.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.studentemployee.Facilities
import com.example.studentemployee.components.LeadershipCard
import com.example.studentemployee.components.TopAppBarMenu
import com.example.studentemployee.data.Leader
import com.example.studentemployee.viewmodel.LeaderViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun LeadershipScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    firestore: FirebaseFirestore,
    viewModel: LeaderViewModel = viewModel()
) {
    val leaderList by viewModel.leaderList.collectAsState()
    Scaffold(topBar = {
        TopAppBarMenu(
            onClick = { navController.navigateUp() },
            text = "Pimpinan Lab Sistem Informasi"
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),

            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ListPimpinan(leaderList = leaderList)
        }
    }
}

@Preview
@Composable
private fun LeadershipScreenPreview() {
    LeadershipScreen(navController = rememberNavController(), firestore = FirebaseFirestore.getInstance())
}

@Composable
fun ListPimpinan(modifier: Modifier = Modifier, leaderList: List<Leader>) {
    Column( verticalArrangement = Arrangement.spacedBy(10.dp)) {
        leaderList.forEach { leader ->
            LeadershipCard(leader = leader)
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
    ListPimpinan(leaderList = leaderList)
}