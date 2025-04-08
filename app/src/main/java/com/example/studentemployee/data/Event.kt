package com.example.studentemployee.data

data class Event(
    override val title: String = "",
    var id: String = "",
    val date: String = "",
    val time: String = "",
    val imageUrl: String = "",
    val link: String = ""
) : HasTitle