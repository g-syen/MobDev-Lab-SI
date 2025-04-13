package com.example.studentemployee.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage
import com.example.studentemployee.components.BottomNavBar
import com.example.studentemployee.components.BottomNavBarMember
import com.example.studentemployee.data.Devotion
import com.example.studentemployee.data.Event
import com.example.studentemployee.data.News
import com.example.studentemployee.data.Research
import com.example.studentemployee.viewmodel.ContentViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentScreen(
    navController: NavController,
    viewModel: ContentViewModel = viewModel(),
) {
    val news by viewModel.news.collectAsState()
    val events by viewModel.event.collectAsState()
    val researches by viewModel.research.collectAsState()
    val devotions by viewModel.devotion.collectAsState()
    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null


    val dummyNews = listOf(
        News(
            title = "FILKOM Adakan Seminar Teknologi AI Terkini",
            date = "2025-04-01",
            imageUrl = "https://example.com/image1.jpg",
            link = "https://filkom.ub.ac.id/berita1"
        ),
        News(
            title = "Mahasiswa FILKOM Juara 1 Lomba Data Science Nasional",
            date = "2025-03-15",
            imageUrl = "https://example.com/image2.jpg",
            link = "https://filkom.ub.ac.id/berita2"
        )
    )

    val dummyEvents = listOf(
        Event(
            title = "Workshop UI/UX Design",
            date = "2025-04-10",
            time = "09:00 - 12:00",
            imageUrl = "https://example.com/event1.jpg",
            link = "https://filkom.ub.ac.id/event1"
        ),
        Event(
            title = "Pelatihan Android Jetpack Compose",
            date = "2025-04-20",
            time = "13:00 - 16:00",
            imageUrl = "https://example.com/event2.jpg",
            link = "https://filkom.ub.ac.id/event2"
        )
    )

    val dummyResearches = listOf(
        Research(
            id = "1",
            title = "Implementasi Machine Learning untuk Deteksi Penyakit",
            authors = "Dr. Siti Aisyah, M.Kom",
            link = "https://filkom.ub.ac.id/research1"
        ),
        Research(
            id = "2",
            title = "Analisis Big Data untuk Prediksi Kebutuhan Pangan",
            authors = "Prof. Budi Santoso, Ph.D",
            link = "https://filkom.ub.ac.id/research2"
        )
    )

    val dummyDevotions = listOf(
        Devotion(
            id = "1",
            title = "Pelatihan TIK untuk Guru di Malang",
            link = "https://filkom.ub.ac.id/devotion1",
            contributors = "Dewi Kartika, Rina Marlina"
        ),
        Devotion(
            id = "2",
            title = "Pengenalan Coding untuk Anak-Anak Desa",
            link = "https://filkom.ub.ac.id/devotion2",
            contributors = "Andi Wijaya, Siti Nurhaliza"
        )
    )

    val tabs = listOf("Berita", "Event", "Penelitian", "Pengabdian")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { },
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .width(56.dp)
                                    .height(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Logout,
                                    contentDescription = "Logout",
                                    tint = Color(0xFF19253F)
                                )
                            }
                            Text(
                                text = "Konten Lab Sistem Informasi",
                                color = Color.White,
                                style = MaterialTheme.typography.titleSmall,
                                fontSize = 16.sp
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF19253F)
                )
            )
        },
        bottomBar = {
            if (isLoggedIn) {
                BottomNavBarMember(navController)
            }else{
                BottomNavBar(navController = navController)
            }
        },
        contentColor = Color(0xFFF9F9F9)
    ) { innerPadding ->
        val scope = rememberCoroutineScope()
        val pagerState = rememberPagerState(pageCount = { tabs.size })

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = Color(0xFFE2640D)
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        },
                        modifier = Modifier.background(Color(0xFF19253F)),
                        text = {
                            Text(
                                title,
                                fontSize = 12.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> NewsList(news)
                    1 -> EventList(events)
                    2 -> ResearchList(researches)
                    3 -> DevotionList(devotions)
                }
            }
        }
    }
}

@Preview
@Composable
private fun ContentScreenPreview() {
    ContentScreen(navController = rememberNavController())
}

@Composable
fun NewsList(news: List<News>) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(news) { item ->
            NewsCardHorizontal(news = item)
        }
    }
}

@Composable
fun EventList(events: List<Event>) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    )  {
        items(events) { item ->
            EventCardHorizontal(event = item)
        }
    }
}

@Composable
fun ResearchList(researches: List<Research>) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    )  {
        items(researches) { item ->
            ResearchCardHorizontal(research = item)
        }
    }
}

@Composable
fun DevotionList(devotions: List<Devotion>) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    )  {
        items(devotions) { item ->
            DevotionCardHorizontal(devotion = item)
        }
    }
}

@Composable
fun DevotionCardHorizontal(modifier: Modifier = Modifier, devotion: Devotion) {
    val context = LocalContext.current
    ElevatedCard(
        modifier = modifier
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
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xff195693)
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
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Expand",
                modifier = Modifier.size(24.dp),
                tint = Color(0xff195693)
            )
        }
    }
}

@Preview
@Composable
private fun DevotionCardHorizontalPreview() {
    DevotionCardHorizontal(
        devotion = Devotion(
            id = "1",
            title = "2023. Pelatihan Penguatan Layanan Pendidikan di POS PAUD Kasih Sayang Kota Malang",
            link = "https://filkom.ub.ac.id/devotion1",
            contributors = "Intan Sartika Eris Maghfiroh, Bondan Sapta Prakoso, Almira Syawli, Riswan Septriayadi Sianturi, RACANA AYU KAEKSI, FADHILAH ALYA PRASTINANDA"
        ),
    )
}

@Composable
fun ResearchCardHorizontal(modifier: Modifier = Modifier, research: Research) {
    val context = LocalContext.current
    ElevatedCard(
        modifier = modifier
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
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xff195693)
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
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Expand",
                modifier = Modifier.size(24.dp),
                tint = Color(0xff195693)
            )
        }
    }
}

@Preview
@Composable
private fun ResearchCardHorizontalPreview() {
    ResearchCardHorizontal(
        research = Research(
            id = "1",
            title = "Implementasi Sistem Informasi Berbasis Web untuk Optimalisasi Manajemen Data ",
            authors = "Ahmad Ramadhan, Siti Nur Aisyah, Budi Santoso",
            link = "https://filkom.ub.ac.id/research1"
        ),
    )
}

@Composable
fun NewsCardHorizontal(modifier: Modifier = Modifier, news: News) {
    val context = LocalContext.current
    ElevatedCard(
        modifier = modifier
            .clickable {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(news.link))
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = news.imageUrl,
                contentDescription = "News Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(bottomStart = 8.dp, topStart = 8.dp)), // Image size
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = news.title,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xff195693)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = news.date,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Expand",
                modifier = Modifier.size(24.dp),
                tint = Color(0xff195693)
            )
            Spacer(Modifier.width(8.dp))
        }
    }
}

@Preview
@Composable
private fun NewsCardHorizontalPreview() {
    NewsCardHorizontal(
        news = News(
            title = "Mahasiswa Lab SI FILKOM UB Kembangkan Aplikasi Berbasis AI untuk Meningkatkan Efisiensi Mahasiswa Lab SI FILKOM UB Kembangkan Aplikasi Berbasis AI untuk Meningkatkan Efisiensi    ",
            date = "2025-04-01",
            imageUrl = "https://example.com/image1.jpg",
            link = "https://filkom.ub.ac.id/berita1"
        ),
    )
}

@Composable
fun EventCardHorizontal(modifier: Modifier = Modifier, event: Event) {
    val context = LocalContext.current
    ElevatedCard(
        modifier = modifier
            .clickable {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = "News Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(bottomStart = 8.dp, topStart = 8.dp)), // Image size
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = "${event.date} | ${event.time}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    color = Color(0xffE2640D),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = event.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xff195693)
                )
                Spacer(modifier = Modifier.height(5.dp))

            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Expand",
                modifier = Modifier.size(24.dp),
                tint = Color(0xff195693)
            )
            Spacer(Modifier.width(8.dp))
        }
    }
}

@Preview
@Composable
private fun EventCardHorizontalPreview() {
    EventCardHorizontal(
        event = Event(
            title = "Lab Sistem Informasi FILKOM UB Gelar Workshop Pengembangan Keamanan Dijital",
            date = "2025-04-10",
            time = "09:00 - 12:00",
            imageUrl = "https://example.com/event1.jpg",
            link = "https://filkom.ub.ac.id/event1"
        )
    )
}