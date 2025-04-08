package com.example.studentemployee.screen

import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.studentemployee.components.BottomNavBarMember
import com.example.studentemployee.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.net.toUri
import com.example.studentemployee.Screen
import com.example.studentemployee.components.ArtikelContent
import com.example.studentemployee.components.PengabdianContent
import com.example.studentemployee.components.TabSection


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProfileViewModel = viewModel(),
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val socialLinks by viewModel.socialLinks.collectAsState()
    val articles by viewModel.articles.collectAsState()
    val devotions by viewModel.devotions.collectAsState()
    var selectedTab by remember { mutableStateOf("Artikel") }

    LaunchedEffect(Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            viewModel.loadUserProfile(userId)
            viewModel.loadUserContributions(userId = userId)
        }
    }

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
                                text = "Profil Saya",
                                color = Color.White,
                                style = MaterialTheme.typography.titleSmall,
                                fontSize = 16.sp
                            )
                        }
                        IconButton(onClick = { navController.navigate(Screen.EditProfile.route) }) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Icon Edit",
                                tint = Color.White
                            )
                        }
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                userProfile?.let {
                    NameSocialsCard(
                        name = it.nama,
                        nip = "198802022012100901",
                        socialLinks = socialLinks
                    )
                    ProfileCard(
                        title = "Research Interest",
                        text = it.specialist
                    )
                    ProfileCard(
                        title = "Biografi",
                        text = it.biography
                    )
                }
                CustomButton(onClick = {}, text = "Logout")
            }
//            ElevatedCard(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp),
//                elevation = CardDefaults.elevatedCardElevation(4.dp),
//                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
//            ) {
//                TabSection(selectedTab) { newTab ->
//                    selectedTab = newTab
//                }
//            }
//
//            when (selectedTab) {
//                "Artikel" -> ArtikelContent(articles)
//                "Pengabdian" -> PengabdianContent(devotions)
//            }
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}

@Composable
fun ProfileCard(modifier: Modifier = Modifier, title: String, text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, shape = RoundedCornerShape(8.dp)) // Shadow tetap ada
            .clip(RoundedCornerShape(8.dp)) // Clip agar background mengikuti shape
            .background(Color.White) // Background mengikuti clip
            .padding(
                horizontal = 24.dp,
                vertical = 15.dp
            ) // Tambahkan padding agar konten tidak menempel

    ) {
        Column {
            Text(
                title,
                color = Color(0XFFE2640D),
                fontSize = 14.sp
            )
            Spacer(Modifier.height(5.dp))
            Text(
                text,
                color = Color(0XFF195693),
                fontSize = 16.sp,
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Preview
@Composable
private fun ProfileCardPreview() {
    ProfileCard(title = "Research Interest", text = "Geoinformasi")
}

@Composable
fun CustomButton(
    modifier: Modifier = Modifier, onClick: () -> Unit, text: String
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(Color(0XFFE2640D)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

@Preview
@Composable
private fun ButtonLogoutPreview() {
    CustomButton(onClick = {}, text = "Logout")
}

@Composable
fun NameSocialsCard(
    modifier: Modifier = Modifier,
    name: String,
    nip: String,
    socialLinks: Map<String, String>
) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, shape = RoundedCornerShape(8.dp)) // Shadow tetap ada
            .clip(RoundedCornerShape(8.dp)) // Clip agar background mengikuti shape
            .background(Color.White) // Background mengikuti clip
            .padding(
                horizontal = 24.dp,
                vertical = 15.dp
            ) // Tambahkan padding agar konten tidak menempel

    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InitialsAvatar(name = name)
                Column {
                    Text(
                        name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0XFF195693)
                    )
                    Text(
                        nip,
                        fontSize = 16.sp,
                        color = Color(0XFFE2640D)
                    )
                }
            }

            Divider(
                color = Color(0xFF195693),
                thickness = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                socialLinks.forEach { (platform, link) ->
                    ButtonSocial(
                        onClick = {
                            val intent =
                                Intent(Intent.ACTION_VIEW, link.toUri())
                            context.startActivity(intent)
                        },
                        modifier = Modifier.weight(1f),
                        text = platform
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun NameSocialsCardPreview() {
    val socialLinks = mapOf(
        "Instagram" to "https://instagram.com/johndoe",
        "LinkedIn" to "https://linkedin.com/in/johndoe",
        "GitHub" to "https://github.com/johndoe",
        "YouTube" to "https://youtube.com/johndoe",
    )
    NameSocialsCard(
        name = "Dewi Kartika, S.Kom., M.Sc. ",
        nip = "198802022012100901",
        socialLinks = socialLinks
    )
}

@Composable
fun InitialsAvatar(modifier: Modifier = Modifier, name: String) {
    val initials = name
        .split(" ")
        .filter { it.isNotBlank() && it.length > 0 }
        .mapNotNull { it.firstOrNull()?.uppercaseChar()?.toString() }
        .take(2) // Ambil 2 huruf aja biar nggak panjang
        .joinToString("")

    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(Color(0XFFEAEAEA)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            initials,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0XFF195693)
        )
    }
}

@Preview
@Composable
private fun InitalsAvatarPreview() {
    InitialsAvatar(name = "Dewi Kartika")
}

@Composable
fun ButtonSocial(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(
                0xFF426193
            )
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .height(36.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview
@Composable
private fun ButtonSocialPreview() {
    ButtonSocial(onClick = {}, text = "Instagram")
}