package com.example.ui.pages

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberTextField
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
fun SubscriptionPage(viewModel: ZyrathonViewModel) {
    val scrollState = rememberScrollState()

    // Observe payment levels and status indicators
    val user by viewModel.currentUserState.collectAsStateWithLifecycle()
    val stripeStatusState by viewModel.stripeStatus.collectAsStateWithLifecycle()

    var activeTierSelection by remember { mutableStateOf<Pair<String, Int>?>(null) } // Plan name to Price

    // Modals parameters inputs
    var mockCardNo by remember { mutableStateOf("4242 4242 4242 4242") }
    var mockCardExp by remember { mutableStateOf("12/28") }
    var mockCardCvc by remember { mutableStateOf("999") }

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
                text = "SAAS SUBCRIPTION INTEGRATOR",
                color = TextWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.testTag("subscription_title")
            )
            Text(
                text = "DISPATCH AND ALLOCATE PREMIUM BIOMETRICAL ACCESS TO CENTRAL NODE",
                color = NeonBlue,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Current state banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0F0F1A))
                    .border(1.dp, NeonPurple, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = NeonPurple)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "ACTIVE SAAS ACCESS RANK LEVEL:",
                            color = TextGray,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "[ ${user?.subscriptionRank ?: "ORIGIN"} COGNITIVE TIER ]",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.testTag("user_active_tier")
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Animated grid selecting plans
            SectionHeader(title = "SaaS Nodes Options", accentColor = NeonBlue)

            Spacer(modifier = Modifier.height(14.dp))

            // ORIGIN Free Tier card
            SaaSTierCard(
                title = "ORIGIN COGNITIVE NODE",
                priceText = "FREE ACCESS",
                benefits = listOf("Limited basic metrics logs", "10 Daily general Gemini bio-chats", "Ad enabled interface grids"),
                actionText = if (user?.subscriptionRank == "ORIGIN") "CURRENTLY ENGAGED" else "ENGAGE BASIC NODE",
                glowColor = Color.Gray,
                onSelect = { activeTierSelection = null }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // NEXUS
            SaaSTierCard(
                title = "NEXUS CORE ARRAY",
                priceText = "$14 / MONTH",
                benefits = listOf("Unlimited biomorphic coach splits", "Complete dietary tracker", "Infinite Gemini chats", "No platform ads"),
                actionText = if (user?.subscriptionRank == "NEXUS") "CURRENTLY COLLATED" else "UPGRADE FOR $14",
                glowColor = NeonBlue,
                onSelect = { activeTierSelection = Pair("NEXUS", 14) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // TITAN-X
            SaaSTierCard(
                title = "TITAN-X QUANTUM GRID",
                priceText = "$39 / MONTH",
                benefits = listOf("Double protein/macro calculations", "Body analytical BMI sliders", "Wearable telemetry synchronization", "Priority LLM nodes"),
                actionText = if (user?.subscriptionRank == "TITAN-X") "CURRENTLY SYNCHRONIZED" else "UPGRADE FOR $39",
                glowColor = NeonPurple,
                onSelect = { activeTierSelection = Pair("TITAN-X", 39) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // OMEGA INFINITY
            SaaSTierCard(
                title = "OMEGA INFINITY MATRIX",
                priceText = "$99 / MONTH",
                benefits = listOf("Complete premium metrics nodes", "Automated system reports", "VIP dashboard directories", "Personal AI coach mentors"),
                actionText = if (user?.subscriptionRank == "OMEGA INFINITY") "CORE PARAMOUNT ACTIVE" else "ENGAGE SUPREME MATRIX $99",
                glowColor = CyberMagenta,
                onSelect = { activeTierSelection = Pair("OMEGA INFINITY", 99) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Comparisons database grid
            SectionHeader(title = "Subscription Comparisons Grids", accentColor = NeonPurple)

            Spacer(modifier = Modifier.height(12.dp))

            GlowCard(borderColor = NeonPurple) {
                Text(
                     text = "SPEC CORE MATRIX VALIDATION",
                     fontSize = 11.sp,
                     fontFamily = FontFamily.Monospace,
                     fontWeight = FontWeight.Bold,
                     color = NeonPurple
                )
                Spacer(modifier = Modifier.height(12.dp))

                ComparisonTableRow("AI Chatbot Assistance", "10 Slots", "Unlimited", "Priority Neural", "Private Core")
                ComparisonTableRow("Local Room DB Logs", "Basic", "Advanced", "Quantum Speed", "Redundant Vault")
                ComparisonTableRow("Ad Interconnectivity", "Ads Enabled", "Zero Ads", "Zero Ads", "Zero Ads")
                ComparisonTableRow("Analytical Speedometers", "Speedometer Basic", "Speedometer Multi-split", "Speedometer High-glowing", "Speeodmeter Absolute Precision")
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // Overlay Stripe check-out modal
        AnimatedVisibility(
            visible = activeTierSelection != null,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f)),
                contentAlignment = Alignment.BottomCenter
            ) {
                activeTierSelection?.let { selection ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            .border(1.dp, NeonBlue, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            .testTag("stripe_checkout_panel"),
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0C0C14))
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            // Top row Close
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "STRIPE SAAS CHECKOUT MODULE",
                                    color = NeonBlue,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp
                                )

                                IconButton(
                                    onClick = { activeTierSelection = null },
                                    modifier = Modifier.testTag("close_checkout_btn")
                                ) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "close", tint = Color.LightGray)
                                }
                            }

                            Divider(color = Color(0xFF242435), modifier = Modifier.padding(vertical = 12.dp))

                            Text(
                                text = "UPGRADE CORE SELECTION: ${selection.first.uppercase()}",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "SECURE CHARGE TARGET: $${selection.second}.00 USD/MONTH",
                                color = CyberMagenta,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Card parameters fields input
                            CyberTextField(
                                value = mockCardNo,
                                onValueChange = { mockCardNo = it },
                                label = "Card Allocation Numbers",
                                leadingIcon = Icons.Default.CreditCard,
                                testTagStr = "checkout_card_input"
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(modifier = Modifier.fillMaxWidth()) {
                                Box(modifier = Modifier.weight(1f)) {
                                    CyberTextField(
                                        value = mockCardExp,
                                        onValueChange = { mockCardExp = it },
                                        label = "Expiry dates",
                                        testTagStr = "checkout_exp_input"
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(modifier = Modifier.weight(1f)) {
                                    CyberTextField(
                                        value = mockCardCvc,
                                        onValueChange = { mockCardCvc = it },
                                        label = "CVC Token",
                                        testTagStr = "checkout_cvc_input"
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Action Upgrade Button
                            if (stripeStatusState != null) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator(color = NeonBlue, modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = stripeStatusState!!,
                                        color = Color.Yellow,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            } else {
                                CyberButton(
                                    text = "INITIALIZE STRIPE API DEPLOYMENT",
                                    onClick = {
                                        viewModel.processStripeUpgrade(selection.first, selection.second)
                                    },
                                    glowColor = CyberMagenta,
                                    modifier = Modifier.fillMaxWidth(),
                                    testTagStr = "stripe_process_btn"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SaaSTierCard(
    title: String,
    priceText: String,
    benefits: List<String>,
    actionText: String,
    glowColor: Color,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                Brush.horizontalGradient(listOf(glowColor.copy(alpha = 0.8f), Color.Transparent)),
                RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F0F1A))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = priceText,
                    color = NeonBlue,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            benefits.forEach { benefit ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = glowColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = benefit,
                        color = TextWhite,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            CyberButton(
                text = actionText,
                onClick = onSelect,
                glowColor = glowColor,
                modifier = Modifier.fillMaxWidth(),
                testTagStr = "upgrade_tier_action"
            )
        }
    }
}

@Composable
fun ComparisonTableRow(title: String, origin: String, nexus: String, titan: String, omega: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        Text(
            text = title.uppercase(),
            color = NeonBlue,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ComparisonElement("ORIGIN:", origin)
            ComparisonElement("NEXUS:", nexus)
            ComparisonElement("TITAN-X:", titan)
            ComparisonElement("OMEGA:", omega)
        }

        Spacer(modifier = Modifier.height(4.dp))
        Divider(color = Color(0xFF1B1B2C))
    }
}

@Composable
fun ComparisonElement(prefix: String, value: String) {
    Column(
        modifier = Modifier.width(68.dp)
    ) {
        Text(text = prefix, color = TextGray, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
        Text(text = value, color = Color.White, fontSize = 9.sp, lineHeight = 12.sp)
    }
}
