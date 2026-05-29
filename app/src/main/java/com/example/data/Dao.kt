package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FitnessDao {

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUserFlow(userId: String = "user_current"): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserSync(userId: String = "user_current"): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM workouts ORDER BY dateLong DESC")
    fun getAllWorkouts(): Flow<List<WorkoutEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity)

    @Query("SELECT * FROM diet_plans ORDER BY dateLong DESC")
    fun getAllMeals(): Flow<List<DietPlanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: DietPlanEntity)

    @Query("SELECT * FROM progress_logs ORDER BY dateLong DESC")
    fun getProgressLogs(): Flow<List<ProgressLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgressLog(progressLog: ProgressLogEntity)

    @Query("SELECT * FROM payments ORDER BY dateLong DESC")
    fun getPayments(): Flow<List<PaymentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentEntity)

    @Query("SELECT * FROM chat_history ORDER BY timestampLong ASC")
    fun getChatHistory(): Flow<List<ChatHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatHistoryEntity)

    @Query("DELETE FROM chat_history")
    suspend fun clearChatHistory()
}
