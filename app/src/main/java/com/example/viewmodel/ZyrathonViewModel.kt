package com.example.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.api.Content as ApiContent
import com.example.api.GenerateContentRequest
import com.example.api.Part as ApiPart
import com.example.api.RetrofitClient
import com.example.data.AppDatabase
import com.example.data.ChatHistoryEntity
import com.example.data.DietPlanEntity
import com.example.data.FitnessRepository
import com.example.data.PaymentEntity
import com.example.data.ProgressLogEntity
import com.example.data.UserEntity
import com.example.data.WorkoutEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

enum class AppScreen {
    LANDING,
    LOGIN,
    DASHBOARD,
    AI_FITNESS,
    AI_NUTRITION,
    AI_CHAT,
    SUBSCRIPTION,
    ADMIN
}

class ZyrathonViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = FitnessRepository(db.fitnessDao())

    // UI Configuration & Navigation
    private val _currentScreen = MutableStateFlow(AppScreen.LANDING)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // Active User State representation
    val currentUserState: StateFlow<UserEntity?> = repository.getUserFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Historical Logs
    val workouts: StateFlow<List<WorkoutEntity>> = repository.getAllWorkouts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val meals: StateFlow<List<DietPlanEntity>> = repository.getAllMeals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val progressLogs: StateFlow<List<ProgressLogEntity>> = repository.getProgressLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val payments: StateFlow<List<PaymentEntity>> = repository.getPayments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatHistory: StateFlow<List<ChatHistoryEntity>> = repository.getChatHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Chat Inputs
    private val _isAiGenerating = MutableStateFlow(false)
    val isAiGenerating: StateFlow<Boolean> = _isAiGenerating.asStateFlow()

    // Active Fitness Screen State
    private val _timerSeconds = MutableStateFlow(0)
    val timerSeconds: StateFlow<Int> = _timerSeconds.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private var timerJob: Job? = null

    // Water Intake Realtime Value (for immediate local updates in Dashboard, synced)
    private val _waterDrankMlToday = MutableStateFlow(750)
    val waterDrankMlToday: StateFlow<Int> = _waterDrankMlToday.asStateFlow()

    // Calories Burned Realtime Value (for direct logs, synced on save)
    private val _caloriesBurnedToday = MutableStateFlow(320)
    val caloriesBurnedToday: StateFlow<Int> = _caloriesBurnedToday.asStateFlow()

    // Active Stripe Payment State (e.g. animated spinner indicator)
    private val _stripeStatus = MutableStateFlow<String?>(null)
    val stripeStatus: StateFlow<String?> = _stripeStatus.asStateFlow()

    // Dynamic AI Suggestions generated dynamically on profile metrics
    private val _aiRecommendation = MutableStateFlow("Initializing quantum biomonitoring node...")
    val aiRecommendation: StateFlow<String> = _aiRecommendation.asStateFlow()

    // Admin Dashboard live counter
    val adminNodeCallCount = MutableStateFlow(42)

    init {
        // Ensure user database defaults and seeds are loaded automatically
        viewModelScope.launch(Dispatchers.IO) {
            setupDefaultUserAndSeedData()
            triggerAiRecommendationGeneration()
        }
    }

    private suspend fun setupDefaultUserAndSeedData() {
        val existingUser = repository.getUserSync()
        if (existingUser == null) {
            // Seed User
            val defaultUser = UserEntity(
                id = "user_current",
                name = "Zenith Cipher",
                email = "janmayjaysinghrathor786@gmail.com",
                subscriptionRank = "ORIGIN"
            )
            repository.insertUser(defaultUser)

            // Seed Workouts (For charts/history on dashboard)
            val now = System.currentTimeMillis()
            val dayMs = 24 * 60 * 60 * 1000L
            repository.insertWorkout(WorkoutEntity(type = "Gym Mode", durationMin = 45, caloriesBurned = 410, exercisesCsv = "Lat Pulldown x12, Dumbbell Row x10, Bench Press x15", dateLong = now - 2 * dayMs, difficulty = "Intermediate"))
            repository.insertWorkout(WorkoutEntity(type = "Home Workout", durationMin = 30, caloriesBurned = 280, exercisesCsv = "Squats x20, Pushups x15, Plank 60s, Jump Lunges x12", dateLong = now - dayMs, difficulty = "Beginner"))
            repository.insertWorkout(WorkoutEntity(type = "Gym Mode", durationMin = 50, caloriesBurned = 520, exercisesCsv = "Deadlift x5, Leg Press x10, Weighted Dips x12", dateLong = now, difficulty = "Advanced"))

            // Seed Meals (For nutrition trackers)
            repository.insertMeal(DietPlanEntity(mealName = "Glitch Protobar & Coffee", mealType = "Breakfast", calories = 310, proteinG = 25, carbsG = 30, fatG = 8, dateLong = now - dayMs))
            repository.insertMeal(DietPlanEntity(mealName = "Quantum Chicken Rice Bowl", mealType = "Lunch", calories = 650, proteinG = 45, carbsG = 70, fatG = 12, dateLong = now - dayMs))
            repository.insertMeal(DietPlanEntity(mealName = "Neon Salmon Skewer", mealType = "Dinner", calories = 550, proteinG = 38, carbsG = 15, fatG = 22, dateLong = now - dayMs))

            // Seed Progress Log
            repository.insertProgressLog(ProgressLogEntity(dateLong = now - 3 * dayMs, weightKg = 73f, waterDrankMl = 2000, caloriesBurnedToday = 300, activeMinutesToday = 25, healthScore = 72))
            repository.insertProgressLog(ProgressLogEntity(dateLong = now - 2 * dayMs, weightKg = 72.5f, waterDrankMl = 2250, caloriesBurnedToday = 410, activeMinutesToday = 45, healthScore = 75))
            repository.insertProgressLog(ProgressLogEntity(dateLong = now - dayMs, weightKg = 72.1f, waterDrankMl = 2500, caloriesBurnedToday = 280, activeMinutesToday = 30, healthScore = 78))

            // Seed Payments
            repository.insertPayment(PaymentEntity(amountPaidCents = 1400L, planName = "NEXUS DEBUT", status = "COMPLETED", transactionId = "TXN_SYNAPSE_88921", dateLong = now - 30 * dayMs))

            // Seed Initial Chat welcome message
            repository.insertChatMessage(ChatHistoryEntity(
                text = "SYSTEM BOOTSTRAP COMPLETE. I am ZYRATHON-X AI assistant core. What biometric optimizations shall we review today, User Zenith?",
                isAi = true,
                timestampLong = System.currentTimeMillis()
            ))
        }
    }

    fun navigateTo(screen: AppScreen) {
        viewModelScope.launch {
            _currentScreen.value = screen
        }
    }

    // Auth Flows
    fun loginWithCredentials(emailText: String, nameText: String): Boolean {
        if (emailText.isBlank() || nameText.isBlank()) return false
        viewModelScope.launch(Dispatchers.IO) {
            val existing = repository.getUserSync()
            val finalUser = existing?.copy(name = nameText, email = emailText) ?: UserEntity(
                name = nameText,
                email = emailText,
                subscriptionRank = "ORIGIN"
            )
            repository.insertUser(finalUser)
            _isLoggedIn.value = true
            _currentScreen.value = AppScreen.DASHBOARD
            triggerAiRecommendationGeneration()
        }
        return true
    }

    fun logout() {
        _isLoggedIn.value = false
        _currentScreen.value = AppScreen.LANDING
    }

    // Health score updater
    fun calculateHealthScore(bmi: Float, mealsRecorded: Int, workoutsRecorded: Int): Int {
        var score = 60
        if (bmi in 18.5f..24.9f) score += 20
        else score += 10

        score += (mealsRecorded * 3).coerceAtMost(10)
        score += (workoutsRecorded * 5).coerceAtMost(10)
        return score.coerceAtMost(100)
    }

    // Trigger dynamic recommendation based on health characteristics
    fun triggerAiRecommendationGeneration() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = repository.getUserSync() ?: return@launch
            val mealsList = db.fitnessDao().getAllMeals()
            val workoutList = db.fitnessDao().getAllWorkouts()

            val bmi = user.weightKg / ((user.heightCm / 100f) * (user.heightCm / 100f))
            val currentRank = user.subscriptionRank

            val prompt = """
                Biometric stats for ${user.name}:
                Height: ${user.heightCm}cm, Weight: ${user.weightKg}kg (BMI: ${"%.1f".format(bmi)}).
                Subscription Core Rank: $currentRank.
                Goal daily: ${user.dailyCalorieGoal} kcal.
                Generate a 2-sentence cyberpunk tactical biomonitoring fitness & bio-hack summary, pointing out how their current metrics match.
            """.trimIndent()

            val generated = runGeminiQueryInternal(prompt)
            _aiRecommendation.value = generated
        }
    }

    // Interactive trackers
    fun incrementWater(amountMl: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _waterDrankMlToday.value = (_waterDrankMlToday.value + amountMl).coerceAtLeast(0)
            saveMetricsToProgressDB()
        }
    }

    fun logActiveCalories(amountKcal: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _caloriesBurnedToday.value = (_caloriesBurnedToday.value + amountKcal).coerceAtLeast(0)
            saveMetricsToProgressDB()
        }
    }

    private suspend fun saveMetricsToProgressDB() {
        val now = System.currentTimeMillis()
        val user = repository.getUserSync() ?: return
        val bmi = user.weightKg / ((user.heightCm / 100f) * (user.heightCm / 100f))
        val finalHealthScore = calculateHealthScore(bmi, 3, 3)

        val log = ProgressLogEntity(
            dateLong = now,
            weightKg = user.weightKg,
            waterDrankMl = _waterDrankMlToday.value,
            caloriesBurnedToday = _caloriesBurnedToday.value,
            activeMinutesToday = (_caloriesBurnedToday.value / 8), // simplified ratio
            healthScore = finalHealthScore
        )
        repository.insertProgressLog(log)
    }

    // Nutrition tracker saves
    fun logMeal(name: String, type: String, kcalToLog: Int, carbs: Int, protein: Int, fat: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val entry = DietPlanEntity(
                mealName = name,
                mealType = type,
                calories = kcalToLog,
                carbsG = carbs,
                proteinG = protein,
                fatG = fat,
                dateLong = System.currentTimeMillis()
            )
            repository.insertMeal(entry)
            triggerAiRecommendationGeneration()
        }
    }

    // Workout Tracker additions
    fun completeWorkout(type: String, durationMin: Int, difficulty: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val cals = durationMin * if (type == "Gym Mode") 10 else 7
            val exerciseMapping = if (type == "Gym Mode") {
                "Bench Press 3x12, Leg Press 3x15, Cable Pulls 4x10"
            } else {
                "Jumping Jacks x50, Air Squats 4x20, Pushups 4x15, Bicycle Crunches x40"
            }

            val entry = WorkoutEntity(
                type = type,
                durationMin = durationMin,
                caloriesBurned = cals,
                exercisesCsv = exerciseMapping,
                dateLong = System.currentTimeMillis(),
                difficulty = difficulty
            )
            repository.insertWorkout(entry)
            logActiveCalories(cals)
            triggerAiRecommendationGeneration()
        }
    }

    // Timer functions
    fun startTimer() {
        if (_isTimerRunning.value) return
        _isTimerRunning.value = true
        timerJob = viewModelScope.launch(Dispatchers.Default) {
            while (_isTimerRunning.value) {
                delay(1000L)
                _timerSeconds.value++
            }
        }
    }

    fun pauseTimer() {
        _isTimerRunning.value = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        _isTimerRunning.value = false
        timerJob?.cancel()
        _timerSeconds.value = 0
    }

    // Subscriptions upgrade processing (Stripe simulation animation lock)
    fun processStripeUpgrade(rankName: String, priceDollars: Int) {
        viewModelScope.launch {
            _stripeStatus.value = "CONNECTING SECURE GATEWAY..."
            delay(1200L)
            _stripeStatus.value = "VERIFYING STRIPE CONSOLE NODES..."
            delay(1200L)
            _stripeStatus.value = "UPGRADING SECURE SAAS MATRIX..."
            delay(1200L)

            withContext(Dispatchers.IO) {
                val user = repository.getUserSync()
                if (user != null) {
                    val upgraded = user.copy(subscriptionRank = rankName)
                    repository.insertUser(upgraded)

                    val paymentReceipt = PaymentEntity(
                        amountPaidCents = priceDollars * 100L,
                        planName = rankName,
                        status = "COMPLETED",
                        transactionId = "STRP_TX_${UUID.randomUUID().toString().take(12).uppercase()}",
                        dateLong = System.currentTimeMillis()
                    )
                    repository.insertPayment(paymentReceipt)
                }

                // Add nice AI automated upgrade confirmation response
                repository.insertChatMessage(ChatHistoryEntity(
                    text = "TRANSACTION CONFIRMED. Neural Core unlocked to high-tier [ $rankName ] metrics. Dynamic bio-scans initialized.",
                    isAi = true,
                    timestampLong = System.currentTimeMillis()
                ))
            }
            _stripeStatus.value = "UPGRADE_SUCCESS"
            delay(1000L)
            _stripeStatus.value = null
            navigateTo(AppScreen.DASHBOARD)
            triggerAiRecommendationGeneration()
        }
    }

    // Admin dashboard functions (promoting user plans directly)
    fun adminModifyUserSubscription(newPlan: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = repository.getUserSync()
            if (user != null) {
                val modified = user.copy(subscriptionRank = newPlan)
                repository.insertUser(modified)
                triggerAiRecommendationGeneration()
                adminNodeCallCount.value += 12
            }
        }
    }

    // AI Conversational Chat Messaging functions
    fun sendUserChatMessage(prompt: String) {
        if (prompt.isBlank()) return
        viewModelScope.launch {
            val userMsg = ChatHistoryEntity(text = prompt, isAi = false, timestampLong = System.currentTimeMillis())
            withContext(Dispatchers.IO) {
                repository.insertChatMessage(userMsg)
            }
            _isAiGenerating.value = true

            // Formulate full contextual prompts compiling recent histories
            val systemContext = """
                You are ZYRATHON-X, a futuristic level 5 luxury AI biometric bio-coach, nutritionist, and health mentor assistant core. 
                Maintain a professional, energetic, futuristic, SaaS advisor persona. Use cyberpunk aesthetic terms (e.g. bio-hacks, matrix levels). 
                Respond concisely in 2 high-value bullet points or a short, sharp paragraph. Keep instructions tactical, real-world, and healthy.
            """.trimIndent()

            withContext(Dispatchers.IO) {
                val fullConversation = db.fitnessDao().getUserSync()?.let { user ->
                    "User: ${user.name} with Rank: ${user.subscriptionRank} says: $prompt"
                } ?: prompt

                val finalAiResponse = runGeminiQueryInternal(fullConversation, systemContext)
                repository.insertChatMessage(
                    ChatHistoryEntity(
                        text = finalAiResponse,
                        isAi = true,
                        timestampLong = System.currentTimeMillis()
                    )
                )
                adminNodeCallCount.value++
            }
            _isAiGenerating.value = false
        }
    }

    private suspend fun runGeminiQueryInternal(prompt: String, systemInstruction: String? = null): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            // High quality offline fallback matrix when developers compile without configured key
            delay(1000L) // simulation transition
            return simulateBeautifulOfflineResponse(prompt)
        }

        return try {
            val contents = listOf(
                ApiContent(parts = listOf(ApiPart(text = prompt)))
            )
            val systInstruction = systemInstruction?.let {
                ApiContent(parts = listOf(ApiPart(text = it)))
            }
            val request = GenerateContentRequest(
                contents = contents,
                systemInstruction = systInstruction
            )
            val serviceResult = RetrofitClient.service.generateContent(apiKey, request)
            serviceResult.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "QUANTUM NODES SILENT. Re-engage biomonitor filters shortly."
        } catch (e: Exception) {
            "BIO-SCANNING ERR: ${e.message}. Dynamic simulation core active."
        }
    }

    private fun simulateBeautifulOfflineResponse(prompt: String): String {
        val promptClean = prompt.lowercase()
        return when {
            promptClean.contains("workout") || promptClean.contains("exercise") || promptClean.contains("fit") -> {
                "• **Zyrathon Neural Advice**: Logged workouts reveal a muscular gap. Recommendation: Integrate a **Carbon 3-day split** highlighting squats, lat pull-downs, and weighted lunges.\n" +
                        "• **Bio-hack tip**: Maintain a 45-second high-intensity interval pace to optimize cardiovascular cellular replication metrics."
            }
            promptClean.contains("diet") || promptClean.contains("meal") || promptClean.contains("protein") || promptClean.contains("calorie") -> {
                "• **Zyrathon Nutrition Matrix**: Maximize proteins to safeguard muscle preservation. Targeted goal: **2.2g of protein per kg of bio-mass**.\n" +
                        "• **Tactical Nutrients**: Incorporate omega-rich lipids and leafy bio-carbs to keep neural synapses fire rate steady."
            }
            promptClean.contains("motivation") || promptClean.contains("coach") || promptClean.contains("help") -> {
                "**BIO-COACH SYNAPSE UPDATE**: Your bio-structure shows supreme latency potential. Do not let minor muscle breakdowns disrupt long-term quantum kinetic consistency. Elevate your node today, Zenith!"
            }
            else -> {
                "• **Quantum Biometrics Loaded**: Zyrathon-X has synced with your biometric signature. We advise keeping hydration flow above 2,500ml daily.\n" +
                        "• **Neural Node online**: Formulated on standard health algorithms. Level up your SaaS subscription tier in our portal for deeper neural analytics."
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearChatHistory()
        }
    }
}
