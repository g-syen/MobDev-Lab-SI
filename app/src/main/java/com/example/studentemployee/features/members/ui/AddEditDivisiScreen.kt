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
import com.example.studentemployee.features.members.model.Divisi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditDivisiScreen(
    navController: NavController,
    studentEmployeeId: String, // ID of the parent StudentEmployee
    divisiId: String? = null,    // Null for Add, non-null for Edit
    viewModel: StudentEmployeeViewModel = viewModel()
) {
    var divisiName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val selectedDivisiForEdit by viewModel.selectedDivisi.collectAsState()

    LaunchedEffect(divisiId) {
        if (divisiId != null) {
            // Fetch or find the Divisi to edit
            // We need to ensure the correct StudentEmployee's context is used.
            // The ViewModel's getDivisiForEdit will find it from the currently loaded data.
            viewModel.getDivisiForEdit(studentEmployeeId, divisiId)
        } else {
            viewModel.clearSelectedDivisi()
            divisiName = "" // Clear for Add mode
        }
    }

    LaunchedEffect(selectedDivisiForEdit) {
        if (divisiId != null && selectedDivisiForEdit != null) {
            // Ensure we are editing the correct Divisi for the correct StudentEmployee
            // This check might be redundant if getDivisiForEdit is robust
            if (selectedDivisiForEdit!!.id == divisiId) {
                divisiName = selectedDivisiForEdit!!.divisi ?: ""
            }
        } else if (divisiId == null) {
            divisiName = ""
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (divisiId == null) "Add Divisi" else "Edit Divisi") },
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
                    if (divisiName.isNotBlank()) {
                        isLoading = true

                        val operationOnFailure: (String) -> Unit = { errorMsg ->
                            isLoading = false
                            Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_LONG).show()
                        }

                        if (divisiId == null) {
                            viewModel.addDivisi(
                                studentEmployeeId = studentEmployeeId,
                                divisiName = divisiName,
                                onSuccess = { newDivisiId ->
                                    isLoading = false
                                    Toast.makeText(context, "Divisi added successfully! ID: $newDivisiId", Toast.LENGTH_SHORT).show()
                                    navController.navigateUp()
                                },
                                onFailure = operationOnFailure
                            )
                        } else {
                            val divisiToUpdate = Divisi(id = divisiId, divisi = divisiName)
                            viewModel.updateDivisi(
                                studentEmployeeId = studentEmployeeId,
                                divisiToUpdate = divisiToUpdate,
                                onSuccess = {
                                    isLoading = false
                                    Toast.makeText(context, "Divisi saved successfully!", Toast.LENGTH_SHORT).show()
                                    navController.navigateUp()
                                },
                                onFailure = operationOnFailure
                            )
                        }
                    } else {
                        Toast.makeText(context, "Divisi name cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                },
                containerColor = Color(0xFFE2640D)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Filled.Save, "Save Divisi", tint = Color.White)
                }
            }
        },
        containerColor = Color(0xFFF9F9F9)
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
                value = divisiName,
                onValueChange = { divisiName = it },
                label = { Text("Division Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = divisiName.isBlank() && isLoading,
                colors = TextFieldDefaults.colors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black, focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
            )
        }
    }
}
