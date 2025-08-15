package com.example.studentemployee.features.research.ui

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
import androidx.compose.foundation.layout.imePadding
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
import com.example.studentemployee.core.components.RoundedCard
import com.example.studentemployee.core.components.TopAppBarMenu
import com.example.studentemployee.features.research.model.Research
import com.example.studentemployee.features.profile.ui.CustomButton
import com.example.studentemployee.features.profile.ui.CustomDivider
import com.example.studentemployee.features.profile.ui.CustomTextField
import com.example.studentemployee.features.research.ResearchViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddEditResearchContent(
    modifier: Modifier = Modifier,
    researches: List<Research>,
    researchTitle: String,
    researchAuthor: String,
    researchUrl: String,
    isEdit: Boolean,
    urlError: Boolean,
    onTitleChange: (String) -> Unit,
    onAuthorChange: (String) -> Unit,
    onUrlChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onEditResearch: (Research) -> Unit,
    onDeleteResearch: (Research) -> Unit,
    onNavigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBarMenu(
                onClick = onNavigateUp,
                text = "Tambah & Edit Penelitian"
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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Tambahkan / Edit Penelitian",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0XFFF37619)
                    )
                    CustomDivider()
                    CustomTextField(
                        value = researchTitle,
                        onValueChange = onTitleChange,
                        label = "Masukkan Judul Penelitian"
                    )
                    CustomTextField(
                        value = researchAuthor,
                        onValueChange = onAuthorChange,
                        label = "Masukkan Penulis Penelitian"
                    )
                    CustomTextField(
                        value = researchUrl,
                        onValueChange = onUrlChange,
                        label = "Masukkan Link Penelitian",
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
                        onClick = onSaveClick,
                        text = if (isEdit) "Edit Penelitian" else "Simpan Penelitian"
                    )
                }
            }
            Text(
                "Penelitian yang telah diunggah",
                modifier = modifier.padding(start = 24.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0XFFF37619)
            )

            LazyColumn(modifier = modifier.imePadding().padding(horizontal = 24.dp)) {
                items(researches) { research ->
                    ResearchCard(
                        research = research,
                        onEditResearch = { onEditResearch(research) },
                        onDeleteResearch = { onDeleteResearch(research) }
                    )
                }
            }
        }
    }
}

@Composable
fun AddEditResearchScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ResearchViewModel = viewModel(),
) {
    val userResearches by viewModel.researches.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (userId != null) {
            viewModel.loadUserResearches(userId)
        }
    }

    var researchTitle by remember { mutableStateOf("") }
    var researchAuthor by remember { mutableStateOf("") }
    var researchUrl by remember { mutableStateOf("") }
    var isEdit by remember { mutableStateOf(false) }
    var selectedResearch by remember { mutableStateOf<Research?>(null) }

    var titleError by remember { mutableStateOf(false) }
    var authorError by remember { mutableStateOf(false) }
    var urlError by remember { mutableStateOf(false) }
    var urlInvalid by remember { mutableStateOf(false) }

    LaunchedEffect(selectedResearch) {
        selectedResearch?.let {
            researchTitle = it.title
            researchAuthor = it.authors
            researchUrl = it.link
        }
    }

    fun clearField() {
        researchTitle = ""
        researchAuthor = ""
        researchUrl = ""
        selectedResearch = null
        isEdit = false
    }

    fun isValidUrl(url: String): Boolean {
        return url.startsWith("http://") || url.startsWith("https://")
    }

    fun handleSaveClick() {
        titleError = researchTitle.isBlank()
        authorError = researchAuthor.isBlank()
        urlError = researchUrl.isBlank()
        urlInvalid = !isValidUrl(researchUrl)

        if (userId != null) {
            if (titleError || authorError || urlError) {
                Toast.makeText(
                    context,
                    "Mohon lengkapi semua field dengan benar.",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            if (urlInvalid) {
                Toast.makeText(
                    context,
                    "Mohon mulai link penelitian dengan \"http\" atau \"https\"",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            if (!isEdit) {
                //add new research
                val newResearch = Research(
                    title = researchTitle,
                    authors = researchAuthor,
                    link = researchUrl
                )
                viewModel.addResearch(
                    userId,
                    newResearch,
                    onSuccess = {
                        Toast.makeText(context, "Berhasil disimpan", Toast.LENGTH_SHORT).show()
                        clearField()
                    },
                    onError = {
                        Toast.makeText(context, "Gagal: ${it.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
            } else {
                val updatedResearch = Research(
                    id = selectedResearch?.id ?: "",
                    title = researchTitle,
                    authors = researchAuthor,
                    link = researchUrl,
                )
                viewModel.updateResearch(
                    userId = userId,
                    research = updatedResearch,
                    onSuccess = {
                        Toast.makeText(context, "Berhasil diupdate", Toast.LENGTH_SHORT).show()
                        clearField()
                    },
                    onError = {
                        Toast.makeText(context, "Gagal: ${it.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
            }
        }
    }

    // Call the stateless UI composable
    AddEditResearchContent(
        modifier = modifier,
        researches = userResearches,
        researchTitle = researchTitle,
        researchAuthor = researchAuthor,
        researchUrl = researchUrl,
        isEdit = isEdit,
        urlError = urlError,
        onTitleChange = { researchTitle = it },
        onAuthorChange = { researchAuthor = it },
        onUrlChange = {
            researchUrl = it
            urlError = false
        },
        onSaveClick = { handleSaveClick() },
        onEditResearch = {
            selectedResearch = it
            isEdit = true
        },
        onDeleteResearch = { research ->
            if (userId != null && research.id.isNotEmpty()) {
                viewModel.deleteResearch(
                    userId = userId,
                    researchId = research.id,
                    onSuccess = {
                        clearField()
                        Toast.makeText(context, "Berhasil dihapus", Toast.LENGTH_SHORT).show()
                    },
                    onError = {
                        Toast.makeText(context, "Gagal hapus: ${it.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
            }
        },
        onNavigateUp = { navController.navigateUp() }
    )
}

@Preview(showBackground = true)
@Composable
private fun AddEditResearchScreenPreview() {
    // Provide sample data for the preview
    val sampleResearches = listOf(
        Research(
            id = "1",
            title = "Implementasi Machine Learning untuk Prediksi Penyakit",
            authors = "Dewi Kartika, S.Kom., M.Sc.; Andi Susanto, M.T.",
            link = "https://ieee.com/"
        ),
        Research(
            id = "2",
            title = "Analisis Sentimen di Media Sosial Menggunakan Deep Learning",
            authors = "Budi Hartono, Ph.D.",
            link = "https://ieee.com/"
        )
    )

    // Call the stateless content composable with the sample data
    AddEditResearchContent(
        researches = sampleResearches,
        researchTitle = "Contoh Judul",
        researchAuthor = "Contoh Penulis",
        researchUrl = "",
        isEdit = false,
        urlError = false,
        onTitleChange = {},
        onAuthorChange = {},
        onUrlChange = {},
        onSaveClick = {},
        onEditResearch = {},
        onDeleteResearch = {},
        onNavigateUp = {}
    )
}

@Composable
fun ResearchCard(
    research: Research,
    modifier: Modifier = Modifier,
    onEditResearch: () -> Unit,
    onDeleteResearch: () -> Unit

) {
    val context = LocalContext.current
    ElevatedCard(
        modifier = modifier
            .padding(vertical = 8.dp)
            .clickable {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(research.link))
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
                    text = research.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = research.authors,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = FontStyle.Italic
                )
            }

            Row{
                IconButton(onClick = onEditResearch) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Research Icon",
                        tint = Color(0xFF426193) // contoh warna biru untuk edit
                    )
                }
                IconButton(onClick = onDeleteResearch) {
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
private fun ResearchCardPreview() {
    ResearchCard(
        research = Research(
            title = "Penerapan Deep Learning untuk Deteksi Dini Penyakit Paru-paru Berdasarkan Citra X-ray Menggunakan CNN",
            authors = "Dewi Kartika, S.Kom., M.Sc.; Andi Susanto, M.T.",
            link = "https://example.com/article1"
        ),
        onEditResearch = {},
        onDeleteResearch = {}
    )
}
