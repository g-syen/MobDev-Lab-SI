package com.example.studentemployee.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.studentemployee.components.DropDownProfile
import com.example.studentemployee.components.RoundedCard
import com.example.studentemployee.components.SectionHeader
import com.example.studentemployee.components.TopAppBarMenu

@Composable
fun ProfileLabScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Scaffold(topBar = {
        TopAppBarMenu(
            onClick = { navController.navigateUp() },
            text = "Profil Lab Sistem Informasi"
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),

            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CardVisi()
            CardMisi()
            CardSejarah()
            CardLambang()
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
                "Menjadi laboratorium unggulan dalam pengembangan dan penerapan sistem informasi berbasis teknologi terkini untuk mendukung inovasi, penelitian, dan solusi digital yang berdaya saing global.",
                color = Color(0xff195693),
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
        "Meningkatkan Kolaborasi" to "Meningkatkan kolaborasi dengan akademisi, praktisi, dan mitra industri untuk memperkuat ekosistem riset pengembangan teknologi.",
        "Inovasi Berkelanjutan" to "Mengembangkan solusi inovatif berbasis teknologi informasi yang bermanfaat bagi masyarakat dan industri.",
        "Penguatan Kompetensi" to "Membantu mahasiswa dan peneliti dalam meningkatkan keterampilan dan wawasan di bidang sistem informasi."
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
fun ListSejarah() {
    val sejarah = listOf(
        "Didirikan" to "Lab Sistem Informasi didirikan pada tahun 2005 sebagai bagian dari Fakultas Ilmu Komputer untuk mendukung riset dan pengembangan di bidang sistem informasi.",
        "Pengembangan Awal" to "Pada tahun 2008, lab mulai mengembangkan berbagai penelitian terkait sistem informasi berbasis web dan teknologi enterprise.",
        "Ekspansi dan Kolaborasi" to "Sejak 2015, Lab Sistem Informasi aktif menjalin kerja sama dengan industri dan institusi akademik dalam berbagai proyek penelitian dan pengembangan.",
        "Transformasi Digital" to "Pada tahun 2020, lab fokus pada inovasi di bidang kecerdasan buatan, big data, dan transformasi digital untuk mendukung solusi berbasis teknologi.",
        "Pengembangan Terkini" to "Saat ini, lab terus berkembang dengan berbagai penelitian dan proyek yang berorientasi pada smart systems dan digital economy."
    )

    Column {
        sejarah.forEach { (title, description) ->
            DropDownProfile(title, description)
        }
    }
}

@Preview
@Composable
private fun ListSejarahPreview() {
    ListSejarah()
}

@Composable
fun CardSejarah(modifier: Modifier = Modifier) {
    RoundedCard {
        Column() {
            SectionHeader(text = "Sejarah")
            Spacer(Modifier.height(10.dp))
            ListSejarah()
        }
    }
}

@Preview
@Composable
private fun CardSejarahPreview() {
    CardSejarah()
}

@Composable
fun ListLambang() {
    val lambang = listOf(
        "Logo Lab SI" to "Melambangkan integrasi teknologi dan informasi, dengan warna biru sebagai simbol kepercayaan dan inovasi.",
        "Garis Melengkung" to "Merepresentasikan fleksibilitas dan adaptasi terhadap perkembangan teknologi informasi yang dinamis.",
        "Ikon Digital" to "Menunjukkan fokus lab pada kecerdasan buatan, big data, dan transformasi digital.",
        "Warna Oranye" to "Melambangkan semangat, kreativitas, dan inovasi dalam pengembangan sistem informasi.",
        "Struktur Geometris" to "Menggambarkan kestabilan, keteraturan, dan pendekatan berbasis sains dalam setiap penelitian yang dilakukan di Lab SI."
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
fun CardLambang(modifier: Modifier = Modifier) {
    RoundedCard {
        Column() {
            SectionHeader(text = "Lambang")
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