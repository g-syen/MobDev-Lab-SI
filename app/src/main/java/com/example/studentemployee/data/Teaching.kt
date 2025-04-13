package com.example.studentemployee.data

data class Teaching(
    val id: String = "",
    override val title: String = "",
    val classname: String = "",
    val semester: String = "",
    val year: String = ""
) : HasTitle