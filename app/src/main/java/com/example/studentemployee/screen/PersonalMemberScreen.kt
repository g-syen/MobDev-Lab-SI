package com.example.studentemployee.screen

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.studentemployee.R
import com.example.studentemployee.components.ArtikelContent
import com.example.studentemployee.components.FacilitiesCard
import com.example.studentemployee.components.ImageCarousel
import com.example.studentemployee.components.PengabdianContent
import com.example.studentemployee.components.PublikasiContent
import com.example.studentemployee.components.TabSection
import com.example.studentemployee.data.Article
import com.example.studentemployee.data.Devotion
import com.example.studentemployee.data.Facilities
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalMemberScreen(
    navController: NavController,
    firestore: FirebaseFirestore,
    onClickBack: () -> Unit,
    personalId: String?
) {
    var name by remember { mutableStateOf("") }
    var profileImageUrl by remember { mutableStateOf("") }
    var specialist by remember { mutableStateOf("") }
    var articles by remember { mutableStateOf<List<Article>>(emptyList()) }
    var publications by remember { mutableStateOf<List<Article>>(emptyList()) }
    var devotions by remember { mutableStateOf<List<Devotion>>(emptyList()) }
    var biography by remember { mutableStateOf("") }
    var socialLinks by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var selectedTab by remember { mutableStateOf("Artikel") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (personalId != null) {
            val userRef = firestore.collection("users").document(personalId)

            userRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        name = document.getString("nama").orEmpty()
                        profileImageUrl = document.getString("profileImageUrl").orEmpty()
                        specialist = document.getString("specialist").orEmpty()
                        biography = document.getString("biography").orEmpty()
                        Log.d("Firestore", "User name: $name")
                        Log.d("Firestore", "Profile Image: $profileImageUrl")
                        Log.d("Firestore", "Specialist: $specialist")
                        Log.d("Firestore", "Biography: $biography")
                    }
                }
                .addOnFailureListener {
                    Log.e("Firestore", "Error getting user document", it)
                }

            userRef.collection("socialmedia").get()
                .addOnSuccessListener { querySnapshot ->
                    val linksMap = mutableMapOf<String, String>()
                    for (doc in querySnapshot) {
                        val platform = doc.id
                        val link = doc.getString("link")
                        if (!link.isNullOrEmpty()) {
                            linksMap[platform] = link
                        }
                    }
                    socialLinks = linksMap
                    Log.d("Firestore", "Social links: $socialLinks")
                }
                .addOnFailureListener {
                    Log.e("Firestore", "Error getting social media collection", it)
                }

            userRef.collection("articles")
                .get()
                .addOnSuccessListener { result ->
                    val newArticles = result.documents.mapNotNull { it.toObject(Article::class.java) }
                    articles = newArticles
                }
                .addOnFailureListener{
                    Log.e("Firestore", "Error getting documents", it)
                }

            userRef.collection("publications")
                .get()
                .addOnSuccessListener { result ->
                    val newPublications = result.documents.mapNotNull { it.toObject(Article::class.java) }
                    publications = newPublications
                }
                .addOnFailureListener{
                    Log.e("Firestore", "Error getting documents", it)
                }

            userRef.collection("devotions")
                .get()
                .addOnSuccessListener { result ->
                    val newDevotion = result.documents.mapNotNull { it.toObject(Devotion::class.java) }
                    devotions = newDevotion
                }
                .addOnFailureListener{
                    Log.e("Firestore", "Error getting documents", it)
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(name,
                    color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { onClickBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF19253F)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(1f)
            )
        },
        containerColor = Color(0xFFF9F9F9)
    ) { innerPadding ->
        Column (modifier = Modifier
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment =  Alignment.Start,
        )   {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.elevatedCardElevation(4.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = profileImageUrl.takeIf { it.isNotEmpty() } ?: R.drawable.default_pfp,
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF195693)
                            )
                            Text(
                                text = specialist,
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )
                        }
                    }

                    Divider(
                        color = Color(0xFF195693),
                        thickness = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .padding(vertical = 12.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.wrapContentWidth()
                        ) {
                            socialLinks.forEach { (platform, link) ->
                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(
                                            0xFF426193
                                        )
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(1f, false)
                                        .height(36.dp)
                                        .width(72.dp),
                                    contentPadding = PaddingValues(8.dp)
                                ) {
                                    Text(
                                        text = platform,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(400.dp),  // Set fixed height
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.elevatedCardElevation(4.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Biografi",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6A00)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = biography.takeIf { it.isNotEmpty() } ?: "-",
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                elevation = CardDefaults.elevatedCardElevation(4.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
            ) {
                TabSection(selectedTab) { newTab ->
                    selectedTab = newTab
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                "Artikel" -> ArtikelContent(articles)
                "Publikasi" -> PublikasiContent(publications)
                "Pengabdian" -> PengabdianContent(devotions)
            }


        }
    }
}

