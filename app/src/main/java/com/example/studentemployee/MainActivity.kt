package com.example.studentemployee

import android.os.Bundle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import com.example.studentemployee.features.members.ui.DivisiAdminScreen
import com.example.studentemployee.ui.theme.StudentEmployeeTheme
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.studentemployee.features.admin.ui.KelompokAdminScreen
import com.example.studentemployee.features.members.ui.MemberAdminScreen
import com.example.studentemployee.features.auth.ui.LoginScreen
import com.example.studentemployee.features.publication.ui.ContentScreen
import com.example.studentemployee.features.devotion.ui.AddEditDevotionScreen
import com.example.studentemployee.features.events.ui.AddEditEventScreen
import com.example.studentemployee.features.facilities.ui.AddEditFacilityScreen
import com.example.studentemployee.features.facilities.ui.FacilitiesAdminScreen
import com.example.studentemployee.features.facilities.ui.FacilitiesScreen
import com.example.studentemployee.features.homepage.ui.HomepageAdminScreen
import com.example.studentemployee.features.homepage.ui.HomepageGuestScreen
import com.example.studentemployee.features.homepage.ui.HomepageMemberScreen
import com.example.studentemployee.features.leadership.ui.AddEditLeaderScreen
import com.example.studentemployee.features.leadership.ui.LeadershipAdminScreen
import com.example.studentemployee.features.leadership.ui.LeadershipScreen
import com.example.studentemployee.features.profile.ui.EditProfileScreen
import com.example.studentemployee.features.profile.ui.ProfileScreen
import com.example.studentemployee.features.leadership.LeaderViewModel
import com.example.studentemployee.features.members.ui.AddEditMemberScreen
import com.example.studentemployee.features.members.ui.MemberScreen
import com.example.studentemployee.features.members.MemberViewModel
import com.example.studentemployee.features.members.StudentEmployeeViewModel
import com.example.studentemployee.features.members.ui.AddEditAnggotaScreen
import com.example.studentemployee.features.members.ui.AddEditDivisiScreen
import com.example.studentemployee.features.members.ui.AddEditKelompokScreen
import com.example.studentemployee.features.members.ui.AddEditStudentEmployeeScreen
import com.example.studentemployee.features.members.ui.DivisiScreen
import com.example.studentemployee.features.members.ui.KelompokScreen
import com.example.studentemployee.features.members.ui.PersonalMemberScreen
import com.example.studentemployee.features.dashboard.ui.MenuAdminScreen
import com.example.studentemployee.features.dashboard.ui.MenuMemberScreen
import com.example.studentemployee.features.news.ui.AddEditNewsScreen
import com.example.studentemployee.features.profilelab.ui.ProfileLabScreen
import com.example.studentemployee.features.research.ui.AddEditResearchScreen
import com.example.studentemployee.features.search.ui.SearchAdminScreen
import com.example.studentemployee.features.search.ui.SearchGuestScreen
import com.example.studentemployee.features.search.ui.SearchMemberScreen
import com.example.studentemployee.features.search.SearchViewModel
import com.example.studentemployee.features.teaching.ui.AddEditTeachingScreen
import com.example.studentemployee.features.uploadprofile.ui.UploadProfileScreen
import com.example.studentemployee.screen.ChangePasswordScreen
import com.example.studentemployee.screen.StatisticsScreen
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage

class MainActivity : ComponentActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
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
    object Divisi : Screen("divisi")
    object Kelompok : Screen("kelompok")
    object Facilities : Screen("facilities")
    object Statistics : Screen("statistics")
    object News : Screen("news")
    object Content : Screen("content")
    object SearchGuest : Screen("searchguest")

    object Profile : Screen("profile")
    object MenuMember : Screen("menumember")
    object PersonalMember : Screen("personalmember")
    object SearchMember : Screen("searchmember")
    object EditProfile:Screen("editprofile")
    object AddEditResearch:Screen("addeditresearch")
    object AddEditDevotion:Screen("addeditdevotion")
    object AddEditTeaching:Screen("addeditteaching")
    object ChangePassword:Screen("changepassword")

    object HomepageAdmin : Screen("homepageadmin")
    object FacilitiesAdmin : Screen("facilitiesadmin")
    object AddEditFacility : Screen("addeditfacility")
    object AddEditMember : Screen("addeditmember")
    object AddEditStudentEmployee : Screen("addeditstudentemployee")
    object MemberAdmin : Screen("memberadmin")
    object DivisiAdmin : Screen("divisiadmin")
    object AddEditLeader : Screen("addeditleader")
    object LeadershipAdmin : Screen("leadershipadmin")
    object MenuAdmin : Screen("menuadmin")
    object AddEditEvent : Screen("addeditevent")
    object AddEditNews : Screen("addeditnews")
    object SearchAdmin : Screen("searchadmin")

}

@Composable
fun AppNavigation(
    currentUser: FirebaseUser?,
    firestore: FirebaseFirestore,
    storage: FirebaseStorage
) {
    val navController = rememberNavController()

    var startDestination by remember { mutableStateOf(Screen.HomepageGuest.route) }

    val sharedViewModel = StudentEmployeeViewModel()

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
            ContentScreen(
                onClickLogin = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.HomepageAdmin.route) { inclusive = true }
                    }
                },
                navController = navController
            )
        }

        composable("content/{contentId}") { backStackEntry ->
            val contentIdString = backStackEntry.arguments?.getString("contentId")
            val contentId = contentIdString?.toIntOrNull()
            contentId?.let {
                ContentScreen(
                    navController = navController,
                    onClickLogin = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.HomepageAdmin.route) { inclusive = true }
                        }
                    },
                    selectedPage = it
                )
            } ?: run {
                navController.navigateUp()
            }
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
                viewModel = viewModel,
                seViewModel = sharedViewModel,
            )
        }

        composable(Screen.Divisi.route) {
            val viewModel: MemberViewModel = viewModel()
            DivisiScreen(
                navController = navController,
                firestore = firestore,
                viewModel = viewModel,
                seViewModel = sharedViewModel,
            )
        }

        composable("divisi/{yearBatch}") { backStackEntry ->
            val yearBatch = backStackEntry.arguments?.getString("yearBatch")
            val viewModel: MemberViewModel = viewModel()
            yearBatch?.let {
                DivisiScreen(
                    navController = navController,
                    firestore = firestore,
                    viewModel = viewModel,
                    seViewModel = sharedViewModel,
                    selectedYearBatch = yearBatch
                )
            } ?: run {
                navController.navigateUp()
            }
        }

        composable(Screen.Kelompok.route) {
            val viewModel: MemberViewModel = viewModel()
            KelompokScreen(
                navController = navController,
                firestore = firestore,
                viewModel = viewModel,
                seViewModel = sharedViewModel,
            )
        }

        composable("kelompok/{studempDivisi}") { backStackEntry ->
            val studempDivisi = backStackEntry.arguments?.getString("studempDivisi")
            val selectedStudentEmployee = studempDivisi?.substringBefore(",")
            val selectedDivisi = studempDivisi?.substringAfter(",")
            val viewModel: MemberViewModel = viewModel()
            studempDivisi?.let {
                KelompokScreen(
                    navController = navController,
                    firestore = firestore,
                    viewModel = viewModel,
                    seViewModel = sharedViewModel,
                    selectedDivisi = selectedDivisi,
                    selectedStudentEmployee = selectedStudentEmployee
                )
            } ?: run {
                navController.navigateUp()
            }
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
            StatisticsScreen(navController = navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }

        composable(Screen.EditProfile.route){
            EditProfileScreen(navController = navController)
        }

        composable(Screen.MenuMember.route) {
            MenuMemberScreen(
                navController = navController,
                onClickLogin = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.MenuMember.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.SearchMember.route){
            val viewModel: SearchViewModel = viewModel()
            SearchMemberScreen(
                firestore = firestore,
                navController = navController,
                viewModel = viewModel
                )
        }

        composable(Screen.AddEditResearch.route){
            AddEditResearchScreen(navController = navController)
        }

        composable(Screen.AddEditDevotion.route){
            AddEditDevotionScreen(navController = navController)
        }

        composable(Screen.AddEditTeaching.route){
            AddEditTeachingScreen(navController = navController)
        }

        composable(Screen.ChangePassword.route){
            ChangePasswordScreen(navController = navController)
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
                    navController.navigate("content/1")
                },
                onClickNews = {
                    navController.navigate("content/0")
                },
                onClickResearch = {
                    navController.navigate("content/2")
                },
                onClickDevotion = {
                    navController.navigate("content/3")
                },
                navController = navController
            )
        }

        composable(Screen.MemberAdmin.route) {
            val viewModel: MemberViewModel = viewModel()
            MemberAdminScreen(
                navController = navController,
                seViewModel = sharedViewModel,
                memberViewModel = viewModel,
            )
        }

        composable(Screen.AddEditStudentEmployee.route) {
            AddEditStudentEmployeeScreen(
                navController = navController,
                studentEmployeeId = null
            )
        }

        composable(
            route = "addEditStudentEmployee/{studentEmployeeId}",
            arguments = listOf(navArgument("studentEmployeeId") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val studentEmployeeId = backStackEntry.arguments?.getString("studentEmployeeId")
            AddEditStudentEmployeeScreen(
                navController = navController,
                studentEmployeeId = studentEmployeeId
            )
        }

        composable(Screen.DivisiAdmin.route) {
            DivisiAdminScreen(
                navController = navController,
                seViewModel = sharedViewModel,
                selectedYearBatch = ""
            )
        }

        composable(
            route = "divisiadmin/{selectedYearBatch}",
            arguments = listOf(navArgument("selectedYearBatch") { type = NavType.StringType })
        ) { backStackEntry ->
            val selectedYearBatch = backStackEntry.arguments?.getString("selectedYearBatch")
            DivisiAdminScreen(
                navController = navController,
                selectedYearBatch = selectedYearBatch
            )
        }

        composable(
            route = "addEditDivisi/{studentEmployeeId}/{divisiId}",
            arguments = listOf(
                navArgument("studentEmployeeId") { type = NavType.StringType },
                navArgument("divisiId") { type = NavType.StringType; nullable = true }
            )
        ) { backStackEntry ->
            val studentEmployeeId = backStackEntry.arguments?.getString("studentEmployeeId")
            val divisiId = backStackEntry.arguments?.getString("divisiId")
            if (studentEmployeeId != null) {
                AddEditDivisiScreen(
                    navController = navController,
                    studentEmployeeId = studentEmployeeId,
                    divisiId = divisiId
                )
            } else {
                Text("Error: Student Employee ID is missing.")
            }
        }
        composable(
            route = "addEditDivisi/{studentEmployeeId}",
            arguments = listOf(navArgument("studentEmployeeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val studentEmployeeId = backStackEntry.arguments?.getString("studentEmployeeId")
            if (studentEmployeeId != null) {
                AddEditDivisiScreen(
                    navController = navController,
                    studentEmployeeId = studentEmployeeId,
                    divisiId = null
                )
            } else {
                Text("Error: Student Employee ID is missing for adding divisi.")
            }
        }

        composable(
            route = "kelompokAdmin/{studentEmployeeId}/{divisiId}",
            arguments = listOf(
                navArgument("studentEmployeeId") { type = NavType.StringType },
                navArgument("divisiId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val studentEmployeeId = backStackEntry.arguments?.getString("studentEmployeeId")
            val divisiId = backStackEntry.arguments?.getString("divisiId")
            if (studentEmployeeId != null && divisiId != null) {
                KelompokAdminScreen(
                    navController = navController,
                    studentEmployeeId = studentEmployeeId,
                    divisiId = divisiId
                )
            } else {
                Text("Error: Missing Student Employee ID or Divisi ID for Kelompok Admin.")
            }
        }

        composable(
            route = "addEditKelompok/{studentEmployeeId}/{divisiId}/{kelompokId}",
            arguments = listOf(
                navArgument("studentEmployeeId") { type = NavType.StringType },
                navArgument("divisiId") { type = NavType.StringType },
                navArgument("kelompokId") { type = NavType.StringType; nullable = true }
            )
        ) { backStackEntry ->
            val studentEmployeeId = backStackEntry.arguments?.getString("studentEmployeeId")
            val divisiId = backStackEntry.arguments?.getString("divisiId")
            val kelompokId = backStackEntry.arguments?.getString("kelompokId")

            if (studentEmployeeId != null && divisiId != null) {
                AddEditKelompokScreen(
                    navController = navController,
                    studentEmployeeId = studentEmployeeId,
                    divisiId = divisiId,
                    kelompokId = kelompokId
                )
            } else {
                Text("Error: Missing IDs for Add/Edit Kelompok.")
            }
        }

        composable(
            route = "addEditKelompok/{studentEmployeeId}/{divisiId}",
            arguments = listOf(
                navArgument("studentEmployeeId") { type = NavType.StringType },
                navArgument("divisiId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val studentEmployeeId = backStackEntry.arguments?.getString("studentEmployeeId")
            val divisiId = backStackEntry.arguments?.getString("divisiId")
            if (studentEmployeeId != null && divisiId != null) {
                AddEditKelompokScreen(
                    navController = navController,
                    studentEmployeeId = studentEmployeeId,
                    divisiId = divisiId,
                    kelompokId = null // Explicitly null for add mode
                )
            } else {
                Text("Error: Missing IDs for Add Kelompok.")
            }
        }

        composable(
            route = "addEditAnggota/{studentEmployeeId}/{divisiId}/{kelompokId}/{anggotaId}", // anggotaId is optional for add
            arguments = listOf(
                navArgument("studentEmployeeId") { type = NavType.StringType },
                navArgument("divisiId") { type = NavType.StringType },
                navArgument("kelompokId") { type = NavType.StringType },
                navArgument("anggotaId") { type = NavType.StringType; nullable = true }
            )
        ) { backStackEntry ->
            val studentEmployeeId = backStackEntry.arguments?.getString("studentEmployeeId")
            val divisiId = backStackEntry.arguments?.getString("divisiId")
            val kelompokId = backStackEntry.arguments?.getString("kelompokId")
            val anggotaId = backStackEntry.arguments?.getString("anggotaId")

            if (studentEmployeeId != null && divisiId != null && kelompokId != null) {
                AddEditAnggotaScreen(
                    navController = navController,
                    studentEmployeeId = studentEmployeeId,
                    divisiId = divisiId,
                    kelompokId = kelompokId,
                    anggotaId = anggotaId
                )
            } else {
                Text("Error: Missing IDs for Add/Edit Anggota.")
            }
        }

        composable(
            route = "addEditAnggota/{studentEmployeeId}/{divisiId}/{kelompokId}",
            arguments = listOf(
                navArgument("studentEmployeeId") { type = NavType.StringType },
                navArgument("divisiId") { type = NavType.StringType },
                navArgument("kelompokId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val studentEmployeeId = backStackEntry.arguments?.getString("studentEmployeeId")
            val divisiId = backStackEntry.arguments?.getString("divisiId")
            val kelompokId = backStackEntry.arguments?.getString("kelompokId")
            if (studentEmployeeId != null && divisiId != null && kelompokId != null) {
                AddEditAnggotaScreen(
                    navController = navController,
                    studentEmployeeId = studentEmployeeId,
                    divisiId = divisiId,
                    kelompokId = kelompokId,
                    anggotaId = null
                )
            } else {
                Text("Error: Missing IDs for Add Anggota.")
            }
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


        composable(Screen.AddEditEvent.route){
            AddEditEventScreen(firestore = firestore, navController = navController, eventId = null)
        }

        composable("addeditevent/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            AddEditEventScreen(
                firestore = firestore,
                navController = navController,
                eventId = eventId
            )
        }

        composable(Screen.AddEditNews.route) {
            AddEditNewsScreen(firestore = firestore, navController = navController, newsId = null)
        }

        composable("addeditnews/{newsId}") { backStackEntry ->
            val newsId = backStackEntry.arguments?.getString("newsId")
            AddEditNewsScreen(
                firestore = firestore,
                navController = navController,
                newsId = newsId
            )
        }

        composable(Screen.MenuAdmin.route){
            MenuAdminScreen(
                navController = navController,
                onClickAddEvent = {
                    navController.navigate(Screen.AddEditEvent.route) {
                        popUpTo(Screen.MenuAdmin.route) { inclusive = false }
                    }
                },
                onClickAddNews = {
                    navController.navigate(Screen.AddEditNews.route) {
                        popUpTo(Screen.MenuAdmin.route) { inclusive = false }
                    }
                },
                onClickLogin = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.MenuAdmin.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.SearchAdmin.route){
            val viewModel: SearchViewModel = viewModel()
            SearchAdminScreen(
                firestore = firestore,
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(Screen.HomepageMember.route) {
            HomepageMemberScreen(
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
                    navController.navigate("content/1")
                },
                onClickNews = {
                    navController.navigate("content/0")
                },
                onClickResearch = {
                    navController.navigate("content/2")
                },
                onClickDevotion = {
                    navController.navigate("content/3")
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
                    navController.navigate("content/1")
                },
                onClickNews = {
                    navController.navigate("content/0")
                },
                onClickResearch = {
                    navController.navigate("content/2")
                },
                onClickDevotion = {
                    navController.navigate("content/3")
                },
                navController = navController
            )
        }

        composable(Screen.SearchGuest.route){
            val viewModel: SearchViewModel = viewModel()
            SearchGuestScreen(
                firestore = firestore,
                navController = navController,
                viewModel = viewModel
            )
        }

    }
}





