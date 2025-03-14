package com.example.studentemployee.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.studentemployee.components.LeadershipCard
import com.example.studentemployee.components.TopAppBarMenu

@Composable
fun LeadershipScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
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
            ListPimpinan()
        }
    }
}

@Preview
@Composable
private fun LeadershipScreenPreview() {
    LeadershipScreen(navController = rememberNavController())
}

@Composable
fun ListPimpinan(modifier: Modifier = Modifier) {
    val pimpinan = listOf(
        "Kepala Lab" to "Dr. Aditya Wibowo, M.Kom.",
        "Koordinator Riset" to "Prof. Bambang Prasetyo, Ph.D.",
        "Kepala Divisi Sistem Informasi" to "Dewi Kartika, S.Kom., M.Sc.",
        "Kepala Divisi Keamanan Siber" to "Andi Nugraha, S.T., M.T."
    )
    Column( verticalArrangement = Arrangement.spacedBy(10.dp)) {
        pimpinan.forEach { (jabatan, nama) ->
            LeadershipCard(jabatan = jabatan, nama = nama)
        }
    }
}

@Preview
@Composable
private fun ListPimpinanPreview() {
    ListPimpinan()
}