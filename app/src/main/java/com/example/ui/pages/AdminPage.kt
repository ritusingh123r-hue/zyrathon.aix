package com.example.ui.pages

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CyberButton
import com.example.ui.components.GlowCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberMagenta
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextGray
import com.example.ui.theme.TextWhite
import com.example.viewmodel.ZyrathonViewModel

@Composable
fun AdminPage(viewModel: ZyrathonViewModel) {
    val scrollState = rememberScrollState()

    // Observe DB states
    val user by viewModel.currentUserState.collectAsStateWithLifecycle()
    val workouts by viewModel.workouts.collectAsStateWithLifecycle()
    val payments by viewModel.payments.collectAsStateWithLifecycle()
    val totalApiCallCount by viewModel.adminNodeCallCount.collectAsStateWithLifecycle()

    // Calculated fields
    val baseMRR = 1420L
    val aggregatePaymentsSum = payments.sumOf { it.amountPaidCents } / 100L
    val calculatedMRR = baseMRR + (workouts.size * 39L) + aggregatePaymentsSum

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
                text = "ADMINISTRATIVE ENGINE CONTROL",
                color = TextWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.testTag("admin_header")
            )
            Text(
                text = "SECURE BACKEND SAAS INTERCONNECTIVITY TELEMETRY",
                color = NeonBlue,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Dynamic analytical cards row
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    AdminAnalyticalCard(
                        title = "NODE MRR REVENUE",
                        value = "$$calculatedMRR USD",
                        glowColor = CyberGreen
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f)) {
                    AdminAnalyticalCard(
                        title = "ACTIVE COGNITIVE NODES",
                        value = "${24 + workouts.size} Nodes",
                        glowColor = NeonBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    AdminAnalyticalCard(
                        title = "GEMINI CORE CALLS",
                        value = "$totalApiCallCount queries",
                        glowColor = Color.Yellow
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f)) {
                    AdminAnalyticalCard(
                        title = "MEMBERS COUNT",
                        value = "1 Operator",
                        glowColor = NeonPurple
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Graphical MRR chart Canvas
            SectionHeader(title = "MRR SaaS Growth Curves", accentColor = NeonBlue)

            Spacer(modifier = Modifier.height(12.dp))

            GlowCard(borderColor = NeonBlue) {
                Text(
                    text = "REVENUE LOG INDEX PROGRESSION (Q1 2026)",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = NeonBlue,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Neon graph drawn directly to Canvas
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color(0xFF0C0C14))
                ) {
                    val points = listOf(
                        Pair(50f, 90f),
                        Pair(150f, 75f),
                        Pair(250f, 100f),
                        Pair(350f, 40f),
                        Pair(450f, 60f),
                        Pair(550f, 20f)
                    )

                    val path = Path().apply {
                        moveTo(points.first().first, points.first().second)
                        for (i in 1 until points.size) {
                            lineTo(points[i].first, points[i].second)
                        }
                    }

                    // Stroke Path
                    drawPath(
                        path = path,
                        color = NeonBlue,
                        style = Stroke(width = 4.dp.toPx())
                    )

                    // Glow effect
                    drawPath(
                        path = path,
                        color = NeonBlue.copy(alpha = 0.3f),
                        style = Stroke(width = 12.dp.toPx())
                    )

                    // Bottom horizontal lines
                    drawLine(
                        color = Color(0xFF1B1B2C),
                        start = androidx.compose.ui.geometry.Offset(0f, 110f),
                        end = androidx.compose.ui.geometry.Offset(size.width, 110f),
                        strokeWidth = 2f
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "JAN (1.2K)", fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = TextGray)
                    Text(text = "FEB (1.3K)", fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = TextGray)
                    Text(text = "MAR (1.4K)", fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = TextGray)
                    Text(text = "APR (1.45K)", fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = TextGray)
                    Text(text = "MAY (CURRENT)", fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = TextGray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // User list profiles modify controls
            SectionHeader(title = "Member Directory Node Administration", accentColor = NeonPurple)

            Spacer(modifier = Modifier.height(14.dp))

            user?.let { actualUser ->
                GlowCard(borderColor = NeonPurple) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.SupervisorAccount,
                                    contentDescription = null,
                                    tint = NeonPurple,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = actualUser.name.uppercase(),
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Text(
                                text = actualUser.email,
                                color = TextGray,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            Text(
                                text = "RANK: ${actualUser.subscriptionRank}",
                                color = Color.Yellow,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        // Instant admin tier modifies triggers
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "MODIFY LEVEL:",
                                color = TextGray,
                                fontSize = 8.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            Row {
                                AdminLevelBtn("FREE", actualUser.subscriptionRank == "ORIGIN") {
                                    viewModel.adminModifyUserSubscription("ORIGIN")
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                AdminLevelBtn("NEXUS", actualUser.subscriptionRank == "NEXUS") {
                                    viewModel.adminModifyUserSubscription("NEXUS")
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                AdminLevelBtn("TITAN-X", actualUser.subscriptionRank == "TITAN-X") {
                                    viewModel.adminModifyUserSubscription("TITAN-X")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun AdminAnalyticalCard(title: String, value: String, glowColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                Brush.linearGradient(listOf(glowColor.copy(alpha = 0.5f), Color.Transparent)),
                RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F0F1A))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                color = TextGray
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = Color.White
            )
        }
    }
}

@Composable
fun AdminLevelBtn(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(if (isSelected) NeonBlue else Color(0xFF131320))
            .border(1.dp, if (isSelected) Color.Cyan else Color(0xFF242435), RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.Black else Color.LightGray,
            fontSize = 8.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold
        )
    }
}
