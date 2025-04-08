package com.example.studentemployee.data

data class Teaching(
    override val title: String = "",
    val year: String = "",
    val semester: String = ""
) : HasTitle