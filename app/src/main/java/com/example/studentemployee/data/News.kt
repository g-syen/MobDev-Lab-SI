package com.example.studentemployee.data

data class News(
    override val title: String = "",
    var id: String = "",
    val date: String = "",
    val imageUrl: String = "",
    val link: String = ""
) : HasTitle