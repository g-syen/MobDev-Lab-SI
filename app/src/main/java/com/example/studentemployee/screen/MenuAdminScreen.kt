package com.example.studentemployee.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.studentemployee.components.BottomNavBarAdmin
import com.example.studentemployee.components.CardMenu
import com.example.studentemployee.components.LogoutConfirmationDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuAdminScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onClickAddEvent: () -> Unit,
    onClickAddNews: () -> Unit,
    onClickLogin: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

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
                            onClick = { showLogoutDialog = true },
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
        bottomBar = { BottomNavBarAdmin(navController) },
        containerColor = Color(0xFFF9F9F9)
    ) { innerPadding ->
        if (showLogoutDialog) {
            LogoutConfirmationDialog(
                onConfirm = onClickLogin,
                onDismiss = { showLogoutDialog = false }
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical =10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            CardMenu(
                text = "Tambahkan Event",
                icon = Icons.Default.CalendarToday,
                onClick = onClickAddEvent
            )
            CardMenu(
                text = "Tambahkan Berita",
                icon = Icons.Default.Newspaper,
                onClick = onClickAddNews
            )
        }
    }
}

@Preview
@Composable
private fun MenuAdminScreenPreview() {
    MenuAdminScreen(navController = rememberNavController(), onClickAddEvent = {}, onClickAddNews = {}, onClickLogin = {})
}
