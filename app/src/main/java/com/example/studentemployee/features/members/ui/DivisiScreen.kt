package com.example.studentemployee.features.members.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.studentemployee.core.components.DivisiCard
import com.example.studentemployee.core.components.MemberCard
import com.example.studentemployee.core.components.SearchBar
import com.example.studentemployee.core.components.StudentEmployeeCard
import com.example.studentemployee.features.members.model.User
import com.example.studentemployee.features.members.MemberViewModel
import com.example.studentemployee.features.members.StudentEmployeeViewModel
import com.example.studentemployee.features.members.model.Divisi
import com.example.studentemployee.features.members.model.StudentEmployee
import com.example.studentemployee.features.members.model.StudentEmployees
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DivisiScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    firestore: FirebaseFirestore,
    viewModel: MemberViewModel = viewModel(),
    seViewModel: StudentEmployeeViewModel = viewModel(),
    selectedPage: Int? = 1,
    selectedYearBatch: String? = null
) {
    val memberList by viewModel.memberList.collectAsState()
    val studentEmployeeList by seViewModel.studentEmployeesData.collectAsState()
    var studentEmployee by remember { mutableStateOf(StudentEmployee()) }
    var searchQuery by remember { mutableStateOf("") }
    val tabs = listOf("Dosen", "Student Employee")
    val year = selectedYearBatch?.substring(range = IntRange(0,3))
    val batch = selectedYearBatch?.substring(range = IntRange(4,4))

    studentEmployeeList.forEach { studemp ->
        if(studemp.year==year && studemp.batch==batch) {
            studentEmployee = studemp
        }
    }

    val filteredList = memberList.filter {
        it.nama.contains(searchQuery, ignoreCase = true)
    }

    var topBarTitle by remember { mutableStateOf("Student Employee $year (Batch $batch)") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(topBarTitle, color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF19253F)
                )
            )
        },
        containerColor = Color(0xFFF9F9F9)
    ) { innerPadding ->
        val scope = rememberCoroutineScope()
        val pagerState = rememberPagerState(pageCount = { tabs.size })

        LaunchedEffect(selectedPage) {
            selectedPage?.let {
                pagerState.scrollToPage(it)
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            TabRow(
                selectedTabIndex = selectedPage ?: pagerState.currentPage,
                modifier = Modifier
                    .height(50.dp),
                indicator = {
                },
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        },
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .background(Color(0xFF19253F)),
                        content = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .padding(horizontal = 4.dp, vertical = 4.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (index == pagerState.currentPage) Color(0xFFE2640D) else Color(0xFF426193))
                                ,
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    title,
                                    fontSize = 14.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                        },
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()),

                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        topBarTitle = "Anggota Lab Sistem Informasi"
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
                    1 -> Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()),

                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        topBarTitle = "Student Employee $year (Batch $batch)"
                        ListDivisi(studentEmployee = studentEmployee, navController = navController)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MemberScreenPreview() {
    MemberScreen(
        navController = rememberNavController(),
        firestore = FirebaseFirestore.getInstance()
    )
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

@Composable
private fun ListDivisi(studentEmployee: StudentEmployee, navController: NavController) {
    studentEmployee.divisi.forEach { divisi ->
        DivisiCard(divisi = divisi, studemp = studentEmployee, navController = navController)
    }
}