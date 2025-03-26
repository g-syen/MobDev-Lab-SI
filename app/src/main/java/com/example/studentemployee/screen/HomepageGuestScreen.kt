package com.example.studentemployee.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Groups2
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.studentemployee.data.Article
import com.example.studentemployee.components.ArticleCard
import com.example.studentemployee.data.Event
import com.example.studentemployee.components.EventCard
import com.example.studentemployee.components.FeatureItem
import com.example.studentemployee.data.News
import com.example.studentemployee.components.NewsCard
import com.example.studentemployee.components.BottomNavBar
import com.example.studentemployee.data.BottomNavItem
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomepageGuestScreen(
    firestore: FirebaseFirestore,
    onClickLogin: () -> Unit,
    onClickProfile: () -> Unit,
    onClickLeadership: () -> Unit,
    onClickMember: () -> Unit,
    onClickFacilities: () -> Unit,
    onClickStatistics: () -> Unit,
    onClickEvents: () -> Unit,
    onClickNews: () -> Unit,
    onClickArticles: () -> Unit,
    onClickJournals: () -> Unit,
    navController: NavController
) {
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    var news by remember { mutableStateOf<List<News>>(emptyList()) }
    var articles by remember { mutableStateOf<List<Article>>(emptyList()) }
    var journals by remember { mutableStateOf<List<Article>>(emptyList()) }

    LaunchedEffect(Unit) {
        firestore.collection("events")
            .get()
            .addOnSuccessListener { result ->
                events =
                    result.documents.mapNotNull { it.toObject(Event::class.java) }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error getting documents", it)
            }
        firestore.collection("news")
            .get()
            .addOnSuccessListener { result ->
                news =
                    result.documents.mapNotNull { it.toObject(News::class.java) }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error getting documents", it)
            }
        firestore.collection("articles")
            .get()
            .addOnSuccessListener { result ->
                articles =
                    result.documents.mapNotNull { it.toObject(Article::class.java) }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error getting documents", it)
            }
        firestore.collection("journals")
            .get()
            .addOnSuccessListener { result ->
                journals =
                    result.documents.mapNotNull { it.toObject(Article::class.java) }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error getting documents", it)
            }

    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(
                            onClick = { onClickLogin() },
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .width(56.dp)
                                .height(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Login,
                                contentDescription = "Login",
                                tint = Color(0xFF19253F)
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.weight(1f)
                        ) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Halo, Pengunjung",
                                color = Color.White,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = "Masuk",
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier
                                        .padding(0.dp)
                                        .clickable { onClickLogin() },
                                    color = Color(0xFFF37619),
                                )
                                Spacer(Modifier.width(5.dp))

                                Text(
                                    text = "untuk menggunakan menu lain",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.White
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF19253F)
                )
            )
        },
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color(0xFFF9F9F9)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF10375E))
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    FeatureItem(
                        Icons.Default.Book,
                        "Profil Lab",
                        onClickProfile
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FeatureItem(
                        Icons.Default.SupervisorAccount,
                        "Pimpinan",
                        onClickLeadership
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FeatureItem(Icons.Default.Groups2, "Anggota", onClickMember)
                    Spacer(modifier = Modifier.width(8.dp))
                    FeatureItem(
                        Icons.Default.HomeRepairService,
                        "Fasilitas",
                        onClickFacilities
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FeatureItem(
                        Icons.Default.BarChart,
                        "Statistik",
                        onClickStatistics
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = "Event saat ini",
                    color = Color(0xFFf37619)
                )

                Button(
                    onClick = { onClickEvents() },
                    modifier = Modifier
                        .size(width = 120.dp, height = 32.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(
                            0xFF10375E
                        )
                    )
                ) {
                    Text(
                        text = "Lihat Semua",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow {
                items(events) { event ->
                    EventCard(event)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = "Berita seputar Lab",
                    color = Color(0xFFf37619)
                )

                Button(
                    onClick = { onClickNews() },
                    modifier = Modifier
                        .size(width = 120.dp, height = 32.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(
                            0xFF10375E
                        )
                    )
                ) {
                    Text(
                        text = "Lihat Semua",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow {
                items(news) { news ->
                    NewsCard(news)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = "Artikel",
                    color = Color(0xFFf37619)
                )

                Button(
                    onClick = { onClickArticles() },
                    modifier = Modifier
                        .size(width = 120.dp, height = 32.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(
                            0xFF10375E
                        )
                    )
                ) {
                    Text(
                        text = "Lihat Semua",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow {
                items(articles) { articles ->
                    ArticleCard(articles, modifier = Modifier.width(250.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = "Jurnal",
                    color = Color(0xFFf37619)
                )

                Button(
                    onClick = { onClickJournals() },
                    modifier = Modifier
                        .size(width = 120.dp, height = 32.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(
                            0xFF10375E
                        )
                    )
                ) {
                    Text(
                        text = "Lihat Semua",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow {
                items(journals) { journals ->
                    ArticleCard(journals, modifier = Modifier.width(250.dp))
                }
            }
        }

    }
}




