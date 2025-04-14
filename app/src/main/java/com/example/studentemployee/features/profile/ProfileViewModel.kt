package com.example.studentemployee.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentemployee.features.devotion.model.Devotion
import com.example.studentemployee.repository.FirestoreRepository
import com.example.studentemployee.features.research.model.Research
import com.example.studentemployee.features.teaching.model.Teaching
import com.example.studentemployee.features.profile.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val repository = FirestoreRepository()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile

    private val _socialLinks = MutableStateFlow<Map<String, String>>(emptyMap())
    val socialLinks: StateFlow<Map<String, String>> = _socialLinks

    private val _researches = MutableStateFlow<List<Research>>(emptyList())
    val researches: StateFlow<List<Research>> = _researches

    private val _devotions = MutableStateFlow<List<Devotion>>(emptyList())
    val devotions: StateFlow<List<Devotion>> = _devotions

    private val _teachings = MutableStateFlow<List<Teaching>>(emptyList())
    val teachings: StateFlow<List<Teaching>> = _teachings

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
                repository.getUserResearches(userId).collect {
                    _researches.value = it
                }
            }
            launch {
                repository.getUserDevotions(userId).collect {
                    _devotions.value = it
                }
            }
            launch {
                repository.getUserTeachings(userId).collect{
                    _teachings.value = it
                }
            }
        }
    }
}
