package com.example.studentemployee.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.studentemployee.R
import com.example.studentemployee.components.DropDownProfile
import com.example.studentemployee.components.RoundedCard
import com.example.studentemployee.components.SectionHeader
import com.example.studentemployee.components.TopAppBarMenu
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileLabScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Profil Lab Sistem Informasi", color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF19253F)
                )
            )
        },
        containerColor = Color(0xFFF9F9F9)
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),

            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            CardVisi()
            CardMisi()
            CardTujuan()
            CardSasaran()
            CardLambang()
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileLabScreenPreview() {
    ProfileLabScreen(navController = rememberNavController())
}


@Composable
fun CardVisi(modifier: Modifier = Modifier) {
    RoundedCard {
        Column {
            SectionHeader(text = "Visi")
            Spacer(Modifier.height(10.dp))
            Text(
                buildAnnotatedString {
                    append("Menjadi laboratorium ")

                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append("unggul di bidang Sistem Informasi")
                    }

                    append(" yang ")

                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append("berdaya saing internasional")
                    }

                    append(" melalui ")

                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append("integrasi")
                    }

                    append(" pendidikan, penelitian, dan inovasi teknologi yang mendukung pengembangan industri dan masyarakat.")
                }, color = Color(0xff195693),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Preview
@Composable
private fun CardVisiPreview() {
    CardVisi()
}

@Composable
fun ListMisi() {
    val missions = listOf(
        "Pendidikan" to "Mendukung penyelenggaraan pendidikan di bidang Sistem Informasi yang berstandar internasional, berbasis teknologi terkini, serta berorientasi pada kebutuhan industri dan masyarakat.",
        "Penelitian dan Inovasi" to "Mendukung peningkatan kualitas penelitian yang relevan dan berkelanjutan di bidang Sistem Informasi untuk menghasilkan solusi teknologi tepat guna yang dapat diterapkan di berbagai sektor industri dan masyarakat.",
        "Pengabdian kepada Masyarakat" to "Mendukung pengembangan program-program pengabdian yang berbasis teknologi Sistem Informasi untuk memberdayakan masyarakat dan mendukung transformasi digital di tingkat lokal, nasional, dan internasional.",
        "Kerjasama Strategis" to "Mendukung perluasan kerjasama strategis dengan industri, lembaga pendidikan, dan institusi penelitian baik di dalam maupun luar negeri untuk mendukung pendidikan, penelitian, dan inovasi yang berkualitas.",
        "Pengembangan Tata Kelola" to "Mendukung perwujudan tata kelola laboratorium yang transparan, akuntabel, efektif, dan efisien untuk mendukung operasional laboratorium yang berdaya saing unggul."
    )

    Column() {
        missions.forEach { (title, description) ->
            DropDownProfile(title, description)
        }
    }
}

@Preview
@Composable
private fun ListMisiPreview() {
    ListMisi()
}

@Composable
fun CardMisi(modifier: Modifier = Modifier) {
    RoundedCard {
        Column() {
            SectionHeader(text = "Misi")
            Spacer(Modifier.height(10.dp))
            ListMisi()
        }
    }
}

@Preview
@Composable
private fun CardMisiPreview() {
    CardMisi()
}

@Composable
fun ListSasaran() {
    val sasaran = listOf(
        "Bidang Pendidikan" to """
        Mengembangkan kurikulum praktis berbasis proyek yang selaras dengan kebutuhan industri dan perkembangan teknologi terbaru.
        
        Menyediakan fasilitas pembelajaran berbasis teknologi yang mendukung pengembangan kompetensi mahasiswa.
        
        Menyediakan program pelatihan sertifikasi TIK bagi mahasiswa untuk meningkatkan kompetensi profesional mereka.
    """.trimIndent(),

        "Bidang Penelitian" to """
        Mendorong riset-riset inovatif di bidang Sistem Informasi yang memberikan dampak nyata bagi masyarakat dan industri.
        
        Menyediakan pendampingan dan sumber daya untuk meningkatkan kualitas publikasi ilmiah dosen dan mahasiswa.
        
        Mengembangkan kolaborasi riset antara akademisi, industri, dan pemerintah.
    """.trimIndent()
    )

    Column {
        sasaran.forEach { (title, description) ->
            DropDownProfile(title, description)
        }
    }
}

@Preview
@Composable
private fun ListSasaranPreview() {
    ListSasaran()
}

@Composable
fun CardSasaran(modifier: Modifier = Modifier) {
    RoundedCard {
        Column() {
            SectionHeader(text = "Sasaran")
            Spacer(Modifier.height(10.dp))
            ListSasaran()
        }
    }
}

@Preview
@Composable
private fun CardSasaranPreview() {
    CardSasaran()
}


@Composable
fun ListTujuan() {
    val tujuan = listOf(
        "Pengembangan Mahasiswa" to
                "Menghasilkan mahasiswa yang kompeten, kreatif, dan inovatif di bidang Sistem Informasi dengan jiwa entrepreneur dan daya saing internasional.",

        "Peningkatan Luaran Penelitian" to
                "Meningkatkan jumlah dan kualitas publikasi ilmiah serta luaran berupa perangkat lunak Sistem Informasi yang bermanfaat bagi masyarakat dan industri.",

        "Kolaborasi Nasional dan Internasional" to
                "Memperkuat kolaborasi nasional dan internasional di bidang Sistem Informasi untuk mendukung pengembangan teknologi dan inovasi.",

        "Penguatan Suasana Akademik" to
                "Mewujudkan suasana akademik yang kondusif untuk mendukung pengembangan pendidikan, penelitian, dan pengabdian kepada masyarakat di bidang Sistem Informasi.",

        "Peningkatan Sarana dan Prasarana" to
                "Meningkatkan sarana dan prasarana yang mendukung kegiatan penelitian dan pengembangan teknologi Sistem Informasi."
    )

    Column {
        tujuan.forEach { (title, description) ->
            DropDownProfile(title, description)
        }
    }
}

@Preview
@Composable
private fun ListTujuanPreview() {
    ListTujuan()
}

@Composable
fun CardTujuan(modifier: Modifier = Modifier) {
    RoundedCard {
        Column() {
            SectionHeader(text = "Tujuan")
            Spacer(Modifier.height(10.dp))
            ListTujuan()
        }
    }
}

@Preview
@Composable
private fun CardTujuanPreview() {
    CardTujuan()
}


@Composable
fun ListLambang() {
    val lambang = listOf(
        "Logo SI" to """
        Mencerminkan kesatuan dan keselarasan berbagai aspek sistem informasi.
        
        Pertemuan kedua gelombang menggambarkan keseimbangan antara aspek teknis dan kreatif.
    """.trimIndent(),

        "Gerigi" to """
        Melambangkan aspek teknis dan rekayasa dalam sistem informasi.
        
        Menggambarkan perkembangan teknologi yang terus berevolusi.
        
        Posisinya sebagai latar belakang menekankan bahwa teknologi adalah fondasi yang mendukung seluruh aktivitas laboratorium.
    """.trimIndent(),

        "Aliran Gelombang" to """
        Bentuk lengkung yang lebih ekspresif dan dinamis menggambarkan fleksibilitas dan adaptabilitas.
    """.trimIndent(),

        "Jaringan Graf" to """
        Tiga simpul (node) melambangkan prinsip dasar sistem informasi: data, proses, dan pengguna.
        
        Garis penghubung melambangkan integrasi dan aliran informasi yang terorganisir sekaligus melambangkan konektivitas data adalah fokus utama laboratorium.
    """.trimIndent(),

        "Tipografi \"LAB SISTEM INFORMASI\"" to """
        Jenis huruf serif klasik melambangkan tradisi akademis, ketepatan, dan keandalan.
        
        Warna biru menguatkan identitas profesional dan pendekatan sistematis laboratorium.
        
        Posisi berada di bagian bawah logo, memberikan fondasi dan penegasan identitas laboratorium.
        
        Tipografi ini memperkuat komitmen laboratorium terhadap keunggulan akademis dan profesionalisme dalam bidang sistem informasi.
    """.trimIndent()
    )

    Column {
        lambang.forEach { (title, description) ->
            DropDownProfile(title, description)
        }
    }
}

@Preview
@Composable
private fun ListLambangPreview() {
    ListLambang()
}

@Composable
fun CardLogo(modifier: Modifier = Modifier) {
    RoundedCard {
        Image(
            painterResource(id = R.drawable.logo_lab_si),
            contentDescription = "Logo Lab SI"
        )
    }
}

@Preview
@Composable
private fun CardLogoPreview() {
    CardLogo()
}

@Composable
fun CardLambang(modifier: Modifier = Modifier) {
    RoundedCard {
        Column() {
            SectionHeader(text = "Lambang")
            Image(
                painterResource(id = R.drawable.logo_lab_si),
                contentDescription = "Logo Lab SI"
            )
            Spacer(Modifier.height(10.dp))
            ListLambang()
        }
    }
}

@Preview
@Composable
private fun CardLambangPreview() {
    CardLambang()
}