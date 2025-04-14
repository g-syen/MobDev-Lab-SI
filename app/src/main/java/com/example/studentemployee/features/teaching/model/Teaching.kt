package com.example.studentemployee.features.teaching.model

import com.example.studentemployee.core.model.HasTitle

data class Teaching(
    val id: String = "",
    override val title: String = "",
    val classname: String = "",
    val semester: String = "",
    val year: String = ""
) : HasTitle