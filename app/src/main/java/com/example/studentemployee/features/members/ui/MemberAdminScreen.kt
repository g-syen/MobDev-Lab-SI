package com.example.studentemployee.features.members.ui

import android.util.Log
import com.example.studentemployee.core.components.StudentEmployeeCardAdmin
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studentemployee.features.members.MemberViewModel
import com.example.studentemployee.features.members.StudentEmployeeViewModel
import com.example.studentemployee.features.members.model.StudentEmployee
import com.example.studentemployee.features.members.model.User // Import User model
// Import ListMemberAdmin and MemberAdminCard if they are in a different package
// For example, if ListMemberAdmin is from the user's MemberAdminScreen.kt:
// import com.example.studentemployee.features.members.ui.ListMemberAdmin
import com.example.studentemployee.core.components.SearchBar
import com.example.studentemployee.core.components.MemberAdminCard // Assuming this exists
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberAdminScreen(
    navController: NavController,
    memberViewModel: MemberViewModel = viewModel(),
    seViewModel: StudentEmployeeViewModel = viewModel(),
    initialSelectedPage: Int? = null
) {
    val memberList by memberViewModel.memberList.collectAsState()
    val studentEmployeeList by seViewModel.studentEmployeesData.collectAsState()
    var searchQueryDosen by remember { mutableStateOf("") }
    var searchQuerySE by remember { mutableStateOf("") } // Search for Student Employees

    val tabs = listOf("Dosen", "Student Employee")
    val context = LocalContext.current

    // Hoist PagerState to be used by TabRow, HorizontalPager, and FAB
    val pagerState = rememberPagerState(
        initialPage = initialSelectedPage ?: 0,
        pageCount = { tabs.size }
    )
    val scope = rememberCoroutineScope()

    val filteredDosenList = memberList.filter {
        it.nama.contains(searchQueryDosen, ignoreCase = true) && it.role == "member"
    }

    // Filter for Student Employees (example, adjust field if needed)
    val filteredStudentEmployeeList = studentEmployeeList.filter {
        (it.year ?: "").contains(searchQuerySE, ignoreCase = true) ||
                (it.batch ?: "").contains(searchQuerySE, ignoreCase = true)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Members", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF19253F))
            )
        },
        floatingActionButton = {
            // Show FAB based on the current active tab
            when (pagerState.currentPage) {
                0 -> { // Dosen Admin Tab
                    FloatingActionButton(
                        onClick = { navController.navigate("addEditMember") },
                        containerColor = Color(0xFF10375E),
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Filled.Add, "Add Dosen")
                    }
                }
                1 -> {
                    FloatingActionButton(
                        onClick = { navController.navigate("addEditStudentEmployee") },
                        containerColor = Color(0xFFE2640D),
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Filled.Add, "Add Student Employee")
                    }
                }
            }
        },
        containerColor = Color(0xFFF9F9F9)
    ) { innerPadding ->
        LaunchedEffect(initialSelectedPage) {
            initialSelectedPage?.let {
                if (it >= 0 && it < tabs.size) {
                    pagerState.scrollToPage(it)
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.height(50.dp),
                indicator = {}, // Your existing empty indicator
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
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
                                    .background(if (index == pagerState.currentPage) Color(0xFFE2640D) else Color(0xFF426193)),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(title, fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
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
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SearchBar(
                            value = searchQueryDosen,
                            onValueChange = { searchQueryDosen = it },
                            label = "Search Dosen",
                            modifier = Modifier.fillMaxWidth(),
                            onSearch = {}
                        )
                        ListMemberAdmin(
                            memberList = filteredDosenList,
                            navController = navController,
                            onClickEdit = { user ->
                                user.id.let { memberId ->
                                    navController.navigate("addEditMember/$memberId")
                                }
                            },
                            onClickDelete = { user ->
                                memberViewModel.deleteMember(user, firestore = FirebaseFirestore.getInstance(), auth = FirebaseAuth.getInstance())
                                Toast.makeText(context, "Member lab ${user.nama} berhasil dihapus.", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                    1 -> Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ListStudentEmployeeAdmin(
                            studentEmployeeList = filteredStudentEmployeeList,
                            navController = navController,
                            viewModel = seViewModel
                        )
                    }
                }
            }
        }
    }
}

// This ListMemberAdmin should match the one from your MemberAdminScreen.kt
// If it's in a different file/package, make sure to import it.
// For clarity, I'm including a compatible definition here.
// Ensure your User model has an 'id' field.
@Composable
fun ListMemberAdmin(
    modifier: Modifier = Modifier,
    memberList: List<User>,
    navController: NavController,
    onClickEdit: (User) -> Unit,
    onClickDelete: (User) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(), // Ensure it takes width for alignment
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (memberList.isEmpty()) {
            Text(
                "No Dosen found.",
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
            )
        }
        memberList.forEach { user ->
            // Assuming user.role is how you filter for "Dosen" if not already done
            // if (user.role == "member") { // This filter is already applied in filteredDosenList
            MemberAdminCard( // This is your custom card for Dosen
                member = user,
                navController = navController, // navController might not be needed directly by card if actions are lambdas
                onClickEdit = { onClickEdit(user) },
                onClickDelete = { onClickDelete(user) }
            )
            // }
        }
    }
}


// ListStudentEmployeeAdmin (from previous generation, ensure it's available)
@Composable
fun ListStudentEmployeeAdmin(
    studentEmployeeList: List<StudentEmployee>,
    navController: NavController,
    viewModel: StudentEmployeeViewModel
) {
    val context = LocalContext.current

    if (studentEmployeeList.isEmpty()) {
        Text(
            "No Student Employees found. Click the '+' button to add.",
            modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally).padding(16.dp)
        )
    }
    studentEmployeeList.forEach { studemp ->
        StudentEmployeeCardAdmin(
            studemp = studemp,
            onEdit = { id ->
                navController.navigate("addEditStudentEmployee/$id")
            },
            onDelete = { id ->
                viewModel.deleteStudentEmployee(
                    id = id,
                    onSuccess = { Toast.makeText(context, "Berhasil dihapus.", Toast.LENGTH_SHORT).show() },
                    onFailure = { eMsg -> Toast.makeText(context, "Error menghapus: $eMsg", Toast.LENGTH_LONG).show() }
                )
            },
            navController = navController
        )
    }
}

