package com.example.studentemployee.core.database

import androidx.room.*
import com.example.studentemployee.features.search.model.SearchHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM search_history ORDER BY id DESC")
    fun getAllHistory(): Flow<List<SearchHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(searchHistory: SearchHistory)

    @Query("DELETE FROM search_history")
    suspend fun clearHistory()

    @Delete
    suspend fun deleteSearch(searchHistory: SearchHistory)

}


