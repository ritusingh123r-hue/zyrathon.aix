package com.example.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberTextField
import com.example.ui.components.GlowCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberMagenta
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite
import com.example.viewmodel.ZyrathonViewModel

@Composable
fun AiNutritionPage(viewModel: ZyrathonViewModel) {
    val scrollState = rememberScrollState()

    // Collect values
    val loggedMeals by viewModel.meals.collectAsStateWithLifecycle()

    // Slider inputs for instant live BMI calculating
    var sliderHeightCm by remember { mutableFloatStateOf(175f) }
    var sliderWeightKg by remember { mutableFloatStateOf(70f) }

    // Log meal inputs
    var mealTitleInput by remember { mutableStateOf("Glitch Protein Shake") }
    var mealKcalInput by remember { mutableStateOf("450") }
    var mealCarbInput by remember { mutableStateOf("30") }
    var mealProteinInput by remember { mutableStateOf("40") }
    var mealFatInput by remember { mutableStateOf("10") }

    val isMealSavedAlertShow = remember { mutableStateOf(false) }

    // Instant derived BMI calculation
    val heightInMeters = sliderHeightCm / 100f
    val computedBmi = sliderWeightKg / (heightInMeters * heightInMeters)

    val bmiCategory = when {
        computedBmi < 18.5f -> "UNDERWEIGHT (SYNAPSE CALORIC GAIN INDICATED)"
        computedBmi < 25.0f -> "HEALTHY NOMINAL COEFFICIENT"
        computedBmi < 30.0f -> "OVERWEIGHT (SYSTEM DEFICIT BALANCING ADVISED)"
        else -> "OBESE NODE STATUS"
    }

    val bmiColor = when {
        computedBmi < 18.5f -> NeonOrange
        computedBmi < 25.0f -> CyberGreen
        computedBmi < 30.0f -> NeonPurple
        else -> CyberMagenta
    }

    // Dynamic macros totals logged in database
    val totalProteinLogged = loggedMeals.sumOf { it.proteinG }
    val totalCarbLogged = loggedMeals.sumOf { it.carbsG }
    val totalFatLogged = loggedMeals.sumOf { it.fatG }

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
                text = "NUTRITION ANALYTICS NODE",
                color = TextWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.testTag("nutrition_analytics_title")
            )
            Text(
                text = "QUANTIFYING DISCRETE MACRO AND BIOMASS COEFFICIENTS",
                color = NeonBlue,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Macro/Biomass nutrition image banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, Brush.linearGradient(listOf(CyberMagenta, NeonPurple)), RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_matrix),
                    contentDescription = "Nutrition macromolecule matrix scanning",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
                // Label overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)))),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = "MACRONUTRITIONAL_INDEX // LIVE_SCAN",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberMagenta,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dynamic live BMI calculations GlowCard
            SectionHeader(title = "Biomass Scale (BMI Rig)", accentColor = NeonBlue)

            Spacer(modifier = Modifier.height(12.dp))

            GlowCard(borderColor = bmiColor) {
                Text(
                    text = "REALTIME BIOMASS INDEX CHECK",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = bmiColor
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Height Slider
                Text(
                    text = "BIOMASS VERTICAL HEIGHT: ${sliderHeightCm.toInt()} CM",
                    color = TextWhite,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace
                )
                Slider(
                    value = sliderHeightCm,
                    onValueChange = { sliderHeightCm = it },
                    valueRange = 120f..220f,
                    colors = SliderDefaults.colors(
                        thumbColor = NeonBlue,
                        activeTrackColor = NeonBlue,
                        inactiveTrackColor = Color(0xFF1E1E2C)
                    ),
                    modifier = Modifier.testTag("height_slider")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Weight Slider
                Text(
                    text = "BIOMASS HORIZONTAL MASS: ${sliderWeightKg.toInt()} KG",
                    color = TextWhite,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace
                )
                Slider(
                    value = sliderWeightKg,
                    onValueChange = { sliderWeightKg = it },
                    valueRange = 40f..150f,
                    colors = SliderDefaults.colors(
                        thumbColor = CyberMagenta,
                        activeTrackColor = CyberMagenta,
                        inactiveTrackColor = Color(0xFF1E1E2C)
                    ),
                    modifier = Modifier.testTag("weight_slider")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Display BMI indices
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "COMPUTED NODE REGISTER",
                            color = TextGray,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "BMI Index: ${"%.1f".format(computedBmi)}",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.testTag("computed_bmi_readout")
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(bmiColor.copy(alpha = 0.15f))
                            .border(1.dp, bmiColor, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = bmiCategory,
                            color = bmiColor,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Macronutrient tracker targets
            SectionHeader(title = "Daily Macro Balance Bars", accentColor = NeonPurple)

            Spacer(modifier = Modifier.height(12.dp))

            GlowCard(borderColor = NeonPurple) {
                // Protein progress bar
                MacroProgressTrackerRow("PRO (PROTEIN)", totalProteinLogged, 150, CyberGreen)
                Spacer(modifier = Modifier.height(12.dp))
                // Carbs progress bar
                MacroProgressTrackerRow("CHO (CARBS)", totalCarbLogged, 220, NeonBlue)
                Spacer(modifier = Modifier.height(12.dp))
                // Fats progress
                MacroProgressTrackerRow("LIP (LIPIDS/FAT)", totalFatLogged, 75, CyberMagenta)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Log Nutrients Input Box
            SectionHeader(title = "Log Biogenic Nourishment", accentColor = CyberMagenta)

            Spacer(modifier = Modifier.height(12.dp))

            GlowCard(borderColor = CyberMagenta) {
                Text(
                    text = "REGISTER CUSTOM NOURISHMENT SPLITS",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = TextGray,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                CyberTextField(
                    value = mealTitleInput,
                    onValueChange = { mealTitleInput = it },
                    label = "Nourish Title (Meal Name)",
                    leadingIcon = Icons.Default.RestaurantMenu,
                    testTagStr = "meal_name_input"
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(1f)) {
                        CyberTextField(
                            value = mealKcalInput,
                            onValueChange = { mealKcalInput = it },
                            label = "Calories (kcal)",
                            testTagStr = "meal_kcal_input"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        CyberTextField(
                            value = mealProteinInput,
                            onValueChange = { mealProteinInput = it },
                            label = "Protein (g)",
                            testTagStr = "meal_protein_input"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(1f)) {
                        CyberTextField(
                            value = mealCarbInput,
                            onValueChange = { mealCarbInput = it },
                            label = "Carbs (g)",
                            testTagStr = "meal_carbs_input"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        CyberTextField(
                            value = mealFatInput,
                            onValueChange = { mealFatInput = it },
                            label = "Lipids/Fat (g)",
                            testTagStr = "meal_fat_input"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                CyberButton(
                    text = "Add Nutrient Allocation to Database",
                    onClick = {
                        val kcalVal = mealKcalInput.toIntOrNull() ?: 200
                        val proteinVal = mealProteinInput.toIntOrNull() ?: 20
                        val carbVal = mealCarbInput.toIntOrNull() ?: 25
                        val fatVal = mealFatInput.toIntOrNull() ?: 5

                        if (mealTitleInput.isNotBlank()) {
                            viewModel.logMeal(mealTitleInput, "Snack", kcalVal, carbVal, proteinVal, fatVal)
                            isMealSavedAlertShow.value = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = CyberMagenta,
                    testTagStr = "submit_nutrient_btn"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Historical nutrition logs
            SectionHeader(title = "Nourish History Nodes", accentColor = CyberGreen)

            Spacer(modifier = Modifier.height(12.dp))

            if (loggedMeals.isEmpty()) {
                Text(text = "No nourishment logs exist in Room database.", color = TextGray, fontSize = 13.sp)
            } else {
                loggedMeals.take(3).forEach { meal ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .background(Color(0xFF0F0F1A), RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFF1F1F35), RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = meal.mealName.uppercase(), color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            Text(
                                text = "PRO: ${meal.proteinG}G • CHO: ${meal.carbsG}G • LIP: ${meal.fatG}G",
                                color = TextGray,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Text(
                            text = "+${meal.calories} kcal",
                            color = CyberGreen,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            // Success alert
            if (isMealSavedAlertShow.value) {
                AlertDialog(
                    onDismissRequest = { isMealSavedAlertShow.value = false },
                    confirmButton = {
                        TextButton(onClick = { isMealSavedAlertShow.value = false }) {
                            Text(text = "SAAS NOURISH CONFIRMED", color = NeonBlue, fontFamily = FontFamily.Monospace)
                        }
                    },
                    title = {
                        Text(text = "ALLOCATION LOGGED", color = Color.White, fontFamily = FontFamily.Monospace)
                    },
                    text = {
                        Text(text = "Nourishment macro variables successfully registered and updated in physical parameters.", color = TextGray)
                    },
                    containerColor = Color(0xFF10101C),
                    tonalElevation = 6.dp
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun MacroProgressTrackerRow(label: String, currentValue: Int, targetValue: Int, barColor: Color) {
    val progressFraction = if (targetValue > 0) (currentValue.toFloat() / targetValue.toFloat()).coerceAtMost(1f) else 0f
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = TextWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "$currentValue / $targetValue g",
                color = barColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = progressFraction,
            color = barColor,
            trackColor = Color(0xFF1B1B2A),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}
