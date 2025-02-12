package com.example.pokemonultimate.data.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokemonultimate.data.model.userModel.UserProfile

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(userProfile: UserProfile)

    @Query("SELECT * FROM user_profile WHERE userId = :userId LIMIT 1")
    suspend fun getUserProfile(userId: String): UserProfile?

    @Query("SELECT * FROM user_profile")
    suspend fun getAllProfiles(): List<UserProfile>

    @Query("DELETE FROM user_profile")
    suspend fun clearUserData()
}