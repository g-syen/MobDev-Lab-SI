package com.example.studentemployee.features.members.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studentemployee.features.members.StudentEmployeeViewModel
import com.example.studentemployee.features.members.model.Divisi
import com.example.studentemployee.core.components.DivisiAdminCard
import com.example.studentemployee.core.components.RoundedCard
import com.example.studentemployee.features.members.model.StudentEmployee

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DivisiAdminScreen(
    navController: NavController,
    selectedYearBatch: String?,
    seViewModel: StudentEmployeeViewModel = viewModel()
) {
    val context = LocalContext.current
    val isLoading by seViewModel.isLoading.collectAsState()
    val error by seViewModel.error.collectAsState()
    val studentEmployeeList by seViewModel.studentEmployeesData.collectAsState()
    var selectedStudentEmployee : StudentEmployee? = null

    val year = remember(selectedYearBatch) { selectedYearBatch?.takeIf { it.length >= 5 }?.substring(0, 4) }
    val batch = remember(selectedYearBatch) { selectedYearBatch?.takeIf { it.length >= 5 }?.substring(4, 5) }

    studentEmployeeList.forEach { studemp ->
        Log.e("studemp", "${studemp.year}, ${studemp.batch}")
        if(studemp.year == year && studemp.batch == batch) {
            selectedStudentEmployee = studemp
        }
    }

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "SE ${selectedStudentEmployee?.year ?: year ?: ""} (Batch ${selectedStudentEmployee?.batch ?: batch ?: ""})",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF19253F))
            )
        },
        floatingActionButton = {
            selectedStudentEmployee?.id?.let { studentEmployeeId ->
                FloatingActionButton(
                    onClick = {
                        navController.navigate("addeditdivisi/$studentEmployeeId")
                    },
                    containerColor = Color(0xFFE2640D)
                ) {
                    Icon(Icons.Filled.Add, "Add Divisi", tint = Color.White)
                }
            }
        },
        containerColor = Color(0xFFF9F9F9)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (isLoading && selectedStudentEmployee == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (selectedStudentEmployee == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Student Employee not found for ${year ?: ""} Batch ${batch ?: ""}.\nPlease ensure data is loaded or selection is correct.")
                }
            } else {
                val currentStudentEmployee = selectedStudentEmployee!!

                if (currentStudentEmployee.divisi.isEmpty()) {
                    Text(
                        "No divisions found for this Student Employee. Click '+' to add.",
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
                    )
                } else {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        currentStudentEmployee.divisi.forEach { divisi ->
                            DivisiAdminCard(
                                divisi = divisi,
                                onEdit = { selectedDivisi ->
                                    // Navigate to AddEditDivisiScreen for editing
                                    navController.navigate("addEditDivisi/${currentStudentEmployee.id}/${selectedDivisi.id}")
                                },
                                onDelete = { divisiToDelete ->
                                    currentStudentEmployee.id?.let {
                                        divisiToDelete.id?.let { it1 ->
                                            seViewModel.deleteDivisi(
                                                studentEmployeeId = it,
                                                divisiId = it1,
                                                onSuccess = {
                                                    Toast.makeText(context, "'${divisiToDelete.divisi}' deleted", Toast.LENGTH_SHORT).show()
                                                    // List should auto-update via snapshot listener
                                                },
                                                onFailure = { errorMsg ->
                                                    Toast.makeText(context, "Delete failed: $errorMsg", Toast.LENGTH_LONG).show()
                                                }
                                            )
                                        }
                                    }
                                },
                                studemp = currentStudentEmployee,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}
