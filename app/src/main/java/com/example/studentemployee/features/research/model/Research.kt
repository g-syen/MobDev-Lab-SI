package com.example.studentemployee.features.research.model

import com.example.studentemployee.core.model.HasTitle

data class Research(
    val id: String = "",
    override val title: String = "",
    val authors: String = "",
    val link: String = ""
) : HasTitle
