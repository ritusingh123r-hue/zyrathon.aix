package com.example.data

import kotlinx.coroutines.flow.Flow

class FitnessRepository(private val fitnessDao: FitnessDao) {

    fun getUserFlow(userId: String = "user_current"): Flow<UserEntity?> {
        return fitnessDao.getUserFlow(userId)
    }

    suspend fun getUserSync(userId: String = "user_current"): UserEntity? {
        return fitnessDao.getUserSync(userId)
    }

    suspend fun insertUser(user: UserEntity) {
        fitnessDao.insertUser(user)
    }

    fun getAllWorkouts(): Flow<List<WorkoutEntity>> {
        return fitnessDao.getAllWorkouts()
    }

    suspend fun insertWorkout(workout: WorkoutEntity) {
        fitnessDao.insertWorkout(workout)
    }

    fun getAllMeals(): Flow<List<DietPlanEntity>> {
        return fitnessDao.getAllMeals()
    }

    suspend fun insertMeal(meal: DietPlanEntity) {
        fitnessDao.insertMeal(meal)
    }

    fun getProgressLogs(): Flow<List<ProgressLogEntity>> {
        return fitnessDao.getProgressLogs()
    }

    suspend fun insertProgressLog(progressLog: ProgressLogEntity) {
        fitnessDao.insertProgressLog(progressLog)
    }

    fun getPayments(): Flow<List<PaymentEntity>> {
        return fitnessDao.getPayments()
    }

    suspend fun insertPayment(payment: PaymentEntity) {
        fitnessDao.insertPayment(payment)
    }

    fun getChatHistory(): Flow<List<ChatHistoryEntity>> {
        return fitnessDao.getChatHistory()
    }

    suspend fun insertChatMessage(message: ChatHistoryEntity) {
        fitnessDao.insertChatMessage(message)
    }

    suspend fun clearChatHistory() {
        fitnessDao.clearChatHistory()
    }
}
