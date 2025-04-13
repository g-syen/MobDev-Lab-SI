package com.example.studentemployee.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.studentemployee.Screen
import com.example.studentemployee.components.TopAppBarMenu
import com.example.studentemployee.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProfileViewModel = viewModel(),
) {

    val userProfile by viewModel.userProfile.collectAsState()
    val socialLinks by viewModel.socialLinks.collectAsState()

    var name by remember { mutableStateOf("") }
    var nip by remember { mutableStateOf("") }
    var researchInterest by remember { mutableStateOf("") }
    var linkedinUrl by remember { mutableStateOf("") }
    var githubUrl by remember { mutableStateOf("") }
    var youtubeUrl by remember { mutableStateOf("") }
    var instagramUrl by remember { mutableStateOf("") }
    var biography by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            viewModel.loadUserProfile(userId)
        }
    }

    LaunchedEffect(userProfile) {
        userProfile?.let {
            name = it.nama
            nip = it.nip
            biography = it.biography
            researchInterest = it.specialist
        }
    }

    LaunchedEffect(socialLinks) {
        linkedinUrl = socialLinks["LinkedIn"] ?: ""
        githubUrl = socialLinks["GitHub"] ?: ""
        youtubeUrl = socialLinks["YouTube"] ?: ""
        instagramUrl = socialLinks["Instagram"] ?: ""
    }

    Scaffold(
        topBar = {
            TopAppBarMenu(
                onClick = { navController.navigateUp() },
                text = "Edit Profil Saya"
            )
        },
        containerColor = Color(0xFFF9F9F9)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            NameInitialsCard(name = name, nip = nip)
            CustomDivider()
            CustomTextField(
                value = researchInterest,
                onValueChange = { researchInterest = it },
                label = "Masukkan Research Interest"
            )
            CustomTextField(
                value = linkedinUrl,
                onValueChange = { linkedinUrl = it },
                label = "Masukkan link LinkedIn Anda"
            )
            CustomTextField(
                value = githubUrl,
                onValueChange = { githubUrl = it },
                label = "Masukkan link GitHub Anda"
            )
            CustomTextField(
                value = youtubeUrl,
                onValueChange = { youtubeUrl = it },
                label = "Masukkan link YouTube Anda"
            )
            CustomTextField(
                value = instagramUrl,
                onValueChange = { instagramUrl = it },
                label = "Masukkan link Instagram Anda"
            )
            CustomTextField(
                value = biography,
                onValueChange = { biography = it },
                label = "Masukkan biografi Anda"
            )
            Spacer(Modifier.height(8.dp))
            CustomButton(onClick = {
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (uid != null) {
                    viewModel.saveUserProfile(uid, biography, researchInterest)
                    val links = mapOf(
                        "LinkedIn" to linkedinUrl,
                        "GitHub" to githubUrl,
                        "YouTube" to youtubeUrl,
                        "Instagram" to instagramUrl
                    )
                    viewModel.saveSocialLinks(uid, links)
                }
                navController.navigate(Screen.Profile.route)
            }, text = "Simpan")
        }
    }
}

@Preview
@Composable
private fun EditProfileScreenPreview() {
    EditProfileScreen(navController = rememberNavController())
}

@Composable
fun NameInitialsCard(modifier: Modifier = Modifier, name: String, nip: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
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
}

@Preview
@Composable
private fun NameInitialsCardPreview() {
    NameInitialsCard(
        name = "Dewi Kartika, S.Kom., M.Sc. ",
        nip = "198802022012100901"
    )
}

@Composable
fun CustomDivider(modifier: Modifier = Modifier) {
    Divider(
        color = Color(0xFF195693),
        thickness = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: (@Composable (() -> Unit))? = null

) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label, color = Color(0xFFE2640D))
        },
        textStyle = TextStyle(
            color = Color(0xFF195693),
            fontSize = 16.sp
        ),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(0XFFF9F9F9),
            cursorColor = Color(0xFF195693),
            focusedIndicatorColor = Color(0XFF195693),
            unfocusedIndicatorColor = Color(0XFF195693)
        ),
        modifier = modifier
            .fillMaxWidth(),
        supportingText = supportingText,
        isError = isError
    )
}

@Preview(showBackground = true)
@Composable
private fun CustomTextFieldPreview() {
    var name by remember { mutableStateOf("") }
    CustomTextField(
        value = name,
        onValueChange = { name = it },
        label = "Nama Lengkap"
    )
}