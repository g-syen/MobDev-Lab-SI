package com.example.studentemployee.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.studentemployee.components.LeadershipCard
import com.example.studentemployee.components.MemberCard
import com.example.studentemployee.components.SearchBar
import com.example.studentemployee.data.Leader
import com.example.studentemployee.data.User
import com.example.studentemployee.viewmodel.LeaderViewModel
import com.example.studentemployee.viewmodel.MemberViewModel
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    firestore: FirebaseFirestore,
    viewModel: MemberViewModel = viewModel()
) {
    val memberList by viewModel.memberList.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredList = memberList.filter {
        it.nama.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Anggota Lab Sistem Informasi", color = Color.White)
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
            SearchBar(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = "Search Member",
                modifier = Modifier.fillMaxWidth(),
                onSearch = {
                    Log.d("Search", "Searching for: $searchQuery")
                }
            )

            ListMember(memberList = filteredList, navController = navController)
        }
    }
}

@Preview
@Composable
private fun MemberScreenPreview() {
    MemberScreen(navController = rememberNavController(), firestore = FirebaseFirestore.getInstance())
}

@Composable
fun ListMember(
    modifier: Modifier = Modifier,
    memberList: List<User>,
    navController : NavController,
) {
    Column( verticalArrangement = Arrangement.spacedBy(10.dp)) {
        memberList.forEach { user ->
            if(user.role=="member") {
                MemberCard(member = user, navController = navController)
            }
        }
    }
}

@Preview
@Composable
private fun ListMemberPreview() {
    val memberList = listOf(
        User(nama = "Dr. Aditya Wibowo, M.Kom.", role = "member"),
        User(nama = "Prof. Bambang Prasetyo, Ph.D.", role = "member"),
        User(nama = "Dewi Kartika, S.Kom., M.Sc.", role = "member"),
        User(nama = "Andi Nugraha, S.T., M.T.", role = "member")
    )
    ListMember(memberList = memberList, navController = rememberNavController())
}