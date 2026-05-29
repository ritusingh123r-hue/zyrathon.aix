package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = "user_current",
    val name: String,
    val email: String,
    val subscriptionRank: String = "ORIGIN", // "ORIGIN", "NEXUS", "TITAN-X", "OMEGA INFINITY"
    val heightCm: Float = 175f,
    val weightKg: Float = 72f,
    val dailyCalorieGoal: Int = 2200,
    val dailyWaterGoalMl: Int = 2500,
    val initialRegistered: Boolean = true
)

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // "Gym Mode", "Home Workout", "AI Customized"
    val durationMin: Int,
    val caloriesBurned: Int,
    val exercisesCsv: String,
    val dateLong: Long,
    val difficulty: String
)

@Entity(tableName = "diet_plans")
data class DietPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mealName: String,
    val mealType: String, // "Breakfast", "Lunch", "Dinner", "Snack"
    val calories: Int,
    val proteinG: Int,
    val carbsG: Int,
    val fatG: Int,
    val dateLong: Long
)

@Entity(tableName = "progress_logs")
data class ProgressLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateLong: Long,
    val weightKg: Float,
    val waterDrankMl: Int,
    val caloriesBurnedToday: Int,
    val activeMinutesToday: Int,
    val healthScore: Int
)

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amountPaidCents: Long,
    val planName: String,
    val status: String,
    val transactionId: String,
    val dateLong: Long
)

@Entity(tableName = "chat_history")
data class ChatHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val isAi: Boolean,
    val timestampLong: Long
)
