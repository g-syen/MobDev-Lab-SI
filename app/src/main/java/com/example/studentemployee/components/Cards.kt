package com.example.studentemployee.components

import android.widget.Space
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarMenu(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF195693)
        ),
        title = {
            Text(
                text,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    tint = Color.White,
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = ""
                )
            }
        },
    )
}

@Preview
@Composable
private fun TopAppBarMenuPreview() {
    TopAppBarMenu(onClick = {}, text = "Profil Lab Sistem Informasi")
}

@Composable
fun RoundedCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, shape = RoundedCornerShape(8.dp)) // Shadow tetap ada
            .clip(RoundedCornerShape(8.dp)) // Clip agar background mengikuti shape
            .background(Color.White) // Background mengikuti clip
            .padding(24.dp) // Tambahkan padding agar konten tidak menempel

    ) {
        content()
    }
}

@Preview
@Composable
private fun RoundedCardPreview() {
    RoundedCard { Text("RoundedCard") }
}


@Composable
fun DropDownProfile(title: String, description: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
//            .padding(vertical = 15.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    color = Color(0xffF37619),
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Expand",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xffF37619)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Text(
                    text = description,
                    fontSize = 16.sp,
                    color = Color(0xff195693),
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun DropDownProfilePreview() {
    DropDownProfile(
        title = "Dropdown Title",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed venenatis, nunc eu consequat egestas, dui erat hendrerit justo, et fringilla nisl dui a turpis. Mauris tortor libero, semper at tortor at, tincidunt porta ipsum. Curabitur ac porta tellus."
    )
}

@Composable
fun LeadershipCard(modifier: Modifier = Modifier,jabatan:String,nama:String) {
    RoundedCard {
        Row(verticalAlignment = Alignment.CenterVertically, ) {
            Column(modifier=Modifier.weight(1f)) {
                Text(
                    jabatan,
                    fontSize = 14.sp,
                    color = Color(0xffF37619),
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    nama,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff195693)
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Expand",
                modifier = Modifier.size(24.dp),
                tint = Color(0xffF37619)
            )
        }
    }
}

@Preview
@Composable
private fun LeadershipCardPreview() {
    LeadershipCard(jabatan = "Kepala Lab", nama = "Riswan Septriayadi Sianturi, S.Si.., MM., M.Sc., Ph.D.")
}