package com.example.studentemployee.features.members.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studentemployee.features.members.StudentEmployeeViewModel
import com.example.studentemployee.features.members.model.Kelompok

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditKelompokScreen(
    navController: NavController,
    studentEmployeeId: String,
    divisiId: String,
    kelompokId: String? = null, // Null for Add, non-null for Edit
    viewModel: StudentEmployeeViewModel = viewModel()
) {
    var kelompokValueString by remember { mutableStateOf("") } // Input as String
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val selectedKelompokForEdit by viewModel.selectedKelompok.collectAsState()
    val errorFromViewModel by viewModel.error.collectAsState()

    LaunchedEffect(kelompokId) {
        if (kelompokId != null) {
            viewModel.getKelompokForEdit(studentEmployeeId, divisiId, kelompokId)
        } else {
            viewModel.clearSelectedKelompok()
            kelompokValueString = ""
        }
    }

    LaunchedEffect(selectedKelompokForEdit) {
        if (kelompokId != null && selectedKelompokForEdit != null) {
            if (selectedKelompokForEdit!!.id == kelompokId) {
                kelompokValueString = selectedKelompokForEdit!!.kelompok?.toInt()?.toString() ?: ""
            }
        } else if (kelompokId == null && selectedKelompokForEdit == null) {
            kelompokValueString = ""
        }
    }

    LaunchedEffect(errorFromViewModel) {
        errorFromViewModel?.let { Toast.makeText(context, "VM Error: $it", Toast.LENGTH_LONG).show() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (kelompokId == null) "Add Kelompok" else "Edit Kelompok") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) { Icon(Icons.Filled.ArrowBack, "Back") }
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
                    val kelompokDoubleValue = kelompokValueString.toDoubleOrNull()
                    if (kelompokValueString.isNotBlank() && kelompokDoubleValue != null) {
                        isLoading = true
                        val operationOnFailure: (String) -> Unit = { errorMsg ->
                            isLoading = false
                            Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_LONG).show()
                        }

                        if (kelompokId == null) { // Add
                            viewModel.addKelompok(
                                studentEmployeeId = studentEmployeeId,
                                divisiId = divisiId,
                                kelompokValue = kelompokDoubleValue,
                                onSuccess = { _ -> // newKelompokId is available
                                    isLoading = false
                                    Toast.makeText(context, "Kelompok added successfully!", Toast.LENGTH_SHORT).show()
                                    navController.navigateUp()
                                },
                                onFailure = operationOnFailure
                            )
                        } else { // Update
                            val kelompokToUpdate = Kelompok(id = kelompokId, kelompok = kelompokDoubleValue)
                            viewModel.updateKelompok(
                                studentEmployeeId = studentEmployeeId,
                                divisiId = divisiId,
                                kelompokToUpdate = kelompokToUpdate,
                                onSuccess = {
                                    isLoading = false
                                    Toast.makeText(context, "Kelompok updated successfully!", Toast.LENGTH_SHORT).show()
                                    navController.navigateUp()
                                },
                                onFailure = operationOnFailure
                            )
                        }
                    } else {
                        Toast.makeText(context, "Kelompok number cannot be empty and must be a valid number.", Toast.LENGTH_SHORT).show()
                    }
                },
                containerColor = Color(0xFFE2640D)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Filled.Save, "Save Kelompok", tint = Color.White)
                }
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = kelompokValueString,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        kelompokValueString = newValue
                    }
                },
                label = { Text("Kelompok Number (e.g., 1, 2)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = kelompokValueString.isBlank() && isLoading,
                colors = TextFieldDefaults.colors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black, focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
            )
        }
    }
}
