package com.example.studentemployee.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studentemployee.Screen
import com.example.studentemployee.core.components.RoundedCard
import com.example.studentemployee.core.components.TopAppBarMenu
import com.example.studentemployee.features.profile.ui.CustomButton
import com.example.studentemployee.features.profile.ui.CustomDivider
import com.example.studentemployee.features.profile.ui.CustomTextField
import com.example.studentemployee.features.members.AuthViewModel

@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isReauthenticated by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBarMenu(
                onClick = { navController.navigateUp() },
                text = "Tambah & Edit Penelitian"
            )
        },
        containerColor = Color(0xFFF9F9F9)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            RoundedCard {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    Text(
                        "Perbarui Password",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0XFFF37619)
                    )
                    CustomDivider()
                    if (!isReauthenticated) {
                        CustomTextField(
                            value = oldPassword,
                            onValueChange = { oldPassword = it },
                            label = "Masukkan Password Lama",
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                        )
                        Spacer(Modifier.height(8.dp))
                        CustomButton(
                            onClick = {
                                viewModel.reauthenticateUser(oldPassword) { success, error ->
                                    if (success) {
                                        isReauthenticated = true
                                        errorMessage = null
                                        Toast.makeText(
                                            context,
                                            "Verifikasi berhasil",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        oldPassword = ""
                                    } else {
                                        errorMessage = error
                                        Toast.makeText(
                                            context,
                                            error
                                                ?: "Terjadi kesalahan masukkan password lagi",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        oldPassword = ""
                                    }
                                }
                            },
                            text = "Verifikasi"
                        )
                    } else {
                        CustomTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = "Masukkan Password Baru",
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                        )
                        CustomTextField(
                            value = confirmPassword,
                            onValueChange = {
                                confirmPassword = it
                            },
                            label = "Konfirmasi Password Baru",
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                        )
                        Spacer(Modifier.height(8.dp))
                        CustomButton(
                            onClick = {
                                if (newPassword != confirmPassword) {
                                    errorMessage = "Password baru tidak cocok"
                                    Toast.makeText(
                                        context,
                                        errorMessage,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                viewModel.updatePassword(newPassword) { success, error ->
                                    if (success) {
                                        successMessage =
                                            "Password berhasil diperbarui"
                                        errorMessage = null
                                        Toast.makeText(
                                            context,
                                            successMessage,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        newPassword = ""
                                        confirmPassword = ""
                                        navController.navigate(Screen.MenuMember.route)
                                    } else {
                                        errorMessage = error
                                        Toast.makeText(
                                            context,
                                            error
                                                ?: "Gagal memperbarui password",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        newPassword = ""
                                        confirmPassword = ""
                                    }
                                }
                            },
                            text = "Simpan Password Baru"
                        )
                    }
                }
            }
        }
    }
}