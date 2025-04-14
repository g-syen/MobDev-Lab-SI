package com.example.studentemployee.features.devotion.model

import com.example.studentemployee.core.model.HasTitle

data class Devotion(
    val id:String="",
    override val title: String = "",
    val link: String = "",
    val contributors:String = ""
): HasTitle
