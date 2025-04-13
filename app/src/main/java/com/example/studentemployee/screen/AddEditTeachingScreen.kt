package com.example.studentemployee.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.studentemployee.components.RoundedCard
import com.example.studentemployee.components.TopAppBarMenu
import com.example.studentemployee.data.Research
import com.example.studentemployee.data.Teaching
import com.example.studentemployee.viewmodel.TeachingViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddEditTeachingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: TeachingViewModel = viewModel(),
) {
    val userTeachings by viewModel.teachings.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(Unit) {
        if (userId != null) {
            viewModel.loadUserTeachings(userId)
        }
    }

    var teachingTitle by remember { mutableStateOf("") }
    var teachingClassName by remember { mutableStateOf("") }
    var teachingSemester by remember { mutableStateOf("") }
    var teachingYear by remember { mutableStateOf("") }
    var selectedTeaching by remember { mutableStateOf<Teaching?>(null) }

    var isEdit by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var titleError by remember { mutableStateOf(false) }
    var classNameError by remember { mutableStateOf(false) }
    var semesterError by remember { mutableStateOf(false) }
    var yearError by remember { mutableStateOf(false) }

    LaunchedEffect(selectedTeaching) {
        selectedTeaching?.let {
            teachingTitle = it.title
            teachingClassName = it.classname
            teachingSemester = it.semester
            teachingYear = it.year
        }
    }


    fun clearField() {
        teachingTitle = ""
        teachingClassName = ""
        teachingSemester = ""
        teachingYear = ""
        selectedTeaching = null
        isEdit = false
    }


    val dummyTeachings = listOf(
        Teaching(
            id = "1",
            title = "Pemrograman Mobile Lanjut",
            classname = "TIF-A",
            semester = "Genap",
            year = "2023"
        ),
        Teaching(
            id = "2",
            title = "Rekayasa Perangkat Lunak",
            classname = "TIF-B",
            semester = "Ganjil",
            year = "2022"
        ),
        Teaching(
            id = "3",
            title = "Manajemen Proyek TI",
            classname = "TIF-C",
            semester = "Genap",
            year = "2024"
        ),
        Teaching(
            id = "4",
            title = "Dasar Pemrograman",
            classname = "TIF-A",
            semester = "Ganjil",
            year = "2021"
        ),
        Teaching(
            id = "5",
            title = "Sistem Operasi",
            classname = "TIF-D",
            semester = "Genap",
            year = "2023"
        )
    )

    Scaffold(
        topBar = {
            TopAppBarMenu(
                onClick = { navController.navigateUp() },
                text = "Tambah & Edit Pengajaran"
            )
        },
        containerColor = Color(0xFFF9F9F9)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            RoundedCard {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    Text(
                        "Tambahkan / Edit Pengajaran",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0XFFF37619)
                    )
                    CustomDivider()
                    CustomTextField(
                        value = teachingTitle,
                        onValueChange = { teachingTitle = it },
                        label = "Masukkan Nama Mata Kuliah"
                    )
                    CustomTextField(
                        value = teachingClassName,
                        onValueChange = { teachingClassName = it },
                        label = "Masukkan Nama Kelas (ex: SI-A)"
                    )
                    SemesterDropdown(
                        selectedSemester = teachingSemester,
                        onSemesterSelected = { teachingSemester = it }
                    )
                    CustomTextField(
                        value = teachingYear,
                        onValueChange = { teachingYear = it },
                        label = "Masukkan Tahun Ajar (ex: 2024/2025)"
                    )
                    Spacer(Modifier.height(8.dp))
                    CustomButton(
                        text = if (isEdit) "Edit Pengajaran" else "Simpan Pengajaran",
                        onClick = {
                            titleError = teachingTitle.isBlank()
                            classNameError = teachingClassName.isBlank()
                            semesterError = teachingSemester.isBlank()
                            yearError = teachingYear.isBlank()

                            if (userId != null) {
                                if (titleError || classNameError || semesterError || yearError) {
                                    Toast.makeText(
                                        context,
                                        "Mohon lengkapi semua field dengan benar.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@CustomButton
                                }
                                if (!isEdit) {
                                    val newTeaching = Teaching(
                                        title = teachingTitle,
                                        classname = teachingClassName,
                                        semester = teachingSemester,
                                        year = teachingYear
                                    )
                                    viewModel.addTeaching(
                                        userId = userId,
                                        teaching = newTeaching,
                                        onSuccess = {
                                            Toast.makeText(
                                                context,
                                                "Berhasil disimpan",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            clearField()
                                        },
                                        onError = {
                                            Toast.makeText(
                                                context,
                                                "Gagal: ${it.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                } else {
                                    val updatedTeaching = Teaching(
                                        id = selectedTeaching?.id ?: "",
                                        title = teachingTitle,
                                        classname = teachingClassName,
                                        semester = teachingSemester,
                                        year = teachingYear
                                    )
                                    viewModel.updateTeaching(
                                        userId = userId,
                                        teaching = updatedTeaching,
                                        onSuccess = {
                                            Toast.makeText(
                                                context,
                                                "Berhasil diupdate",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            clearField()
                                        },
                                        onError = {
                                            Toast.makeText(
                                                context,
                                                "Gagal: ${it.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                }
                            }
                        },

                    )
                }
            }
            Text(
                "Pengajaran yang telah diunggah",
                modifier = modifier.padding(start = 24.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0XFFF37619)
            )
            LazyColumn(modifier = modifier.padding(horizontal = 24.dp)) {
                items(userTeachings) { teaching ->
                    TeachingCard(
                        teaching = teaching,
                        onEditTeaching = {
                            selectedTeaching = teaching
                            isEdit = true
                        },
                        onDeleteTeaching = {
                            if (userId != null && teaching.id.isNotEmpty()) {
                                viewModel.deleteTeaching(
                                    userId = userId,
                                    teachingId = teaching.id,
                                    onSuccess = {
                                        clearField()
                                        Toast.makeText(
                                            context,
                                            "Berhasil dihapus",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onError = {
                                        Toast.makeText(
                                            context,
                                            "Gagal hapus: ${it.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun AddEditTeachingScreenPreview() {
    AddEditTeachingScreen(navController = rememberNavController())
}

@Composable
fun TeachingCard(
    teaching: Teaching,
    modifier: Modifier = Modifier,
    onEditTeaching: () -> Unit,
    onDeleteTeaching: () -> Unit

) {
    ElevatedCard(
        modifier = modifier
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = teaching.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "${teaching.classname} | ${teaching.semester} ${teaching.year}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = FontStyle.Italic
                )
            }

            Row(
            ) {
                IconButton(onClick = onEditTeaching) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Research Icon",
                        tint = Color(0xFF426193) // contoh warna biru untuk edit
                    )
                }
                IconButton(onClick = onDeleteTeaching) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Research Icon",
                        tint = Color(0xFFE55304) // contoh warna oranye/merah untuk delete
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TeachingCardPreview() {
    TeachingCard(
        teaching = Teaching(
            id = "",
            title = "Teknologi Blockchain dan Platform keuangan dijital",
            classname = "TIF-B",
            semester = "Genap",
            year = "2024/2025"
        ),
        onEditTeaching = {},
        onDeleteTeaching = {}
    )
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SemesterDropdown(
    selectedSemester: String,
    onSemesterSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val list = listOf("Ganjil", "Genap")
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExposedDropdownMenuBox(
            modifier = modifier.fillMaxWidth(),
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }
        ) {
            TextField(
                modifier = modifier.fillMaxWidth(),
                value = selectedSemester,
                onValueChange = {},
                label = {
                    Text(
                        "Masukkan Semester Pengajaran",
                        color = Color(0xFFE2640D),
                        fontSize = 13.sp
                    )
                },
                readOnly = true,
                textStyle = TextStyle(
                    color = Color(0xFF195693),
                    fontSize = 16.sp
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0XFFF9F9F9),
                    cursorColor = Color(0xFF195693),
                    focusedIndicatorColor = Color(0XFF195693),
                    unfocusedIndicatorColor = Color(0XFF195693)
                ),
                trailingIcon = {
                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                        contentDescription = "Dropdown Icon",
                        tint = Color(0xFF195693)
                    )
                }
            )

            DropdownMenu(
                modifier = modifier
                    .background(Color.White)
                    .exposedDropdownSize(),
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                list.forEach { option ->
                    DropdownMenuItem(
                        modifier = modifier.fillMaxWidth(),
                        text = {
                            Text(
                                text = option,
                                color = Color(0xFF195693)
                            )
                        },
                        onClick = {
                            onSemesterSelected(option)
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }
}
