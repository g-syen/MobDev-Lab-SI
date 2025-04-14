package com.example.studentemployee.features.news.model

import com.example.studentemployee.core.model.HasTitle

data class News(
    override val title: String = "",
    var id: String = "",
    val date: String = "",
    val imageUrl: String = "",
    val link: String = ""
) : HasTitle