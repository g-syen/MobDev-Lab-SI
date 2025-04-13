package com.example.studentemployee.data

data class Research(
    val id: String = "",
    override val title: String = "",
    val authors: String = "",
    val link: String = ""
) : HasTitle
