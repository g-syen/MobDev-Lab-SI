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
import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.DpOffset
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
    object Homepage : Screen("homepage")
}

@Composable
fun AppNavigation(
    currentUser: FirebaseUser?,
    firestore: FirebaseFirestore,
    storage: FirebaseStorage
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route

//        if (currentUser != null)
//            Screen.HomepageGuest.route
//        else
//            Screen.HomepageAdmin.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Homepage.route) {
                        popUpTo(Screen.Login.route) {inclusive = true}
                    }
                }
            )
        }
        composable(Screen.Homepage.route) {
            HomepageScreen(
                firestore = firestore,
                onBackToLogin = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Homepage.route) { inclusive = true }
                    }
                }
            )
        }

    }
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

@Composable
fun HomepageScreen(
    firestore: FirebaseFirestore,
    onBackToLogin: () -> Unit
) {
    Text(
        text = "Welcome",
        color = Color.Black,
        modifier = Modifier.padding(top = 8.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = typography.titleSmall
    )
}
