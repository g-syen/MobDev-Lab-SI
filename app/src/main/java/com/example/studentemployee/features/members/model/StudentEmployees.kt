package com.example.studentemployee.features.members.model

data class Anggota(
    val id: String? = null,
    val nama: String? = null,
    val nim: String? = null
)

data class Kelompok(
    val id: String? = null,
    val kelompok: Double? = null,
    val anggota: List<Anggota> = emptyList()
)

data class Divisi(
    val id: String? = null,
    val divisi: String? = null,
    val kelompok: List<Kelompok> = emptyList()
)

data class StudentEmployee(
    val id: String? = null,
    val batch: String? = null,
    val year: String? = null,
    val divisi: List<Divisi> = emptyList()
)

data class StudentEmployees(
    val studentEmployees: List<StudentEmployee> = emptyList()
)