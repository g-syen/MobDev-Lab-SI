package com.example.studentemployee.features.events.model

import com.example.studentemployee.core.model.HasTitle

data class Event(
    override val title: String = "",
    var id: String = "",
    val date: String = "",
    val time: String = "",
    val imageUrl: String = "",
    val link: String = ""
) : HasTitle