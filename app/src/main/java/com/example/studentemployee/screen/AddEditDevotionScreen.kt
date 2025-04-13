package com.example.studentemployee.screen

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.studentemployee.data.Devotion
import com.example.studentemployee.data.Research
import com.example.studentemployee.viewmodel.DevotionViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddEditDevotionScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: DevotionViewModel = viewModel(),
) {
    val userDevotions by viewModel.devotions.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (userId != null) {
            viewModel.loadUserDevotions(userId)
        }
    }

    var devotionTitle by remember { mutableStateOf("") }
    var devotionContributors by remember { mutableStateOf("") }
    var devotionUrl by remember { mutableStateOf("") }

    var isEdit by remember { mutableStateOf(false) }
    var selectedDevotion by remember { mutableStateOf<Devotion?>(null) }

    var titleError by remember { mutableStateOf(false) }
    var contributorsError by remember { mutableStateOf(false) }
    var urlError by remember { mutableStateOf(false) }

    LaunchedEffect(selectedDevotion) {
        selectedDevotion?.let {
            devotionTitle = it.title
            devotionContributors = it.contributors
            devotionUrl = it.link
        }
    }



    fun clearField() {
        devotionTitle = ""
        devotionContributors = ""
        devotionUrl = ""
        selectedDevotion = null
        isEdit = false
    }

    fun isValidUrl(url: String): Boolean {
        return url.startsWith("http://") || url.startsWith("https://")
    }

    val sampleDevotions = listOf(
        Devotion(
            id = "1",
            title = "Pelatihan Digital Marketing untuk UMKM di Kota Batu",
            link = "https://example.com/pengabdian1",
            contributors = "Dewi Kartika, Budi Santoso, Rina Marlina"
        ),
        Devotion(
            id = "2",
            title = "Pendampingan Pembelajaran Daring di Sekolah Dasar",
            link = "https://example.com/pengabdian2",
            contributors = "Andi Wijaya, Siti Nurhaliza"
        ),
        Devotion(
            id = "3",
            title = "Pelatihan Literasi Digital untuk Guru PAUD",
            link = "https://example.com/pengabdian3",
            contributors = "Dewi Kartika, Rudi Hartono, Intan Permata"
        ),
        Devotion(
            id = "4",
            title = "Pemberdayaan Masyarakat Desa Melalui Aplikasi Pertanian Pintar",
            link = "https://example.com/pengabdian4",
            contributors = "Siti Nurhaliza, Ahmad Fauzi"
        ),
        Devotion(
            id = "5",
            title = "Workshop Pembuatan Konten Edukasi untuk Remaja",
            link = "https://example.com/pengabdian5",
            contributors = "Budi Santoso, Eka Putri, Dewa Bagus"
        )
    )

    Scaffold(
        topBar = {
            TopAppBarMenu(
                onClick = { navController.navigateUp() },
                text = "Tambah & Edit Pengabdian"
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
                        "Tambahkan / Edit Pengabdian",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0XFFF37619)
                    )
                    CustomDivider()
                    CustomTextField(
                        value = devotionTitle,
                        onValueChange = { devotionTitle = it },
                        label = "Masukkan Nama Pengabdian"
                    )
                    CustomTextField(
                        value = devotionContributors,
                        onValueChange = { devotionContributors = it },
                        label = "Masukkan Kontributor Pengabdian"
                    )
                    CustomTextField(
                        value = devotionUrl,
                        onValueChange = {
                            devotionUrl = it
                            urlError = false
                        },
                        label = "Masukkan Link Pengabdian",
                        isError = urlError,
                        supportingText = {
                            if (urlError) Text(
                                "Link harus dimulai dengan http:// atau https://",
                                color = Color.Red
                            )
                        }
                    )
                    Spacer(Modifier.height(8.dp))
                    CustomButton(
                        text = if (isEdit) "Edit Pengabdian" else "Simpan Pengabdian",
                        onClick = {titleError = devotionTitle.isBlank()
                            contributorsError = devotionContributors.isBlank()
                            urlError = devotionUrl.isBlank() || !isValidUrl(devotionUrl)

                            if (userId != null) {
                                if (titleError || contributorsError || urlError) {
                                    Toast.makeText(
                                        context,
                                        "Mohon lengkapi semua field dengan benar.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@CustomButton
                                }
                                if (!isEdit) {
                                    //add new devotion
                                    val newDevotion = Devotion(
                                        title = devotionTitle,
                                        contributors = devotionContributors,
                                        link = devotionUrl
                                    )
                                    viewModel.addDevotion(
                                        userId,
                                        newDevotion,
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
                                    //update devotion
                                    val updatedDevotion = Devotion(
                                        id = selectedDevotion?.id ?: "",
                                        title = devotionTitle,
                                        contributors = devotionContributors,
                                        link = devotionUrl,
                                    )
                                    viewModel.updateDevotion(
                                        userId = userId,
                                        devotion = updatedDevotion,
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
                "Pengabdian yang telah diunggah",
                modifier = modifier.padding(start = 24.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0XFFF37619)
            )
            LazyColumn(modifier = modifier.padding(horizontal = 24.dp)) {
                items(userDevotions) { devotion ->
                    DevotionCard(
                        devotion = devotion,
                        onEditDevotion = {
                            selectedDevotion = devotion
                            isEdit = true
                        },
                        onDeleteDevotion = {
                            if (userId != null && devotion.id.isNotEmpty()) {
                                viewModel.deleteDevotion(
                                    userId = userId,
                                    devotionId = devotion.id,
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
private fun AddEditDevotionScreenPreview() {
    AddEditDevotionScreen(navController = rememberNavController())
}

@Composable
fun DevotionCard(
    devotion: Devotion,
    modifier: Modifier = Modifier,
    onEditDevotion: () -> Unit,
    onDeleteDevotion: () -> Unit

) {
    val context = LocalContext.current
    ElevatedCard(
        modifier = modifier
            .padding(vertical = 8.dp)
            .clickable {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(devotion.link))
                context.startActivity(intent)
            },
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
                    text = devotion.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = devotion.contributors,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = FontStyle.Italic
                )
            }

            Row {
                IconButton(onClick = onEditDevotion) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Devotion Icon",
                        tint = Color(0xFF426193) // contoh warna biru untuk edit
                    )
                }
                IconButton(onClick = onDeleteDevotion) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Devotion Icon",
                        tint = Color(0xFFE55304) // contoh warna oranye/merah untuk delete
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DevotionCardPreview() {
    DevotionCard(
        devotion = Devotion(
            id = "",
            title = "2023. Pelatihan Penguatan Layanan Pendidikan di POS PAUD Kasih Sayang Kota Malang",
            contributors = "Dewi Kartika, Budi Santoso, Rina Marlina, Andi Wijaya, Siti Nurhaliza",
            link = "",
        ),
        onEditDevotion = {},
        onDeleteDevotion = {}
    )
}