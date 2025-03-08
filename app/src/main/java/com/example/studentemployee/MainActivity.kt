package com.example.studentemployee

import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.text.style.TextAlign
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.example.studentemployee.ui.theme.StudentEmployeeTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.layout.ContentScale
import android.net.Uri
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

class MainActivity : ComponentActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    //    private lateinit var imageDao: ImageDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
//        val db = Room.databaseBuilder(
//            applicationContext,
//            AppDatabase::class.java, "travelupa-database"
//        ).build()
//        imageDao = db.imageDao()
        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        setContent {
            StudentEmployeeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    AppNavigation(currentUser, firestore, storage)
                }
            }
        }
    }
}
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object HomepageGuest : Screen("homepageguest")
    object ProfileLab : Screen("profilelab")
    object Leadership : Screen("leadership")
    object Member : Screen("member")
    object Facilities : Screen("facilities")
    object Statistics : Screen("statistics")
    object Events : Screen("events")
    object News : Screen("news")
    object Articles : Screen("articles")
    object Journals : Screen("journals")
    object Konten : Screen("konten")
}

data class Event(
    val title: String = "",
    val date: String = "",
    val time: String = "",
    val imageUrl: String = "",
    val link: String = ""
)

data class News(
    val title: String = "",
    val date: String = "",
    val imageUrl: String = "",
    val link: String = ""
)

data class Article(
    val title: String = "",
    val authors: String = "",
    val link: String = ""
)


@Composable
fun AppNavigation(
    currentUser: FirebaseUser?,
    firestore: FirebaseFirestore,
    storage: FirebaseStorage
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.HomepageGuest.route

//        if (currentUser != null)
//            Screen.HomepageGuest.route
//        else
//            Screen.HomepageAdmin.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.HomepageGuest.route) {
                        popUpTo(Screen.Login.route) {inclusive = true}
                    }
                }
            )
        }

        composable(Screen.Konten.route) {
            KontenScreen()
        }

        composable(Screen.ProfileLab.route) {
            ProfileLabScreen()
        }

        composable(Screen.Leadership.route) {
            LeadershipScreen()
        }

        composable(Screen.Member.route) {
            MemberScreen()
        }

        composable(Screen.Facilities.route) {
            FacilitiesScreen()
        }

        composable(Screen.Statistics.route) {
            StatisticsScreen()
        }

        composable(Screen.Events.route) {
            EventsScreen()
        }

        composable(Screen.News.route) {
            NewsScreen()
        }

        composable(Screen.Articles.route) {
            ArticlesScreen()
        }

        composable(Screen.Journals.route) {
            JournalsScreen()
        }

        composable(Screen.HomepageGuest.route) {
            HomepageGuestScreen(
                firestore = firestore,
                onClickLogin = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.HomepageGuest.route) { inclusive = true }
                    }
                },
                onClickProfile = {
                    navController.navigate(Screen.ProfileLab.route) {
                        popUpTo(Screen.HomepageGuest.route) { inclusive = true }
                    }
                },
                onClickLeadership = {
                    navController.navigate(Screen.Leadership.route) {
                        popUpTo(Screen.HomepageGuest.route) { inclusive = true }
                    }
                },
                onClickMember = {
                    navController.navigate(Screen.Member.route) {
                        popUpTo(Screen.HomepageGuest.route) { inclusive = true }
                    }
                },
                onClickFacilities = {
                    navController.navigate(Screen.Facilities.route) {
                        popUpTo(Screen.HomepageGuest.route) { inclusive = true }
                    }
                },
                onClickStatistics = {
                    navController.navigate(Screen.Statistics.route) {
                        popUpTo(Screen.HomepageGuest.route) { inclusive = true }
                    }
                },
                onClickEvents = {
                    navController.navigate(Screen.Events.route) {
                        popUpTo(Screen.HomepageGuest.route) { inclusive = true }
                    }
                },
                onClickNews = {
                    navController.navigate(Screen.News.route) {
                        popUpTo(Screen.HomepageGuest.route) { inclusive = true }
                    }
                },
                onClickArticles = {
                    navController.navigate(Screen.Articles.route) {
                        popUpTo(Screen.HomepageGuest.route) { inclusive = true }
                    }
                },
                onClickJournals = {
                    navController.navigate(Screen.Journals.route) {
                        popUpTo(Screen.HomepageGuest.route) { inclusive = true }
                    }
                },
                navController = navController
            )
        }

    }
}

@Composable
fun ProfileLabScreen(

) {
    Text (text = "Profile lab")
}

@Composable
fun LeadershipScreen(

) {
    Text (text = "Leadership")
}

@Composable
fun MemberScreen(

) {
    Text (text = "Member")
}

@Composable
fun FacilitiesScreen(

) {
    Text (text = "Facilities")
}

@Composable
fun StatisticsScreen(

) {
    Text (text = "Statistics")
}

@Composable
fun EventsScreen(

) {
    Text (text = "Events")
}

@Composable
fun NewsScreen(

) {
    Text (text = "News")
}

@Composable
fun ArticlesScreen(

) {
    Text (text = "Articles")
}

@Composable
fun JournalsScreen(

) {
    Text (text = "Journals")
}

@Composable
fun KontenScreen(

) {
    Text (text = "Konten")
}


@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = {
                email = it
                errorMessage = null
            },
            label = {Text("Email")},
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            label = {Text("Password")},
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else
                PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Outlined.VisibilityOff else
                            Icons.Outlined.Visibility,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if(email.isBlank() || password.isBlank()){
                    errorMessage = "Please enter email and password"
                    return@Button
                }
                isLoading = true
                errorMessage = null
                coroutineScope.launch {
                    try {
                        val authResult = withContext(Dispatchers.IO) {
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).await()
                        }
                        isLoading = false
                        onLoginSuccess()
                    } catch (e: Exception) {
                        isLoading = false
                        errorMessage = "Login failed: ${e.localizedMessage}"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if(isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White
                )
            } else {
                Text("Login")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

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
                events = result.documents.mapNotNull { it.toObject(Event::class.java)}
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error getting documents", it)
            }
        firestore.collection("news")
            .get()
            .addOnSuccessListener { result ->
                news = result.documents.mapNotNull { it.toObject(News::class.java)}
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error getting documents", it)
            }
        firestore.collection("articles")
            .get()
            .addOnSuccessListener { result ->
                articles = result.documents.mapNotNull { it.toObject(Article::class.java)}
            }
            .addOnFailureListener{
                Log.e("Firestore", "Error getting documents", it)
            }
        firestore.collection("journals")
            .get()
            .addOnSuccessListener { result ->
                journals = result.documents.mapNotNull { it.toObject(Article::class.java)}
            }
            .addOnFailureListener{
                Log.e("Firestore", "Error getting documents", it)
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(
                            onClick = { /* */ },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                        Column (
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.weight(1f)
                        ){
                            Text(
                                text = "Halo, Pengunjung",
                                color = Color.White,
                                style = MaterialTheme.typography.titleSmall
                            )

                            Row (
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                TextButton(onClick = { onClickLogin()},
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(
                                        text = "Masuk",
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                                Text (
                                    text = "untuk menggunakan menu lain",
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF195693)
                )
            )
        },
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment =  Alignment.Start
        ){
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF195693))
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    FeatureItem(Icons.Default.Book, "Profil Lab", onClickProfile)
                    Spacer(modifier = Modifier.width(8.dp))
                    FeatureItem(Icons.Default.SupervisorAccount, "Pimpinan", onClickLeadership)
                    Spacer(modifier = Modifier.width(8.dp))
                    FeatureItem(Icons.Default.Groups2, "Anggota", onClickMember)
                    Spacer(modifier = Modifier.width(8.dp))
                    FeatureItem(Icons.Default.HomeRepairService, "Fasilitas", onClickFacilities)
                    Spacer(modifier = Modifier.width(8.dp))
                    FeatureItem(Icons.Default.BarChart, "Statistik", onClickStatistics)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text (
                    style = MaterialTheme.typography.titleMedium,
                    text = "Event saat ini",
                    color = Color(0xFFf37619)
                )

                Button(
                    onClick = { onClickEvents() },
                    modifier = Modifier
                        .size(width = 120.dp, height = 32.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF048dc8))
                ){
                    Text(
                        text = "Lihat Semua",
                        style = MaterialTheme.typography.bodySmall
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

            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text (
                    style = MaterialTheme.typography.titleMedium,
                    text = "Berita seputar Lab",
                    color = Color(0xFFf37619)
                )

                Button(
                    onClick = { onClickNews() },
                    modifier = Modifier
                        .size(width = 120.dp, height = 32.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF048dc8))
                ){
                    Text(
                        text = "Lihat Semua",
                        style = MaterialTheme.typography.bodySmall
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

            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text (
                    style = MaterialTheme.typography.titleMedium,
                    text = "Artikel",
                    color = Color(0xFFf37619)
                )

                Button(
                    onClick = { onClickArticles() },
                    modifier = Modifier
                        .size(width = 120.dp, height = 32.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF048dc8))
                ){
                    Text(
                        text = "Lihat Semua",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow {
                items(articles) { articles ->
                    ArticleCard(articles)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text (
                    style = MaterialTheme.typography.titleMedium,
                    text = "Jurnal",
                    color = Color(0xFFf37619)
                )

                Button(
                    onClick = { onClickJournals() },
                    modifier = Modifier
                        .size(width = 120.dp, height = 32.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF048dc8))
                ){
                    Text(
                        text = "Lihat Semua",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow {
                items(journals) { journals ->
                    ArticleCard(journals)
                }
            }
        }

    }
}

@Composable
fun FeatureItem(
    icon: ImageVector,
    title: String,
    onClickButton : () -> Unit
) {
    Card(
        modifier = Modifier.size(54.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF048dc8)),
        onClick = { onClickButton() }
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(imageVector = icon, contentDescription = title, tint = Color.White)
            Text(text = title, color = Color.White, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun EventCard(
    event: Event
){
    val context = LocalContext.current
    ElevatedCard(
        modifier = Modifier
            .width(180.dp)
            .height(220.dp)
            .padding(8.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                context.startActivity(intent)
                       },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = "Event Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp), // Image size
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = event.title, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = event.date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Text(text = event.time, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

@Composable
fun NewsCard(
    news: News
){
    val context = LocalContext.current
    ElevatedCard(
        modifier = Modifier
            .width(180.dp)
            .height(200.dp)
            .padding(8.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.link))
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            AsyncImage(
                model = news.imageUrl,
                contentDescription = "Event Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp), // Image size
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = news.title, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = news.date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

@Composable
fun ArticleCard(
    article: Article
){
    val context = LocalContext.current
    ElevatedCard(
        modifier = Modifier
            .width(250.dp)
            .padding(8.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.link))
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = article.title,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = article.authors,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, "homepageguest"),
        BottomNavItem("Konten", Icons.AutoMirrored.Filled.MenuBook, "konten")
    )

    NavigationBar(containerColor = Color(0xFF195693)) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) }
            )
        }
    }
}
