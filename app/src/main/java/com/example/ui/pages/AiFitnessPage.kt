package com.example.ui.pages

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CyberButton
import com.example.ui.components.GlowCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberMagenta
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite
import com.example.viewmodel.ZyrathonViewModel

@Composable
fun AiFitnessPage(viewModel: ZyrathonViewModel) {
    val scrollState = rememberScrollState()

    // Timer States
    val timerSecs by viewModel.timerSeconds.collectAsStateWithLifecycle()
    val isRunning by viewModel.isTimerRunning.collectAsStateWithLifecycle()

    var activeMode by remember { mutableStateOf("Gym Mode") }
    var selectedDifficulty by remember { mutableStateOf("Intermediate") }
    val isSavedAlertShow = remember { mutableStateOf(false) }

    // Format MM:SS
    val minutes = timerSecs / 60
    val seconds = timerSecs % 60
    val formattedTimer = "%02d:%02d".format(minutes, seconds)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "NEURAL COACH NOOSPHERE",
                color = TextWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.testTag("fitness_core_title")
            )
            Text(
                text = "CALIBRATE WORKOUT COEFFICIENTS IN REALTIME",
                color = NeonBlue,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Bio-fitness image banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, androidx.compose.ui.graphics.Brush.linearGradient(listOf(NeonBlue, NeonPurple)), RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_bio_fitness),
                    contentDescription = "Biometric scanning tracker",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
                // Label overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(androidx.compose.ui.graphics.Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)))),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = "VIRTUAL_PHYSIQUE // STREAMINGING_LOGS",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonBlue,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dynamic Active Stopwatch
            GlowCard(borderColor = if (isRunning) NeonBlue else NeonPurple) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "INTERVAL TIMER CONTROLS",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextGray
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Timer Counter text display
                    Text(
                        text = formattedTimer,
                        fontSize = 58.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = if (isRunning) NeonBlue else Color.White,
                        modifier = Modifier.testTag("workout_stopwatch")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Core controls Row
                    Row {
                        if (isRunning) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF2B0E18))
                                    .border(1.dp, CyberMagenta, RoundedCornerShape(6.dp))
                                    .clickable { viewModel.pauseTimer() }
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                                    .testTag("pause_timer_btn"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Pause, contentDescription = null, tint = CyberMagenta)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = "PAUSE CHRONO", color = CyberMagenta, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF0E1A2B))
                                    .border(1.dp, NeonBlue, RoundedCornerShape(6.dp))
                                    .clickable { viewModel.startTimer() }
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                                    .testTag("start_timer_btn"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = NeonBlue)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = "INITIALIZE TRON CHRONO", color = NeonBlue, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Reset button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF13131F))
                                .border(1.dp, Color(0xFF28283E), RoundedCornerShape(6.dp))
                                .clickable { viewModel.resetTimer() }
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                                .testTag("reset_timer_btn"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color.LightGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Finalize Session Submit
                    CyberButton(
                        text = "COMMIT WORKOUT METRICS TO SQLITE",
                        onClick = {
                            if (timerSecs < 10) {
                                // Simulate at least 10s or just pass standard duration
                            }
                            // Save workout
                            val loggedMinutes = if (timerSecs > 0) (timerSecs / 60).coerceAtLeast(1) else 30
                            viewModel.completeWorkout(activeMode, loggedMinutes, selectedDifficulty)
                            viewModel.resetTimer()
                            isSavedAlertShow.value = true
                        },
                        glowColor = NeonPurple,
                        modifier = Modifier.fillMaxWidth(0.9f),
                        testTagStr = "save_workout_btn"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sub Mode Switches
            SectionHeader(title = "Split Blueprint Matrix", accentColor = NeonBlue)

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (activeMode == "Gym Mode") Color(0xFF161626) else Color(0xFF0C0C14))
                        .border(1.dp, if (activeMode == "Gym Mode") NeonBlue else Color(0xFF242435), RoundedCornerShape(6.dp))
                        .clickable { activeMode = "Gym Mode" }
                        .padding(12.dp)
                        .testTag("mode_gym_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.FitnessCenter, contentDescription = null, tint = if (activeMode == "Gym Mode") NeonBlue else TextGray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "GYM MODE HEAVY ARRAYS", color = if (activeMode == "Gym Mode") Color.White else TextGray, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (activeMode == "Home Workout") Color(0xFF161626) else Color(0xFF0C0C14))
                        .border(1.dp, if (activeMode == "Home Workout") NeonBlue else Color(0xFF242435), RoundedCornerShape(6.dp))
                        .clickable { activeMode = "Home Workout" }
                        .padding(12.dp)
                        .testTag("mode_home_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = null, tint = if (activeMode == "Home Workout") NeonBlue else TextGray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "TACTICAL CALISTHENICS", color = if (activeMode == "Home Workout") Color.White else TextGray, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Difficulty selections
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "BIO-LOAD INTENSITY:", color = TextGray, fontSize = 11.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f))
                DifficultySelectorBadge("BEGINNER", selectedDifficulty == "Beginner") { selectedDifficulty = "Beginner" }
                Spacer(modifier = Modifier.width(4.dp))
                DifficultySelectorBadge("INTERMEDIATE", selectedDifficulty == "Intermediate") { selectedDifficulty = "Intermediate" }
                Spacer(modifier = Modifier.width(4.dp))
                DifficultySelectorBadge("ADVANCED", selectedDifficulty == "Advanced") { selectedDifficulty = "Advanced" }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Split lists
            SectionHeader(title = "Selected Active Exercises", accentColor = NeonPurple)

            Spacer(modifier = Modifier.height(12.dp))

            if (activeMode == "Gym Mode") {
                StaticExerciseItem("• DEADLIFT SPECTRUM", "4 Sets x 5 Reps (85% Core Capacity Load)")
                Spacer(modifier = Modifier.height(8.dp))
                StaticExerciseItem("• BULGARIAN SYNAPSE SPLIT SQUATS", "3 Sets x 12 Reps per biomass node")
                Spacer(modifier = Modifier.height(8.dp))
                StaticExerciseItem("• BENCH PRESS ALIGNMENT ARRAYS", "4 Sets x 12 Reps (glowing bar alignment)")
                Spacer(modifier = Modifier.height(8.dp))
                StaticExerciseItem("• HIGH-PULL LAT INTEGRATION", "3 Sets x 15 Reps (muscle fiber activation)")
            } else {
                StaticExerciseItem("• SECURE BODY SQUAT PULSES", "4 Sets x 25 Reps (kinetic speed focus)")
                Spacer(modifier = Modifier.height(8.dp))
                StaticExerciseItem("• DEPTH PUSHUP COMPRESSIONS", "4 Sets x 15 Reps (elbow joint protection)")
                Spacer(modifier = Modifier.height(8.dp))
                StaticExerciseItem("• PLANK CHRONO RIG", "3 Reps x 60 Seconds lock compression")
                Spacer(modifier = Modifier.height(8.dp))
                StaticExerciseItem("• CYCLIC MOUNTAIN SPRINT CLIMBS", "3 Sets x 40 Reps (cardio dynamic burn)")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Confirmation alert dialog
            if (isSavedAlertShow.value) {
                AlertDialog(
                    onDismissRequest = { isSavedAlertShow.value = false },
                    confirmButton = {
                        TextButton(onClick = { isSavedAlertShow.value = false }) {
                            Text(text = "SECURE LOGS SUCCESSFULLY UPDATED", color = NeonBlue, fontFamily = FontFamily.Monospace)
                        }
                    },
                    title = {
                        Text(text = "COMMITTED TO DATABASE", color = Color.White, fontFamily = FontFamily.Monospace)
                    },
                    text = {
                        Text(text = "Your completed physical exercise duration and calories have been logged to the Room data tables.", color = TextGray)
                    },
                    containerColor = Color(0xFF10101C),
                    tonalElevation = 6.dp
                )
            }
        }
    }
}

@Composable
fun DifficultySelectorBadge(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(if (isSelected) NeonPurple.copy(alpha = 0.2f) else Color.Transparent)
            .border(1.dp, if (isSelected) NeonPurple else Color(0xFF242435), RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else TextGray,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun StaticExerciseItem(title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0F0F1A), RoundedCornerShape(6.dp))
            .border(1.dp, Color(0xFF1F1F35), RoundedCornerShape(6.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = title, color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            Text(text = desc, color = TextGray, fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
        }
    }
}
