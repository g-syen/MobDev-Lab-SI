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
import com.example.studentemployee.features.members.model.Anggota

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAnggotaScreen(
    navController: NavController,
    studentEmployeeId: String,
    divisiId: String,
    kelompokId: String,
    anggotaId: String? = null, // Null for Add, non-null for Edit
    viewModel: StudentEmployeeViewModel = viewModel()
) {
    var nama by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val selectedAnggotaForEdit by viewModel.selectedAnggota.collectAsState()
    val errorFromViewModel by viewModel.error.collectAsState()

    LaunchedEffect(anggotaId) {
        if (anggotaId != null) {
            viewModel.getAnggotaForEdit(studentEmployeeId, divisiId, kelompokId, anggotaId)
        } else {
            viewModel.clearSelectedAnggota()
            nama = ""
            nim = ""
        }
    }

    LaunchedEffect(selectedAnggotaForEdit) {
        if (anggotaId != null && selectedAnggotaForEdit != null) {
            if (selectedAnggotaForEdit!!.id == anggotaId) {
                nama = selectedAnggotaForEdit!!.nama ?: ""
                nim = selectedAnggotaForEdit!!.nim ?: ""
            }
        } else if (anggotaId == null && selectedAnggotaForEdit == null) {
            nama = ""
            nim = ""
        }
    }

    LaunchedEffect(errorFromViewModel){
        errorFromViewModel?.let { Toast.makeText(context, "VM Error: $it", Toast.LENGTH_LONG).show() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (anggotaId == null) "Add Anggota" else "Edit Anggota") },
                navigationIcon = { IconButton(onClick = { navController.navigateUp() }) { Icon(Icons.Filled.ArrowBack, "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF19253F), titleContentColor = Color.White, navigationIconContentColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (nama.isNotBlank() && nim.isNotBlank()) {
                        isLoading = true
                        val operationOnFailure: (String) -> Unit = { errorMsg ->
                            isLoading = false; Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_LONG).show()
                        }
                        val anggotaData = Anggota(id = anggotaId ?: "", nama = nama, nim = nim) // ID is empty for add, populated for update

                        if (anggotaId == null) { // Add
                            viewModel.addAnggota(
                                studentEmployeeId, divisiId, kelompokId, anggotaData,
                                onSuccess = { _ -> // newAnggotaId
                                    isLoading = false; Toast.makeText(context, "Anggota added!", Toast.LENGTH_SHORT).show(); navController.navigateUp()
                                },
                                onFailure = operationOnFailure
                            )
                        } else { // Update
                            viewModel.updateAnggota(
                                studentEmployeeId, divisiId, kelompokId, anggotaData,
                                onSuccess = {
                                    isLoading = false; Toast.makeText(context, "Anggota updated!", Toast.LENGTH_SHORT).show(); navController.navigateUp()
                                },
                                onFailure = operationOnFailure
                            )
                        }
                    } else {
                        Toast.makeText(context, "Nama and NIM cannot be empty.", Toast.LENGTH_SHORT).show()
                    }
                },
                containerColor = Color(0xFFE2640D)
            ) {
                if (isLoading) { CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp) }
                else { Icon(Icons.Filled.Save, "Save Anggota", tint = Color.White) }
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = nama, onValueChange = { nama = it }, label = { Text("Nama Anggota") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = nama.isBlank() && isLoading, colors = TextFieldDefaults.colors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black, focusedContainerColor = Color.White, unfocusedContainerColor = Color.White))
            OutlinedTextField(value = nim, onValueChange = { nim = it }, label = { Text("NIM Anggota") }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = nim.isBlank() && isLoading, colors = TextFieldDefaults.colors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black, focusedContainerColor = Color.White, unfocusedContainerColor = Color.White))
        }
    }
}
