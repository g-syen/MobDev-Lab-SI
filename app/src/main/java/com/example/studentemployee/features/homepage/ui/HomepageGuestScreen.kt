package com.example.studentemployee.features.homepage.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Groups2
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentemployee.features.research.model.Research
import com.example.studentemployee.core.components.ResearchSmallCard
import com.example.studentemployee.features.events.model.Event
import com.example.studentemployee.core.components.EventCard
import com.example.studentemployee.core.components.FeatureItem
import com.example.studentemployee.features.news.model.News
import com.example.studentemployee.core.components.NewsCard
import com.example.studentemployee.core.components.BottomNavBar
import com.example.studentemployee.core.components.DevotionCard
import com.example.studentemployee.features.devotion.model.Devotion
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
    onClickResearch: () -> Unit,
    onClickDevotion: () -> Unit,
    navController: NavController
) {
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    var news by remember { mutableStateOf<List<News>>(emptyList()) }
    var research by remember { mutableStateOf<List<Research>>(emptyList()) }
    var devotion by remember { mutableStateOf<List<Devotion>>(emptyList()) }

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
        firestore.collection("users")
            .get()
            .addOnSuccessListener { userResult ->
                userResult.forEach { userDoc ->
                    val userId = userDoc.id

                    firestore.collection("users")
                        .document(userId)
                        .collection("devotions")
                        .get()
                        .addOnSuccessListener { devotionsResult ->
                            devotion += devotionsResult.documents.mapNotNull { it.toObject(Devotion::class.java) }
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Failed to get devotions for $userId", e)
                        }

                    firestore.collection("users")
                        .document(userId)
                        .collection("researches")
                        .get()
                        .addOnSuccessListener { result ->
                            research += result.documents.mapNotNull { it.toObject(Research::class.java) }
                        }
                        .addOnFailureListener {
                            Log.e("Firestore", "Error getting documents", it)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to get users", e)
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
                    EventCard(event, true)
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
                    NewsCard(news, true)
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
                    text = "Penelitian",
                    color = Color(0xFFf37619)
                )

                Button(
                    onClick = { onClickResearch() },
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
                items(research) { research ->
                    ResearchSmallCard(research, modifier = Modifier.width(250.dp))
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
                    text = "Pengabdian",
                    color = Color(0xFFf37619)
                )

                Button(
                    onClick = { onClickDevotion() },
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
                items(devotion) { devotion ->
                    DevotionCard(devotion, modifier = Modifier.width(250.dp))
                }
            }
        }

    }
}




