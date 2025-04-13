package com.example.studentemployee.data

data class Devotion(
    val id:String="",
    override val title: String = "",
    val link: String = "",
    val contributors:String = ""
): HasTitle
