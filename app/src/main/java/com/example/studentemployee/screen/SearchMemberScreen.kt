package com.example.studentemployee.screen

import android.util.Log
import androidx.collection.emptyObjectList
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentemployee.viewmodel.SearchViewModel
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.sp
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.style.TextAlign
import com.example.studentemployee.components.ArticleCard
import com.example.studentemployee.components.BottomNavBarMember
import com.example.studentemployee.components.DevotionCard
import com.example.studentemployee.components.EventCard
import com.example.studentemployee.components.NewsCard
import com.example.studentemployee.data.Article
import com.example.studentemployee.data.Devotion
import com.example.studentemployee.data.Event
import com.example.studentemployee.data.Facilities
import com.example.studentemployee.data.HasTitle
import com.example.studentemployee.data.News
import com.google.firebase.firestore.FirebaseFirestore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchMemberScreen(
    firestore: FirebaseFirestore,
    navController: NavController,
    viewModel: SearchViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    val history by viewModel.history.collectAsState()
    val focusManager = LocalFocusManager.current
    var results by remember { mutableStateOf(emptyList<Any>()) }

    LaunchedEffect(Unit) {
        firestore.collection("events")
            .get()
            .addOnSuccessListener { result ->
                results += result.documents.mapNotNull { it.toObject(Event::class.java) }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error getting documents", it)
            }
        firestore.collection("news")
            .get()
            .addOnSuccessListener { result ->
                results += result.documents.mapNotNull { it.toObject(News::class.java) }
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
                            results += devotionsResult.documents.mapNotNull { it.toObject(Devotion::class.java) }
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Failed to get devotions for $userId", e)
                        }

                    firestore.collection("users")
                        .document(userId)
                        .collection("articles")
                        .get()
                        .addOnSuccessListener { result ->
                            results += result.documents.mapNotNull { it.toObject(Article::class.java) }
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

    var copyResults = results

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF19253F)
                )
            )
        },
        bottomBar = { BottomNavBarMember(navController) },
        containerColor = Color(0xFFF9F9F9)
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (searchQuery.isNotBlank()) {
                            viewModel.addSearchQuery(searchQuery)
                            copyResults = results.filter {
                                it is HasTitle && it.title.contains(searchQuery, ignoreCase = true)
                            }
                        } else {
                            copyResults = results
                        }

                        isFocused = false
                        focusManager.clearFocus()

                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        isFocused = it.isFocused
                    },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            searchQuery = ""
                            copyResults = results
                        }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (isFocused) {
                // Show search history
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextButton(onClick = { viewModel.clearHistory() }) {
                        Text("Clear History", color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                LazyColumn {
                    items(history) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    searchQuery = item.query
                                    isFocused = false
                                    focusManager.clearFocus()
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = item.query, modifier = Modifier.weight(1f))
                            IconButton(onClick = { viewModel.deleteSearchQuery(item) }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Delete")
                            }
                        }
                        Divider()
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Events",
                        color = Color(0xFFF37619),
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier.height(200.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (copyResults.filterIsInstance<Event>().isEmpty()) {
                            Text(
                                text = "Tidak Ada Event",
                                textAlign = TextAlign.Center
                            )
                        } else {
                            LazyColumn {
                                items(copyResults) { result ->
                                    if(result is Event)
                                        EventCard(event = result, false)
                                }
                            }
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
                    Text(
                        text = "News",
                        color = Color(0xFFF37619),
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier.height(200.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (copyResults.filterIsInstance<News>().isEmpty()) {
                            Text(
                                text = "Tidak Ada Berita",
                                textAlign = TextAlign.Center
                            )
                        } else {
                            LazyColumn {
                                items(copyResults) { result ->
                                    if(result is News)
                                        NewsCard(news = result, false)
                                }
                            }
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
                    Text(
                        text = "Devotions",
                        color = Color(0xFFF37619),
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier.height(200.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (copyResults.filterIsInstance<Devotion>().isEmpty()) {
                            Text(
                                text = "Tidak Ada Pengabdian",
                                textAlign = TextAlign.Center
                            )
                        } else {
                            LazyColumn {
                                items(copyResults) { result ->
                                    if(result is Devotion)
                                        DevotionCard(devotion = result)
                                }
                            }
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
                    Text(
                        text = "Articles",
                        color = Color(0xFFF37619),
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier.height(200.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (copyResults.filterIsInstance<Article>().isEmpty()) {
                            Text(
                                text = "Tidak Ada Artikel",
                                textAlign = TextAlign.Center
                            )
                        } else {
                            LazyColumn {
                                items(copyResults) { result ->
                                    if(result is Article)
                                        ArticleCard(article = result)
                                }
                            }
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
                }
            }
        }
    }
}

