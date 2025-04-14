package com.example.studentemployee.screen

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studentemployee.core.components.RoundedCard
import com.example.studentemployee.features.content.ContentViewModel

data class StatsItem(
    val label: String,
    val count: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navController: NavController,
    viewModel: ContentViewModel = viewModel()
) {
    val newsCount = viewModel.news.collectAsState().value.size
    val eventCount = viewModel.event.collectAsState().value.size
    val researchCount = viewModel.research.collectAsState().value.size
    val devotionCount = viewModel.devotion.collectAsState().value.size

    val stats = listOf(
        StatsItem("Berita", newsCount),
        StatsItem("Event", eventCount),
        StatsItem("Penelitian", researchCount),
        StatsItem("Pengabdian", devotionCount)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Statistik Lab SI", color = Color.White)
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
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),

            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            RoundedCard {
                Column {
                    Text(
                        "Statistik Konten Lab SI",
                        color = Color(0xffF37619),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(10.dp))
                    StatsBarChart(stats = stats)
                }
            }
            RoundedCard {
                Column {
                    Text(
                        "Statistik Anggota Lab SI",
                        color = Color(0xffF37619),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(10.dp))
                    MemberStatsCard(category = "Anggota Lab", stats = "19")
                }
            }
        }
    }
}

@Composable
fun StatsBarChart(stats: List<StatsItem>) {
    val maxCount = stats.maxOfOrNull { it.count } ?: 1

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)) // Clip agar background mengikuti shape
            .background(Color.White) // Background mengikuti clip
    ) {
        stats.forEach { item ->
            Text(
                text = "${item.label} (${item.count})",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xff195693)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(item.count / maxCount.toFloat())
                    .height(18.dp)
                    .background(
                        Color(0xFFE2640D),
                        shape = RoundedCornerShape(8.dp)
                    )
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

}

@Preview
@Composable
private fun StatsBarChartPreview() {
    val stats = listOf(
        StatsItem("Berita", 5),
        StatsItem("Event", 12),
        StatsItem("Penelitian", 20),
        StatsItem("Pengabdian", 17)
    )
    StatsBarChart(stats = stats)
}

@Composable
fun MemberStatsCard(
    modifier: Modifier = Modifier,
    category: String,
    stats: String
) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            category,
            color = Color(0xff195693),
            fontSize = 18.sp,
        )
        Text(
            stats, color = Color(0xff195693),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }

}

@Preview
@Composable
fun MemberStatsCardPreview(modifier: Modifier = Modifier) {
    MemberStatsCard(category = "Anggota Lab", stats = "19")
}