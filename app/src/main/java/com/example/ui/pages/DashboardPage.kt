package com.example.ui.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.GlowCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberMagenta
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite
import com.example.viewmodel.AppScreen
import com.example.viewmodel.ZyrathonViewModel

@Composable
fun DashboardPage(viewModel: ZyrathonViewModel) {
    val scrollState = rememberScrollState()

    // Collect View States
    val user by viewModel.currentUserState.collectAsStateWithLifecycle()
    val workouts by viewModel.workouts.collectAsStateWithLifecycle()
    val meals by viewModel.meals.collectAsStateWithLifecycle()
    val waterToday by viewModel.waterDrankMlToday.collectAsStateWithLifecycle()
    val caloriesToday by viewModel.caloriesBurnedToday.collectAsStateWithLifecycle()
    val aiInsight by viewModel.aiRecommendation.collectAsStateWithLifecycle()

    // Calculated fields based on user entity attributes
    val weight = user?.weightKg ?: 70f
    val height = user?.heightCm ?: 175f
    val bmiValue = weight / ((height / 100f) * (height / 100f))
    val computedScore = viewModel.calculateHealthScore(bmiValue, meals.size, workouts.size)

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

            // User Welcome Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Small premium corporate logo
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.5.dp, NeonBlue, RoundedCornerShape(8.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_zyrathon_logo),
                        contentDescription = "Zyrathon Mini Logo",
                        modifier = Modifier.fillMaxSize().padding(1.dp),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "WELCOME BACK,",
                        color = NeonBlue,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = (user?.name ?: "ZENITH CIPHER").uppercase(),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.testTag("user_dashboard_title")
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Ribbon matching tier level
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(NeonPurple, CyberMagenta)
                            )
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (user?.subscriptionRank ?: "ORIGIN").uppercase(),
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp,
                        modifier = Modifier.testTag("tier_badge")
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Health Score dial gauge
            GlowCard(borderColor = NeonPurple) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Meter graphic
                    Box(
                        modifier = Modifier.size(105.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val animatedAngle by animateFloatAsState(
                            targetValue = (computedScore / 100f) * 270f,
                            animationSpec = tween(1200),
                            label = "dial"
                        )
                        Canvas(modifier = Modifier.size(90.dp)) {
                            // Backing Track arc
                            drawArc(
                                color = Color(0xFF1E1E2F),
                                startAngle = 135f,
                                sweepAngle = 270f,
                                useCenter = false,
                                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                            )
                            // Core score arc
                            drawArc(
                                brush = Brush.linearGradient(colors = listOf(NeonBlue, NeonPurple)),
                                startAngle = 135f,
                                sweepAngle = animatedAngle,
                                useCenter = false,
                                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$computedScore",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "HEALTH CO",
                                fontSize = 8.sp,
                                color = TextGray,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "QUANTUM BIO-METRICS SCORE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = NeonBlue
                        )
                        Text(
                            text = "Based on daily tracker variables, structural BMI ratio, physical calorie output volume, and nutrient integrity records.",
                            fontSize = 12.sp,
                            color = TextGray,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Gemini Dynamic Insights Box
            SectionHeader(title = "Gemini Bio-Telemetry Insights", accentColor = NeonBlue)

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0C0C14), RoundedCornerShape(10.dp))
                    .border(1.dp, NeonBlue.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "gemini",
                        tint = NeonBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = aiInsight,
                        color = Color.LightGray,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        modifier = Modifier.testTag("ai_insight_text")
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Daily Tracking sliders
            SectionHeader(title = "Biomonitor Logs Today", accentColor = CyberMagenta)

            Spacer(modifier = Modifier.height(12.dp))

            // Hydration tracker
            GlowCard(borderColor = NeonBlue) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LocalDrink, contentDescription = null, tint = NeonBlue)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "HYDRATION CORE",
                                color = TextWhite,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "$waterToday / 2500 ml",
                            color = NeonBlue,
                            fontSize = 18.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp, start = 32.dp)
                        )
                    }

                    // Increments actions
                    Row {
                        QuickMetricLogBtn("+100ml Espresso", onClick = { viewModel.incrementWater(100) })
                        Spacer(modifier = Modifier.width(4.dp))
                        QuickMetricLogBtn("+250ml Hydro Drop", onClick = { viewModel.incrementWater(250) })
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Calorie burns tracker
            GlowCard(borderColor = NeonOrange) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Whatshot, contentDescription = null, tint = NeonOrange)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "KINETIC HEAT (BURNS)",
                                color = TextWhite,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "$caloriesToday kcal today",
                            color = NeonOrange,
                            fontSize = 18.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp, start = 32.dp)
                        )
                    }

                    Row {
                        QuickMetricLogBtn("+150 Kcal", onClick = { viewModel.logActiveCalories(150) })
                        Spacer(modifier = Modifier.width(4.dp))
                        QuickMetricLogBtn("+300 Kcal", onClick = { viewModel.logActiveCalories(300) })
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // History indicators
            SectionHeader(title = "Historic Metrics Nodes", accentColor = NeonPurple)

            Spacer(modifier = Modifier.height(12.dp))

            if (workouts.isEmpty()) {
                Text(text = "No workouts recorded yet.", color = TextGray, fontSize = 13.sp)
            } else {
                workouts.take(3).forEach { workout ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .background(Color(0xFF0B0B12), RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFF1B1B2C), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = workout.type.uppercase(),
                                    color = TextWhite,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "${workout.durationMin} MINS • ${workout.difficulty.uppercase()}",
                                    color = TextGray,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }

                            Text(
                                text = "-${workout.caloriesBurned} Kcal",
                                color = NeonOrange,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun QuickMetricLogBtn(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(Color(0xFF12121E), RoundedCornerShape(6.dp))
            .border(1.dp, Color(0xFF28283E), RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = TextWhite,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}
