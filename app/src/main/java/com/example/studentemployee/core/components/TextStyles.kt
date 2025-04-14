package com.example.studentemployee.core.components


import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun SectionHeader(modifier: Modifier = Modifier, text: String) {
    Text(
        text,
        color = Color(0xffF37619),
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
}

@Preview
@Composable
private fun SectionHeaderPreview() {
    SectionHeader(text = "Misi")
}

@Composable
fun SubHeader(modifier: Modifier = Modifier, text: String, color: Color) {
    Text(
        text,
        color = color,
        fontSize = 16.sp,
        textAlign = TextAlign.Justify,
        fontWeight = FontWeight.Bold
    )
}

@Preview
@Composable
private fun SubHeaderPreview() {
    SubHeader(text = "Event Saat ini", color = Color(0xffF37619))
}