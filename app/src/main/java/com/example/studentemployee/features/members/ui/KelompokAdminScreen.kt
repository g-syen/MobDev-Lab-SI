package com.example.studentemployee.features.admin.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
// import androidx.compose.material.icons.filled.KeyboardArrowDown
// import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import com.example.studentemployee.core.components.AnggotaAdminCard
import com.example.studentemployee.features.members.StudentEmployeeViewModel
import com.example.studentemployee.features.members.model.Divisi
import com.example.studentemployee.features.members.model.Kelompok
import com.example.studentemployee.features.members.model.StudentEmployee

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KelompokAdminScreen(
    navController: NavController,
    studentEmployeeId: String?,
    divisiId: String?,
    seViewModel: StudentEmployeeViewModel = viewModel()
) {
    val context = LocalContext.current
    val studentEmployeeList by seViewModel.studentEmployeesData.collectAsState()
    var selectedDivisi : Divisi? = null
    val isLoading by seViewModel.isLoading.collectAsState()
    val error by seViewModel.error.collectAsState()
    var selectedStudentEmployee : StudentEmployee? = null

    studentEmployeeList.forEach { studemp ->
        Log.e("studemp", "${studemp.year}, ${studemp.batch}")
        if(studemp.id == studentEmployeeId) {
            selectedStudentEmployee = studemp
        }
    }

    selectedStudentEmployee?.divisi?.forEach { div ->
        if(div.id == divisiId) {
            selectedDivisi = div
        }
    }

    var expandedKelompokId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(error) {
        error?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedDivisi?.divisi ?: "Divisi Tidak Ternama", color = Color.White, fontSize = 15.sp) },
                navigationIcon = { IconButton(onClick = { navController.navigateUp() }) { Icon(Icons.Default.ArrowBack, "Back", tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF19253F))
            )
        },
        floatingActionButton = {
            if (studentEmployeeId != null && divisiId != null) {
                FloatingActionButton(
                    onClick = { navController.navigate("addEditKelompok/$studentEmployeeId/$divisiId") },
                    containerColor = Color(0xFFE2640D) // FAB for adding Kelompok
                ) { Icon(Icons.Filled.Add, "Add Kelompok", tint = Color.White) }
            }
        },
        containerColor = Color(0xFFF9F9F9)
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(horizontal = 8.dp, vertical=16.dp)) {
            if (isLoading && selectedDivisi == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            } else if (selectedStudentEmployee == null || selectedDivisi == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Student Employee or Divisi not found.") }
            } else {
                val currentDivisi = selectedDivisi!!
                // Display SE and Divisi context
                Text("Student Employee: ${selectedStudentEmployee!!.year ?: ""} Batch ${selectedStudentEmployee!!.batch ?: ""}", style = MaterialTheme.typography.titleMedium)
                Text("Divisi: ${currentDivisi.divisi ?: "N/A"}", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(bottom = 8.dp))


                if (currentDivisi.kelompok.isNullOrEmpty()) {
                    Text("No Kelompok found. Click '+' FAB to add a Kelompok.", modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp))
                } else {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        currentDivisi.kelompok.forEach { kelompok ->
                            selectedStudentEmployee!!.id?.let {
                                currentDivisi.id?.let { it1 ->
                                    KelompokAdminSection( // New composable for Kelompok with its Anggota
                                        kelompok = kelompok,
                                        studentEmployeeId = it,
                                        divisiId = it1,
                                        navController = navController,
                                        seViewModel = seViewModel,
                                        isExpanded = expandedKelompokId == kelompok.id,
                                        onToggleExpand = {
                                            expandedKelompokId = if (expandedKelompokId == kelompok.id) null else kelompok.id
                                        }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KelompokAdminSection(
    kelompok: Kelompok,
    studentEmployeeId: String,
    divisiId: String,
    navController: NavController,
    seViewModel: StudentEmployeeViewModel,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            // Kelompok Header with Edit/Delete for Kelompok itself
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Kelompok ${kelompok.kelompok?.toInt() ?: "N/A"}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row {
                    IconButton(onClick = { navController.navigate("addEditKelompok/$studentEmployeeId/$divisiId/${kelompok.id}") }) {
                        Icon(Icons.Filled.Edit, "Edit Kelompok")
                    }
                    var showDeleteKelompokDialog by remember { mutableStateOf(false) }
                    IconButton(onClick = { showDeleteKelompokDialog = true }) {
                        Icon(Icons.Filled.Delete, "Delete Kelompok")
                    }
                    if (showDeleteKelompokDialog) {
                        AlertDialog(
                            onDismissRequest = { showDeleteKelompokDialog = false },
                            title = { Text("Delete Kelompok") },
                            text = { Text("Are you sure you want to delete Kelompok ${kelompok.kelompok?.toInt()}? This will also delete all its anggota.") },
                            confirmButton = { TextButton(onClick = {
                                kelompok.id?.let {
                                    seViewModel.deleteKelompok(studentEmployeeId, divisiId, it,
                                        onSuccess = { Toast.makeText(context, "Kelompok deleted", Toast.LENGTH_SHORT).show() },
                                        onFailure = { errMsg -> Toast.makeText(context, "Error: $errMsg", Toast.LENGTH_LONG).show() }
                                    )
                                }
                                showDeleteKelompokDialog = false
                            }) { Text("Delete") } },
                            dismissButton = { TextButton(onClick = { showDeleteKelompokDialog = false }) { Text("Cancel") } }
                        )
                    }
                    // Expand/Collapse Icon (Optional - for now, always show Anggota if any)
                    // IconButton(onClick = onToggleExpand) {
                    //    Icon(if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown, "Expand/Collapse Anggota")
                    // }
                }
            }

            // Anggota List for this Kelompok
            // if (isExpanded) { // Or always show if not too many
            Column(modifier = Modifier.padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)) {
                if (kelompok.anggota.isNullOrEmpty()) {
                    Text("No Anggota in this Kelompok.", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 8.dp))
                } else {
                    kelompok.anggota.forEach { anggota ->
                        AnggotaAdminCard(
                            anggota = anggota,
                            onEdit = { selectedAnggota ->
                                navController.navigate("addEditAnggota/$studentEmployeeId/$divisiId/${kelompok.id}/${selectedAnggota.id}")
                            },
                            onDelete = { anggotaToDelete ->
                                kelompok.id?.let {
                                    anggotaToDelete.id?.let { it1 ->
                                        seViewModel.deleteAnggota(
                                            studentEmployeeId, divisiId, it, it1,
                                            onSuccess = { Toast.makeText(context, "Anggota '${anggotaToDelete.nama}' deleted", Toast.LENGTH_SHORT).show() },
                                            onFailure = { errMsg -> Toast.makeText(context, "Error: $errMsg", Toast.LENGTH_LONG).show() }
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
                // Add Anggota Button for this specific Kelompok
                Button(
                    onClick = { navController.navigate("addEditAnggota/$studentEmployeeId/$divisiId/${kelompok.id}") },
                    modifier = Modifier.padding(top = 8.dp).align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(Icons.Filled.Add, "Add Anggota", tint = MaterialTheme.colorScheme.onSecondary)
                    Spacer(Modifier.width(4.dp))
                    Text("Add Anggota", color = MaterialTheme.colorScheme.onSecondary)
                }
            }
            // }
        }
    }
}
