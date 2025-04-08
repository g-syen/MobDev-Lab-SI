package com.example.studentemployee.data

data class Devotion(
    override val title: String = "",
    val description: String = "",
    val link: String = ""
) : HasTitle
