package com.example.studentemployee.screen

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LockPerson
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.studentemployee.R
import com.example.studentemployee.components.BottomNavBarMember
import com.example.studentemployee.components.RoundedCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuMemberScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
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
                            text = "Menu",
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall,
                            fontSize = 16.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF19253F)
                )
            )
        },
        bottomBar = { BottomNavBarMember(navController) },
        contentColor = Color(0xFFF9F9F9)
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            CardMenu(
                text = "Tambahkan Penelitian",
                icon = Icons.Default.Biotech,
                onClick = {}
            )
            CardMenu(
                text = "Tambahkan Pengabdian",
                icon = Icons.Default.Handshake,
                onClick = {}
            )
            CardMenu(
                text = "Tambahkan Pengajaran",
                icon = Icons.Default.MenuBook,
                onClick = {}
            )
            CardMenu(
                text = "Ganti Password",
                icon = Icons.Default.LockPerson,
                onClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun MenuMemberScreenPreview() {
    MenuMemberScreen(navController = rememberNavController())
}

@Composable
fun CardMenu(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .shadow(8.dp, shape = RoundedCornerShape(8.dp)) // Shadow tetap ada
            .clip(RoundedCornerShape(8.dp)) // Clip agar background mengikuti shape
            .background(Color.White) // Background mengikuti clip
            .padding(10.dp) // Tambahkan padding agar konten tidak menempel

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = icon,
                    contentDescription = "Icon $text",
                    tint = Color(0xff093376)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xff093376)
                )
            }
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "Icon Arrow Right",
                tint = Color(0xff093376)
            )

        }
    }
}

@Preview
@Composable
private fun CardMenuPreview() {
    CardMenu(
        text = "Tambahkan Penelitian",
        icon = Icons.Default.Biotech,
        onClick = {}
    )
}