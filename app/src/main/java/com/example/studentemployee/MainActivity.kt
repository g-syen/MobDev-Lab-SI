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
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.example.studentemployee.ui.theme.StudentEmployeeTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.layout.ContentScale
import android.net.Uri
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import android.content.Intent
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.studentemployee.screen.*
import com.example.studentemployee.viewmodel.LeaderViewModel
import com.example.studentemployee.viewmodel.MemberViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage

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
    object HomepageMember : Screen("homepagemember")
    object UploadProfile : Screen("uploadprofile")

    object ProfileLab : Screen("profilelab")
    object Leadership : Screen("leadership")
    object Member : Screen("member")
    object Facilities : Screen("facilities")
    object Statistics : Screen("statistics")
    object Events : Screen("events")
    object News : Screen("news")
    object Articles : Screen("articles")
    object Journals : Screen("journals")
    object Content : Screen("content")
    object Profile : Screen("profile")
    object Menu : Screen("menu")
    object MenuMember : Screen("menumember")
    object PersonalMember : Screen("personalmember")
    object Search : Screen("search")
    object EditProfile:Screen("editprofile")

    object HomepageAdmin : Screen("homepageadmin")
    object FacilitiesAdmin : Screen("facilitiesadmin")
    object AddEditFacility : Screen("addeditfacility")
    object AddEditMember : Screen("addeditmember")
    object MemberAdmin : Screen("memberadmin")
    object AddEditLeader : Screen("addeditleader")
    object LeadershipAdmin : Screen("leadershipadmin")

}

@Composable
fun AppNavigation(
    currentUser: FirebaseUser?,
    firestore: FirebaseFirestore,
    storage: FirebaseStorage
) {
    val navController = rememberNavController()

    var startDestination by remember { mutableStateOf(Screen.HomepageGuest.route) }

    if (currentUser != null)
        firestore.collection("users").document(currentUser.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val role = document.getString("role")
                    Log.d("Firestore", "User role: $role")

                    startDestination = when (role) {
                        "admin" -> Screen.HomepageAdmin.route
                        "member" -> Screen.HomepageMember.route
                        else -> Screen.HomepageGuest.route
                    }
                }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error fetching role", it)
            }


    NavHost(
        navController = navController,
        startDestination = startDestination

    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { user ->
                    firestore.collection("users").document(user.uid)
                        .get()
                        .addOnSuccessListener { document ->
                            val role = document.getString("role") ?: "guest"
                            Log.d("Firestore", "User role: $role")

                            val destination = when (role) {
                                "admin" -> Screen.HomepageAdmin.route
                                "member" -> Screen.HomepageMember.route
                                else -> Screen.HomepageGuest.route
                            }

                            navController.navigate(destination) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                        .addOnFailureListener {
                            Log.e("Firestore", "Error fetching role", it)
                            navController.navigate(Screen.HomepageGuest.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                },
                onClickLoginAsGuest = {
                    navController.navigate(Screen.HomepageGuest.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Content.route) {
            ContentScreen()
        }

        composable(Screen.ProfileLab.route) {
            ProfileLabScreen(navController = navController)
        }

        composable(Screen.Leadership.route) {
            val viewModel: LeaderViewModel = viewModel()
            LeadershipScreen(
                navController = navController,
                firestore = firestore,
                viewModel = viewModel
            )
        }

        composable(Screen.Member.route) {
            val viewModel: MemberViewModel = viewModel()
            MemberScreen(
                navController = navController,
                firestore = firestore,
                viewModel = viewModel
            )
        }

        composable(Screen.Facilities.route) {
            FacilitiesScreen(
                navController = navController,
                firestore = firestore,
                onClickBack = {
                    navController.navigateUp()
                }
            )
        }

        composable(Screen.PersonalMember.route) {
            PersonalMemberScreen(
                navController = navController,
                firestore = firestore,
                onClickBack = {
                    navController.navigateUp()
                },
                personalId = null
            )
        }

        composable("personalmember/{personalId}") { backStackEntry ->
            val personalId = backStackEntry.arguments?.getString("personalId")
            personalId?.let {
                PersonalMemberScreen(
                    firestore = firestore,
                    navController = navController,
                    onClickBack = {
                        navController.navigateUp()
                    },
                    personalId = it
                )
            } ?: run {
                navController.navigateUp()
            }
        }

        composable(Screen.UploadProfile.route) {
            UploadProfileScreen()
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

        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }

        composable(Screen.EditProfile.route){
            EditProfileScreen(navController = navController)
        }

        composable(Screen.Menu.route) {
            MenuScreen()
        }

        composable(Screen.MenuMember.route) {
            MenuMemberScreen(navController = navController)
        }

        composable(Screen.Search.route){
            SearchScreen()
        }

        composable(Screen.HomepageAdmin.route) {
            HomepageAdminScreen(
                firestore = firestore,
                onClickLogin = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.HomepageAdmin.route) { inclusive = true }
                    }
                },
                onClickProfile = {
                    navController.navigate(Screen.ProfileLab.route)
                },
                onClickLeadershipAdmin = {
                    navController.navigate(Screen.LeadershipAdmin.route)
                },
                onClickMemberAdmin = {
                    navController.navigate(Screen.MemberAdmin.route)
                },
                onClickFacilitiesAdmin = {
                    navController.navigate(Screen.FacilitiesAdmin.route)
                },
                onClickStatistics = {
                    navController.navigate(Screen.Statistics.route)
                },
                onClickEvents = {
                    navController.navigate(Screen.Events.route)
                },
                onClickNews = {
                    navController.navigate(Screen.News.route)
                },
                onClickArticles = {
                    navController.navigate(Screen.Articles.route)
                },
                onClickJournals = {
                    navController.navigate(Screen.Journals.route)
                },
                navController = navController
            )
        }

        composable(Screen.MemberAdmin.route) {
            val viewModel: MemberViewModel = viewModel()
            MemberAdminScreen(
                navController = navController,
                firestore = firestore,
                viewModel = viewModel,
                onClickAdd = {
                    navController.navigate(Screen.AddEditMember.route)
                }
            )
        }

        composable(Screen.LeadershipAdmin.route) {
            val viewModel: LeaderViewModel = viewModel()
            LeadershipAdminScreen(
                navController = navController,
                firestore = firestore,
                viewModel = viewModel,
                onClickAdd = {
                    navController.navigate(Screen.AddEditLeader.route)
                }
            )
        }

        composable(Screen.FacilitiesAdmin.route) {
            FacilitiesAdminScreen(
                navController = navController,
                firestore = firestore,
                onClickAdd = {
                    navController.navigate(Screen.AddEditFacility.route)
                },
                onClickBack = {
                    navController.navigateUp()
                }
            )
        }

        composable(Screen.AddEditFacility.route) {
            AddEditFacilityScreen(
                firestore = firestore,
                navController = navController,
                facilityId = null
            )
        }

        composable("addeditfacility/{facilityId}") { backStackEntry ->
            val facilityId = backStackEntry.arguments?.getString("facilityId")
            AddEditFacilityScreen(
                firestore = firestore,
                navController = navController,
                facilityId = facilityId
            )
        }

        composable(Screen.AddEditMember.route) {
            AddEditMemberScreen(
                firestore = firestore,
                navController = navController,
                auth = FirebaseAuth.getInstance(),
                memberId = null
            )
        }

        composable("addeditmember/{memberId}") { backStackEntry ->
            val memberId = backStackEntry.arguments?.getString("memberId")
            AddEditMemberScreen(
                firestore = firestore,
                navController = navController,
                auth = FirebaseAuth.getInstance(),
                memberId = memberId
            )
        }

        composable(Screen.AddEditLeader.route) {
            AddEditLeaderScreen(
                firestore = firestore,
                navController = navController,
                leaderId = null
            )
        }

        composable("addeditleader/{leaderId}") { backStackEntry ->
            val leaderId = backStackEntry.arguments?.getString("leaderId")
            AddEditLeaderScreen(
                firestore = firestore,
                navController = navController,
                leaderId = leaderId
            )
        }

        composable(Screen.HomepageMember.route) {
            HomepageMemberScreen(
                firestore = firestore,
                onClickLogin = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.HomepageMember.route) {
                            inclusive = true
                        }
                    }
                },
                onClickProfile = {
                    navController.navigate(Screen.ProfileLab.route)
                },
                onClickLeadership = {
                    navController.navigate(Screen.Leadership.route)
                },
                onClickMember = {
                    navController.navigate(Screen.Member.route)
                },
                onClickFacilities = {
                    navController.navigate(Screen.Facilities.route)
                },
                onClickStatistics = {
                    navController.navigate(Screen.Statistics.route)
                },
                onClickEvents = {
                    navController.navigate(Screen.Events.route)
                },
                onClickNews = {
                    navController.navigate(Screen.News.route)
                },
                onClickArticles = {
                    navController.navigate(Screen.Articles.route)
                },
                onClickJournals = {
                    navController.navigate(Screen.Journals.route)
                },
                navController = navController
            )
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
                    navController.navigate(Screen.ProfileLab.route)
                },
                onClickLeadership = {
                    navController.navigate(Screen.Leadership.route)
                },
                onClickMember = {
                    navController.navigate(Screen.Member.route)
                },
                onClickFacilities = {
                    navController.navigate(Screen.Facilities.route)
                },
                onClickStatistics = {
                    navController.navigate(Screen.Statistics.route)
                },
                onClickEvents = {
                    navController.navigate(Screen.Events.route)
                },
                onClickNews = {
                    navController.navigate(Screen.News.route)
                },
                onClickArticles = {
                    navController.navigate(Screen.Articles.route)
                },
                onClickJournals = {
                    navController.navigate(Screen.Journals.route)
                },
                navController = navController
            )
        }

    }
}



@Composable
fun MenuScreen(

) {
    Text(text = "Menu Menu")
}



@Composable
fun StatisticsScreen(

) {
    Text(text = "Statistics")
}

@Composable
fun EventsScreen(

) {
    Text(text = "Events")
}

@Composable
fun NewsScreen(

) {
    Text(text = "News")
}

@Composable
fun ArticlesScreen(

) {
    Text(text = "Articles")
}

@Composable
fun JournalsScreen(

) {
    Text(text = "Journals")
}

@Composable
fun ContentScreen(

) {
    Text(text = "Content")
}

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    Text(text = "Search")
}















