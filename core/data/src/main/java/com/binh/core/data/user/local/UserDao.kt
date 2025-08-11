package com.binh.core.data.user.local


import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id: Long): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(user: UserEntity)

    @Query("DELETE FROM user WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Update
    suspend fun update(user: UserEntity)

    @Query("SELECT * FROM user")
    suspend fun getAll(): List<UserEntity>

    @Query("UPDATE user SET accessToken = :newAccessToken WHERE id = :userId")
    suspend fun updateNewAccessToken(userId: Long, newAccessToken: String)
}