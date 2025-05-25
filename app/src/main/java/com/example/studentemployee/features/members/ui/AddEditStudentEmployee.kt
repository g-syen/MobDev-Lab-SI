package com.example.studentemployee.features.members.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studentemployee.features.members.StudentEmployeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditStudentEmployeeScreen(
    navController: NavController,
    studentEmployeeId: String? = null,
    viewModel: StudentEmployeeViewModel = viewModel()
) {
    var year by remember { mutableStateOf("") }
    var batch by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val selectedStudentEmployee by viewModel.selectedStudentEmployee.collectAsState()
    val errorFromViewModel by viewModel.error.collectAsState() // Collect error from ViewModel

    LaunchedEffect(studentEmployeeId) {
        if (studentEmployeeId != null) {
            viewModel.getStudentEmployeeById(studentEmployeeId)
        } else {
            viewModel.clearSelectedStudentEmployee()
            year = ""
            batch = ""
        }
    }

    LaunchedEffect(selectedStudentEmployee) {
        if (studentEmployeeId != null && selectedStudentEmployee != null) {
            year = selectedStudentEmployee!!.year ?: ""
            batch = selectedStudentEmployee!!.batch ?: ""
        } else if (studentEmployeeId == null && selectedStudentEmployee == null) {
            // Clear fields only if truly in "add" mode (no selected employee)
            year = ""
            batch = ""
        }
    }

    // Optional: Show errors from ViewModel as Toasts
    LaunchedEffect(errorFromViewModel) {
        errorFromViewModel?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            // Consider having a method in ViewModel to clear the error after it's shown
            // viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (studentEmployeeId == null) "Add Student Employee" else "Edit Student Employee") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF19253F),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (year.isNotBlank() && batch.isNotBlank()) {
                        isLoading = true

                        // Corrected onFailure to accept String
                        val operationOnFailure: (String) -> Unit = { errorMessage ->
                            isLoading = false
                            Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
                        }

                        if (studentEmployeeId == null) { // ADD Case
                            viewModel.addStudentEmployee(
                                year = year,
                                batch = batch,
                                onSuccess = { newId -> // ViewModel expects (String) -> Unit
                                    isLoading = false
                                    Toast.makeText(context, "Saved successfully! New ID: $newId", Toast.LENGTH_SHORT).show()
                                    navController.navigateUp()
                                },
                                onFailure = operationOnFailure
                            )
                        } else { // EDIT Case
                            viewModel.updateStudentEmployee(
                                id = studentEmployeeId,
                                year = year,
                                batch = batch,
                                onSuccess = { // ViewModel expects () -> Unit
                                    isLoading = false
                                    Toast.makeText(context, "Saved successfully!", Toast.LENGTH_SHORT).show()
                                    navController.navigateUp()
                                },
                                onFailure = operationOnFailure
                            )
                        }
                    } else {
                        Toast.makeText(context, "Year and Batch cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                },
                containerColor = Color(0xFFE2640D)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Filled.Save, "Save", tint = Color.White)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Year (e.g., 2023)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = year.isBlank() && isLoading // Show error if blank during attempted save
            )
            OutlinedTextField(
                value = batch,
                onValueChange = { batch = it },
                label = { Text("Batch (e.g., 1 or A)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = batch.isBlank() && isLoading // Show error if blank during attempted save
            )
        }
    }
}
