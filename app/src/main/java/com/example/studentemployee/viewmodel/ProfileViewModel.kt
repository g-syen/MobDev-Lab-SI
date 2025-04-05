package com.example.studentemployee.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentemployee.data.Article
import com.example.studentemployee.data.Devotion
import com.example.studentemployee.data.FirestoreRepository
import com.example.studentemployee.data.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val repository = FirestoreRepository()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile

    private val _socialLinks = MutableStateFlow<Map<String, String>>(emptyMap())
    val socialLinks: StateFlow<Map<String, String>> = _socialLinks

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles

    private val _devotions = MutableStateFlow<List<Devotion>>(emptyList())
    val devotions: StateFlow<List<Devotion>> = _devotions

    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            launch {
                repository.getUserProfile(userId).collect { profile ->
                    _userProfile.value = profile
                }
            }
            launch {
                repository.getUserSocialLinks(userId).collect { links ->
                    _socialLinks.value = links
                }
            }
        }
    }

    fun saveUserProfile(userId: String,  bio: String, interest: String) {
        val data = mapOf(
            "biography" to bio,
            "specialist" to interest
        )
        repository.updateUserProfile(userId, data)
    }

    fun saveSocialLinks(userId: String, links: Map<String, String>) {
        links.forEach { (platform, link) ->
            repository.updateSocialLink(userId, platform, link)
        }
    }

    fun loadUserContributions(userId: String) {
        viewModelScope.launch {
            launch {
                repository.getUserArticles(userId).collect {
                    _articles.value = it
                }
            }
            launch {
                repository.getUserDevotions(userId).collect {
                    _devotions.value = it
                }
            }
        }
    }
}
